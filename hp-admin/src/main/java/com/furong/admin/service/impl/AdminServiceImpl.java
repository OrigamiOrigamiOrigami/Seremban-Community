package com.furong.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import com.furong.admin.interceptor.AdminThreadLocal;
import com.furong.admin.mapper.AdminMapper;
import com.furong.admin.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import com.furong.pojo.dto.AdminQueryDto;
import com.furong.pojo.dto.UpdatePasswordDto;
import com.furong.pojo.vo.AdminInfoVo;
import com.furong.service.AdminService;
import com.furong.admin.utils.DigesterUtils;
import com.furong.admin.utils.TokenUtils;
import com.furong.exception.CustomerException;
import com.furong.pojo.dto.AdminAddDto;
import com.furong.pojo.dto.AdminLoginDto;
import com.furong.pojo.entity.Admin;
import com.furong.pojo.entity.Permission;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.furong.exception.ErrorCode;

/**
 * @author Origami
 * @date 2025/9/9
 */
@Slf4j
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public void insertAdmin(AdminAddDto adminAddDto) {

        //1.数据重复性校验
        if(adminAddDto == null){
            throw new CustomerException(ErrorCode.USER_NOT_ALLOW_NULL);
        }
        checkUsername(adminAddDto.getUsername());
        checkPhone(adminAddDto.getPhone());
        checkEmail(adminAddDto.getEmail());

        //2.生成盐
        String salt = DigesterUtils.createSalt();

        //3.默认使用手机号码的后6位作为初始密码
        String password = adminAddDto.getPhone().substring(5);

        //4.密码加密
        String md5Password = DigesterUtils.md5(password, salt);

        //5.拷贝dto的数据到entity
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminAddDto, admin);

        //6.设置盐和密码
        admin.setSalt(salt);
        admin.setPassword(md5Password);

        //7.设置租户ID
        AdminInfoVo currentAdmin = AdminThreadLocal.get();
        if (currentAdmin != null) {
            admin.setTenantId(currentAdmin.getTenantId());
        } else {
            admin.setTenantId(1L); // 默认租户ID为1
        }

        //7.保存用户数据
        this.save(admin);

        //8.发送通知邮件
        MailUtil.send(adminAddDto.getEmail(), "芙蓉医疗账号添加成功通知", 
            "<h1>芙蓉医疗账号添加成功，用户名为:" + admin.getUsername() + ",初始化密码为:" + password + "</h1>", true);

    }

    @Override
    public String login(AdminLoginDto adminLoginDto) {

        //1.校验
        if(adminLoginDto == null){
            throw new CustomerException(ErrorCode.USER_NOT_ALLOW_NULL);
        }

        //2.查询用户信息
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(Admin::getUsername, adminLoginDto.getUsername());
        Admin admin = this.getOne(queryWrapper);
        if(admin == null){
            log.error("用户名不存在:{}", adminLoginDto.getUsername());
            throw new CustomerException(ErrorCode.USER_PASSWORD_ERROR);
        }

        //3.获取盐，再次加密
        String salt = admin.getSalt();
        String md5Password = DigesterUtils.md5(adminLoginDto.getPassword(), salt);

        //4.对比两次密码是否一致
        if(!md5Password.equals(admin.getPassword())){
            log.error("密码错误:{}", adminLoginDto.getPassword());
            throw new CustomerException(ErrorCode.USER_PASSWORD_ERROR);
        }

        //5.生成JWT令牌
        Map<String, Object> map = new HashMap<>();
        map.put("id", admin.getId());
        map.put("username", admin.getUsername());
        map.put("phone", admin.getPhone());
        map.put("name", admin.getName());
        map.put("email", admin.getEmail());
        map.put("image", admin.getImage());
        map.put("tenantId", admin.getTenantId()); // 添加租户ID到JWT
        map.put("expire", System.currentTimeMillis() + 1000 * 60 * 60 * 2);

        String token = TokenUtils.createToken(map);

        // 6.获取用户权限并存入Redis
        List<Permission> permissions = permissionMapper.selectAdminPermissions(admin.getId());
        List<String> permissionCodes = permissions.stream()
                .map(Permission::getPermissionCode)
                .collect(Collectors.toList());

        // 构建Redis key: admin:permissions:tenantId:adminId
        String permissionKey = "admin:permissions:" + admin.getTenantId() + ":" + admin.getId();

        // 将权限列表存入Redis，设置2小时过期时间（与JWT一致）
        redisTemplate.opsForValue().set(permissionKey, permissionCodes, 2, TimeUnit.HOURS);

        log.debug("登录处理成功，生成的token:{}, 权限已缓存到Redis", token);

        return token;
    }

    @Override
    public void changePassword(UpdatePasswordDto updatePasswordDto) {

        //1.校验
        if(updatePasswordDto == null){
            throw new CustomerException(ErrorCode.PASSWORD_NOT_ALLOW_EMPTY);
        }

        //2.查询用户信息
        AdminInfoVo infoVo = AdminThreadLocal.get();
        if(infoVo == null){
            throw new CustomerException(ErrorCode.USER_NOT_LOGIN);
        }
        Admin admin = this.getById(infoVo.getId());

        //3.获取盐，再次加密
        String salt = admin.getSalt();
        String oldPassword = DigesterUtils.md5(updatePasswordDto.getOldPassword(), salt);
        if(!oldPassword.equals(admin.getPassword())){
            throw new CustomerException(ErrorCode.USER_PASSWORD_ERROR);
        }

        //4.新密码加密
        String newPassword = DigesterUtils.md5(updatePasswordDto.getNewPassword(), salt);
        admin.setSalt(salt);
        admin.setPassword(newPassword);
        this.updateById(admin);
    }

    private void checkUsername(String username) {
        if(StrUtil.isEmpty(username)){
            throw new CustomerException(ErrorCode.USER_NOT_ALLOW_NULL);
        }
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(Admin::getUsername, username);
        if(this.getOne(queryWrapper) != null){
            throw new CustomerException(ErrorCode.USER_ALREADY_EXISTS);
        }
    }

    private void checkPhone(String phone) {
        if(StrUtil.isEmpty(phone)){
            throw new CustomerException(ErrorCode.PHONE_NOT_ALLOW_EMPTY);
        }
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(Admin::getPhone, phone);
        if(this.getOne(queryWrapper) != null){
            throw new CustomerException(ErrorCode.PHONE_ALREADY_EXISTS);
        }
    }

    private void checkEmail(String email) {
        if(StrUtil.isEmpty(email)){
            throw new CustomerException(ErrorCode.EMAIL_NOT_ALLOW_EMPTY);
        }
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.eq(Admin::getEmail, email);
        if(this.getOne(queryWrapper) != null){
            throw new CustomerException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    @Override
    public Page<AdminInfoVo> getAdminListWithRoles(AdminQueryDto adminQueryDto) {
        // 使用PageHelper分页助手
        PageHelper.startPage(adminQueryDto.getPageNum(), adminQueryDto.getPageSize());
        return getMapper().selectAdminListWithRoles(adminQueryDto);
    }

    @Override
    public AdminInfoVo getAdminDetailWithRoles(Long adminId) {
        return getMapper().selectAdminDetailWithRoles(adminId);
    }

}
