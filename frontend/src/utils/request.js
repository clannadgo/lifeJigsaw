import axios from 'axios'
import { showMessage } from './message.js'

// 解析JWT token函数
function parseJwt(token) {
  console.log('开始解析token:', token ? 'token存在' : 'token不存在');
  try {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    const parsedToken = JSON.parse(jsonPayload);
    console.log('token解析结果:', parsedToken);
    console.log('token中的isAdmin字段值:', parsedToken.isAdmin);
    return parsedToken;
  } catch (e) {
    console.error('解析token失败:', e);
    return null;
  }
}

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
      
      // 优先以isSuccess字段为准判断请求是否成功
      if (res.hasOwnProperty('isSuccess')) {
        if (!res.isSuccess) {
          // isSuccess为false时，显示错误信息
          console.error('响应错误:', res.message)
          showMessage(res.message || '请求失败', 'error')
          return Promise.reject(new Error(res.message || '请求失败'))
        }
      } else if (res.code && res.code !== '0000' && res.code !== 200) {
        // 兼容旧的code判断逻辑
        console.error('响应错误:', res.message)
        showMessage(res.message || '请求失败', 'error')
        return Promise.reject(new Error(res.message || '请求失败'))
      }
      
      // 请求成功，处理token和用户信息
      console.log('处理响应中的token和用户信息...');
      if (res.data && res.data.token) {
        console.log('响应中包含token，开始处理...');
        localStorage.setItem('token', res.data.token)
        if (res.data.user) {
          console.log('响应中包含用户信息，原始user对象:', res.data.user);
          // 从token中解析isAdmin信息
          const tokenData = parseJwt(res.data.token);
          if (tokenData && tokenData.isAdmin !== undefined) {
            console.log('从token中获取到isAdmin值:', tokenData.isAdmin);
            // 使用token中的isAdmin字段更新用户信息
            res.data.user.isAdmin = tokenData.isAdmin;
            console.log('更新后user对象中的isAdmin值:', res.data.user.isAdmin);
          } else {
            console.log('token中未找到isAdmin字段，确保isAdmin默认存在');
            // 确保isAdmin字段存在，默认为false
            res.data.user.isAdmin = res.data.user.isAdmin || false;
            console.log('设置默认isAdmin值后:', res.data.user.isAdmin);
          }
          console.log('保存到localStorage前的最终user对象:', res.data.user);
          localStorage.setItem('user', JSON.stringify(res.data.user))
          console.log('已成功保存用户信息到localStorage');
        }
      } else {
        console.log('响应中不包含token或res.data不存在');
      }
      return res
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

// 设置publicRequest的响应拦截器
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

// 优化后的service响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    // 优先以isSuccess字段为准判断请求是否成功
    if (res.hasOwnProperty('isSuccess')) {
      if (!res.isSuccess) {
        // isSuccess为false时，显示错误信息
        showMessage(res.message || '请求失败', 'error')
        return Promise.reject(new Error(res.message || '请求失败'))
      }
    } else if (res.code && res.code !== '0000' && res.code !== 200) {
      // 兼容旧的code判断逻辑
      showMessage(res.message || '请求失败', 'error')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    // 请求成功，处理token和用户信息
    if (res.data && res.data.token) {
      localStorage.setItem('token', res.data.token)
      if (res.data.user) {
        // 从token中解析isAdmin信息
        const tokenData = parseJwt(res.data.token);
        if (tokenData && tokenData.isAdmin !== undefined) {
          console.log('service拦截器 - 从token中获取到isAdmin值:', tokenData.isAdmin);
          // 使用token中的isAdmin字段更新用户信息
          res.data.user.isAdmin = tokenData.isAdmin;
        } else {
          // 确保isAdmin字段存在，默认为false
          res.data.user.isAdmin = res.data.user.isAdmin || false;
        }
        localStorage.setItem('user', JSON.stringify(res.data.user))
      }
    } else {
      console.log('service拦截器 - 响应中不包含token或res.data不存在');
    }
    return res
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