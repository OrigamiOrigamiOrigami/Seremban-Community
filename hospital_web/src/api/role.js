import request from '@/utils/request'
const api_name = 'role'
export default {

  getRoleList(adminId) {
    return request({
      url: `/${api_name}/findByAdminId/?id=` + adminId,
      method: 'get'
    })
  },
  getAll() {
    return request({
      url: `/${api_name}/all`,
      method: 'get'
    })
  },
  getList() {
    return request({
      url: `/${api_name}`,
      method: 'get'
    })
  },
  search(searchMap) {
    return request({
      url: `/${api_name}/list`,
      method: 'post',
      data: searchMap
    })
  },
  save(pojo) {
    return request({
      url: `/${api_name}/add`,
      method: 'post',
      data: pojo
    })
  },
  findById(id) {
    return request({
      url: `/${api_name}/find/${id}`,
      method: 'get'
    })
  },
  update(pojo) {
    return request({
      url: `/${api_name}/update`,
      method: 'put',
      data: pojo
    })
  },
  deleteById(id) {
    return request({
      url: `/${api_name}/deleteById/` + id,
      method: 'get'
    })
  },
  updateStatus(pojo) {
    return request({
      url: `/${api_name}/status`,
      method: 'post',
      data: pojo
    })
  },
  updateBaseInfo(pojo) {
    return request({
      url: `/${api_name}/updateBaseInfo`,
      method: 'post',
      data: pojo
    })
  },
  addBaseInfo(pojo) {
    return request({
      url: `/${api_name}/addBaseInfo`,
      method: 'post',
      data: pojo
    })
  },
  delete(id) {
    return request({
      url: `/${api_name}/delete?id=` + id,
      method: 'get'
    })
  },
  updateRoleMenu(roleMenuPojo) {
    return request({
      url: `/${api_name}/updateRoleMenu`,
      method: 'post',
      data: roleMenuPojo
    })
  },

  // 为角色分配权限
  assignPermissions(roleId, permissionIds) {
    return request({
      url: `/${api_name}/assignPermissions/${roleId}`,
      method: 'post',
      data: permissionIds
    })
  },

  // 获取角色的权限ID列表
  getRolePermissions(roleId) {
    return request({
      url: `/${api_name}/permissions/${roleId}`,
      method: 'get'
    })
  }
}
