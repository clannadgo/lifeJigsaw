import request from '../utils/request'

// 用户登录
export const login = (data) => {
  return request({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 发送邮箱验证码
export const sendEmailCode = (email) => {
  return request({
    url: '/user/sendEmailCode',
    method: 'post',
    params: { email }
  })
}

// 新增用户
export const addUser = (data) => {
  return request({
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