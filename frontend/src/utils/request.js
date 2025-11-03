import axios from 'axios'

//// 创建axios实例
const service = axios.create({
  timeout: 10000, // 请求超时时间
  baseURL: '/api/puzzle/v2'
})

// request拦截器
service.interceptors.request.use(
  config => {
    // 这里可以添加token等认证信息
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误:', error)
    return Promise.reject(error)
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
      // 如果响应中包含token，保存到localStorage
      if (res.data && res.data.token) {
        localStorage.setItem('token', res.data.token)
        // 保存用户信息
        if (res.data.user) {
          localStorage.setItem('user', JSON.stringify(res.data.user))
        }
      }
      return res
    }
  },
  error => {
    // 优化错误信息显示
    let errorMessage = error.message
    if (error.response && error.response.data) {
      errorMessage = error.response.data.message || errorMessage
    }
    
    // 401未授权，清除token并跳转到登录页
    if (error.response && error.response.status === 401) {
      console.log('未授权，请重新登录')
      // 清除token和用户信息
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      // 跳转到登录页
      window.location.href = '/login'
    }
    // 特殊处理邮箱未验证错误
    else if (errorMessage.includes('请先验证您的邮箱')) {
      console.error('邮箱验证错误:', errorMessage)
    } else {
      console.error('响应错误:', errorMessage)
    }
    return Promise.reject(new Error(errorMessage || '请求失败'))
  }
)

export default service