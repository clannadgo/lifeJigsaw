import axios from 'axios'
import { showMessage } from './message.js'

// 创建axios实例 - 用于需要认证的接口
const service = axios.create({
  timeout: 10000, // 请求超时时间
  baseURL: '/api/puzzle/v2'
})

// 创建axios实例 - 用于公开接口（不需要认证）
export const publicRequest = axios.create({
  timeout: 10000,
  baseURL: '' // 使用空baseURL，让路径完全由API函数控制
})

// 通用响应拦截器配置函数
function setupResponseInterceptor(instance) {
  instance.interceptors.response.use(
    response => {
      const res = response.data
      
      // 统一的响应成功判断逻辑
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
      } else {
        console.error('响应错误:', errorMessage)
        // 显示通用错误提示
        showMessage(errorMessage || '网络请求失败，请稍后重试', 'error')
      }
      return Promise.reject(new Error(errorMessage || '请求失败'))
    }
  )
}

// 设置拦截器
setupResponseInterceptor(service)
setupResponseInterceptor(publicRequest)

// request拦截器 - 用于需要认证的接口
service.interceptors.request.use(
  config => {
    // 添加token认证信息
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