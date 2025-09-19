import request from '@/utils/request'

const api_name='/banner'

export default{


    //编写方法，向服务器发起请求
    search(searchMap){
      return request({
            url: `${api_name}/list`,
            method: 'post',
            data:searchMap
      })
    },
    update(id,pojo){

      //判断是否有id，如果有id就是修改
      if(id===null || id===''){
         return this.save(pojo)
      }

      //修改操作
      return request({
        url: `${api_name}/update`,
        method: 'post',
        data:pojo
     })

    },
    save(pojo){
      return request({
        url: `${api_name}/add`,
        method: 'post',
        data:pojo
     })
    },
    findById(id){
      return request({
        url: `${api_name}/`+id,
        method: 'get'
     })
    },
    deleteById(id){
      return request({
        url: `${api_name}/deleteById/?id=`+id,
        method: 'get'
     })
    }

}

