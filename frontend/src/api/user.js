import request from '../utils/request'

// 用户登录
export const login = (data) => {
  return request({
    url: '/api/user/login',
    method: 'post',
    data
  })
}

// 新增用户
export const addUser = (data) => {
  return request({
    url: '/api/user/add',
    method: 'post',
    data
  })
}

// 修改密码
export const changePassword = (data) => {
  return request({
    url: '/api/user/changePassword',
    method: 'post',
    data
  })
}

// 修改用户信息
export const updateUser = (data) => {
  return request({
    url: '/api/user/update',
    method: 'post',
    data
  })
}