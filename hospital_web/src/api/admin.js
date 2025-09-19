import request from '@/utils/request'

const api_name = '/admin'

export default {
  // 管理员登录
  login(loginForm) {
    return request({
      url: `${api_name}/login`,
      method: 'post',
      data: loginForm
    })
  },

  // 获取当前用户信息
  getCurrentUser() {
    return request({
      url: `${api_name}/info`,
      method: 'get'
    })
  },

  // 分页查询管理员列表
  search(searchMap) {
    return request({
      url: `${api_name}/list`,
      method: 'post',
      data: searchMap
    })
  },

  // 查询所有管理员
  findAll() {
    return request({
      url: `${api_name}/findAll`,
      method: 'get'
    })
  },

  // 根据ID查询管理员
  findById(id) {
    return request({
      url: `${api_name}/info`,
      method: 'get',
      params: { id }
    })
  },

  // 新增管理员
  save(pojo) {
    return request({
      url: `${api_name}/add`,
      method: 'post',
      data: pojo
    })
  },

  // 更新管理员
  update(pojo) {
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data: pojo
    })
  },

  // 新增或更新管理员
  saveOrUpdate(id, pojo) {
    if (id === null || id === '' || id === undefined) {
      return this.save(pojo)
    }
    return this.update(pojo)
  },

  // 删除管理员
  deleteById(id) {
    return request({
      url: `${api_name}/delete`,
      method: 'get',
      params: { id }
    })
  },

  // 批量删除管理员
  deleteBatch(ids) {
    return request({
      url: `${api_name}/deleteBatch`,
      method: 'post',
      data: { ids }
    })
  },

  // 上传头像
  uploadAvatar(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request({
      url: '/upload/avatar',
      method: 'post',
      data: formData,
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },

  // 修改密码
  changePassword(data) {
    return request({
      url: `${api_name}/changePassword`,
      method: 'post',
      data: data
    })
  }
}
