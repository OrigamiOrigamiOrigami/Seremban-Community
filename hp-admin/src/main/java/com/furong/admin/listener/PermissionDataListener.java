package com.furong.admin.listener;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.furong.pojo.entity.Permission;
import com.furong.pojo.excel.PermissionExcelData;
import com.furong.service.PermissionService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限数据导入监听器
 * 有个很重要的点 PermissionDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 * @author Origami
 * @date 2025/9/13
 */
@Slf4j
public class PermissionDataListener implements ReadListener<PermissionExcelData> {

    /**
     * 每隔100条存储数据库，然后清理list，方便内存回收
     */
    private static final int BATCH_COUNT = 100;

    /**
     * 缓存的数据
     */
    private List<PermissionExcelData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private PermissionService permissionService;

    public PermissionDataListener() {
    }

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     * @param permissionService
     */
    public PermissionDataListener(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 这个每一条数据解析都会来调用
     * @param data
     * @param context
     */
    @Override
    public void invoke(PermissionExcelData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", data);
        cachedDataList.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            // 存储完成清理 list
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());

        List<Permission> permissionList = cachedDataList.stream()
                .filter(permissionData -> !StrUtil.isEmpty(permissionData.getPermissionName()))
                .map(permissionData -> {
                    Permission permission = new Permission();
                    permission.setPermissionName(permissionData.getPermissionName());
                    permission.setPermissionCode(permissionData.getPermissionCode());
                    permission.setDescription(permissionData.getDescription());
                    return permission;
                }).collect(Collectors.toList());

        //保存到数据库
        if (!permissionList.isEmpty()) {
            permissionService.saveBatch(permissionList);
            log.info("存储数据库成功！");
        }
    }
}
