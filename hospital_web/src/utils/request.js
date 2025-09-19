import axios from 'axios'
import { ElMessageBox, ElMessage } from 'element-plus'
import store from '@/store'
import { getToken, setToken, removeToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 5000 // request timeout
})

// 请求拦截器
service.interceptors.request.use(
  config => {

    if (store.getters.token) {
      config.headers['Authorization'] = 'Bearer ' + getToken()
    }
    return config
  },
  error => {
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 如果是blob类型的响应（如文件下载），直接返回response
    if (response.config.responseType === 'blob') {
      return response
    }

    const res = response.data

    if (res.code !== 0) {
      // token相关错误处理 (401 未授权)
      if (res.code === 401) {
        ElMessageBox.confirm('登录状态已过期，您可以继续留在该页面，或者重新登录', '系统提示', {
          confirmButtonText: '重新登录',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload()
          })
        })
        // 对于401错误，不显示额外的错误消息，避免重复提示
        return Promise.reject(new Error(res.msg || 'Unauthorized'))
      } else {
        // 显示具体的错误消息
        const errorMessage = res.msg || '操作失败'
        ElMessage({
          message: errorMessage,
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(new Error(errorMessage))
      }
    } else {
      // 请求成功，存储后端生成的新token（如果有）
      const { token } = res

      if (token != null) {
        console.log('更新token: ' + token)
        // 设置token到cookie中
        setToken(token)
        // 设置token到state状态管理对象中
        store.commit('user/SET_TOKEN', token)
      }

      return res
    }
  },
  error => {
    console.log('请求错误: ' + error)

    // 处理网络错误和HTTP状态码错误
    if (error.response) {
      const { status, data } = error.response

      // 尝试从响应数据中获取具体的错误消息
      let errorMessage = '请求失败'
      if (data && data.msg) {
        errorMessage = data.msg
      } else if (status === 401) {
        errorMessage = '未授权，请重新登录'
        // 401错误时重置token并重新加载页面
        store.dispatch('user/resetToken').then(() => {
          location.reload()
        })
      } else if (status === 403) {
        errorMessage = '拒绝访问'
      } else if (status === 404) {
        errorMessage = '请求地址出错'
      } else if (status >= 500) {
        errorMessage = '服务器内部错误'
      } else if (status === 400) {
        errorMessage = data && data.msg ? data.msg : '请求参数错误'
      }

      // 只有在没有被上面的成功响应处理器处理过的情况下才显示错误消息
      // 避免重复显示错误消息
      if (!error.message || !error.message.includes('Request failed with status code')) {
        ElMessage({
          message: errorMessage,
          type: 'error',
          duration: 5 * 1000
        })
      }
    } else {
      // 网络错误
      ElMessage({
        message: '网络错误，请检查网络连接',
        type: 'error',
        duration: 5 * 1000
      })
    }

    return Promise.reject(error)
  }
)

export default service
