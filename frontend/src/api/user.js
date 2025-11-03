import axios from 'axios'
import request from '../utils/request'
import { showMessage } from '../utils/message.js'

// 创建一个无认证的axios实例用于公开接口
const publicRequest = axios.create({
  timeout: 10000,
  baseURL: '' // 使用空baseURL，让路径完全由API函数控制
})

// publicRequest响应拦截器 - 确保错误提示
publicRequest.interceptors.response.use(
  response => {
    const res = response.data
    
    // 根据后端返回的数据格式，这里可以做统一的错误处理
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
    
    console.error('响应错误:', errorMessage)
    // 显示通用错误提示
    showMessage(errorMessage || '网络请求失败，请稍后重试', 'error')
    return Promise.reject(new Error(errorMessage || '请求失败'))
  }
)

// 用户登录 - 使用无认证的axios实例，与sendEmailCode保持一致的模式
export const login = (data) => {
  return publicRequest({
    url: '/api/user/login', // 添加/api前缀，与代理配置匹配
    method: 'post',
    data
  })
}

// 发送邮箱验证码 - 使用无认证的axios实例
export const sendEmailCode = (email) => {
  return publicRequest({
    url: '/api/user/sendEmailCode', // 添加/api前缀，与代理配置匹配
    method: 'post',
    data: { email }
  })
}

// 新增用户 - 使用无认证的axios实例
export const addUser = (data) => {
  return publicRequest({
    url: '/api/user/add', // 添加/api前缀，与代理配置匹配
    method: 'post',
    data
  })
}

// 修改密码
export const changePassword = (data) => {
  return request({
    url: '/user/changePassword',
    method: 'post',
    data
  })
}

// 修改用户信息
export const updateUser = (data) => {
  return request({
    url: '/user/update',
    method: 'post',
    data
  })
}