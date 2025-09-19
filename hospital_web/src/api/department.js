import request from '@/utils/request'

const api_name = '/department'

export default {
  // 分页查询科室列表
  search(searchMap) {
    return request({
      url: `${api_name}/list`,
      method: 'post',
      data: searchMap
    })
  },

  // 查询所有科室
  findAll() {
    return request({
      url: `${api_name}/findAll`,
      method: 'get'
    })
  },

  // 根据ID查询科室
  findById(id) {
    return request({
      url: `${api_name}/find`,
      method: 'get',
      params: { id }
    })
  },

  // 新增科室
  save(pojo) {
    return request({
      url: `${api_name}/add`,
      method: 'post',
      data: pojo
    })
  },

  // 更新科室
  update(pojo) {
    return request({
      url: `${api_name}/update`,
      method: 'post',
      data: pojo
    })
  },

  // 新增或更新科室
  saveOrUpdate(id, pojo) {
    if (id === null || id === '' || id === undefined) {
      return this.save(pojo)
    }
    return this.update(pojo)
  },

  // 删除科室
  deleteById(id) {
    return request({
      url: `${api_name}/delete`,
      method: 'get',
      params: { id }
    })
  },

  // 批量删除科室
  deleteBatch(batchDeleteDto) {
    return request({
      url: `${api_name}/deleteBatch`,
      method: 'post',
      data: batchDeleteDto
    })
  }
}
