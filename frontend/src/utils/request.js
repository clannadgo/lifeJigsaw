import axios from 'axios'
import { showMessage } from './message.js'

//// 创建axios实例
const service = axios.create({
  timeout: 10000, // 请求超时时间
  baseURL: '/api/puzzle/v2'
})

// 公开接口路径白名单
const PUBLIC_PATHS = [
  '/user/login',
  '/user/add',
  '/user/sendEmailCode'
]

// request拦截器
service.interceptors.request.use(
  config => {
    // 检查是否为公开接口
    const isPublicPath = PUBLIC_PATHS.some(path => config.url.includes(path))
    
    if (isPublicPath) {
      // 对于公开接口，显式删除Authorization头，确保不会发送认证信息
      delete config.headers['Authorization']
    } else {
      // 非公开接口才添加token认证信息
      const token = localStorage.getItem('token')
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`
      }
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
      // 显示错误提示
      showMessage(res.message || '请求失败', 'error')
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
      const errorMsg = '未授权，请重新登录'
      console.log(errorMsg)
      showMessage(errorMsg, 'error')
      // 清除token和用户信息
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      // 跳转到登录页
      setTimeout(() => {
        window.location.href = '/login'
      }, 1500)
    }
    // 特殊处理邮箱未验证错误
    else if (errorMessage.includes('请先验证您的邮箱')) {
      console.error('邮箱验证错误:', errorMessage)
      showMessage(errorMessage, 'error')
    } else {
      console.error('响应错误:', errorMessage)
      // 显示通用错误提示
      showMessage(errorMessage || '网络请求失败，请稍后重试', 'error')
    }
    return Promise.reject(new Error(errorMessage || '请求失败'))
  }
)

export default service