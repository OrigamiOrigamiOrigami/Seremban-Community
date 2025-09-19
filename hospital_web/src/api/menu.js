import request from '@/utils/request'

const api_name = 'menu'

// 保存
export const saveMenu = (data) => {
  return request({
    url: `/${api_name}/add`,
    method: 'post',
    data: data
  })
}
// 删除
export const deleteMenu = (id) => {
  return request({
    url: `/${api_name}/delete/` + id,
    method: 'get'
  })
}
// 查找导航菜单树
export const getMenuTree = () => {
  return request({
    url: `/${api_name}/tree`,
    method: 'get'
  })
}

// 获取路由
export const getRouters = () => {
  return request({
    url: `/${api_name}/getRouters`,
    method: 'get'
  })
}

// 获取菜单列表
export const getMenus = () => {
  return request({
    url: `/${api_name}/all`,
    method: 'get'
  })
}

// 更新菜单
export function updateMenu(data) {
  return request({
    url: `/${api_name}/update`,
    method: 'post',
    data: data
  })
}

// 获取角色拥有菜单
export function getRoleHasMenu(id) {
  return request({
    url: `/${api_name}/getRoleHasMenu?id=` + id,
    method: 'get'
  })
}

