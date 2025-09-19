import request from '@/utils/request'

const api_name = '/permission'

export default {
  // 分页查询权限列表
  search(searchMap) {
    return request({
      url: `${api_name}/list`,
      method: 'post',
      data: searchMap
    })
  },

  // 查询所有权限
  findAll() {
    return request({
      url: `${api_name}/findAll`,
      method: 'get'
    })
  },

  // 添加权限
  save(permission) {
    return request({
      url: `${api_name}/save`,
      method: 'post',
      data: permission
    })
  },

  // 更新权限
  update(permission) {
    return request({
      url: `${api_name}/update`,
      method: 'put',
      data: permission
    })
  },

  // 根据ID查询权限详情
  findById(id) {
    return request({
      url: `${api_name}/find/${id}`,
      method: 'get'
    })
  },

  // 删除权限
  delete(ids) {
    return request({
      url: `${api_name}/delete`,
      method: 'delete',
      data: { ids: ids }
    })
  },

  // 获取管理员的权限列表
  getAdminPermissions(adminId) {
    return request({
      url: `${api_name}/adminPermissions/${adminId}`,
      method: 'get'
    })
  },

  // 检查管理员是否拥有指定权限
  checkPermission(adminId, permissionCode) {
    return request({
      url: `${api_name}/checkPermission/${adminId}/${permissionCode}`,
      method: 'get'
    })
  }
}
