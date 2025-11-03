import request, { publicRequest } from '../utils/request'

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