import axios from 'axios'
import request from '../utils/request'

// 创建一个无认证的axios实例用于公开接口
const publicRequest = axios.create({
  timeout: 10000,
  baseURL: '/api/puzzle/v2'
})

// 用户登录 - 仍然使用原request，因为可能需要处理token响应
export const login = (data) => {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 发送邮箱验证码 - 使用无认证的axios实例
export const sendEmailCode = (email) => {
  return publicRequest({
    url: '/user/sendEmailCode',
    method: 'post',
    data: { email }
  })
}

// 新增用户 - 使用无认证的axios实例
export const addUser = (data) => {
  return publicRequest({
    url: '/user/add',
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