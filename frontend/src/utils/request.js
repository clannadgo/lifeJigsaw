import axios from 'axios'

//// 创建axios实例
const service = axios.create({
  timeout: 10000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
  config => {
    // 这里可以添加token等认证信息
    // const token = localStorage.getItem('token')
    // if (token) {
    //   config.headers['Authorization'] = `Bearer ${token}`
    // }
    return config
  },
  error => {
    console.error('请求错误:', error)
    Promise.reject(error)
  }
)

// respone拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 根据后端返回的数据格式，这里可以做统一的错误处理
    // 假设后端返回的数据格式为 { code: 200, message: 'success', data: {} }
    if (res.code && res.code !== '0000' && res.code !== 200) {
      // 错误处理逻辑
      console.error('响应错误:', res.message)
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    // 优化错误信息显示
    let errorMessage = error.message
    if (error.response && error.response.data) {
      errorMessage = error.response.data.message || errorMessage
    }
    // 特殊处理邮箱未验证错误
    if (errorMessage.includes('请先验证您的邮箱')) {
      console.error('邮箱验证错误:', errorMessage)
    } else {
      console.error('响应错误:', errorMessage)
    }
    return Promise.reject(new Error(errorMessage || '请求失败'))
  }
)

export default service