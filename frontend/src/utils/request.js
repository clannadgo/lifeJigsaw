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
    if (res.code && res.code !== 200) {
      // 错误处理逻辑
      console.error('响应错误:', res.message)
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      return res
    }
  },
  error => {
    console.error('响应错误:', error.message)
    // 可以在这里添加统一的错误处理，如网络错误提示等
    return Promise.reject(error)
  }
)

export default service