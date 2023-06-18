import axios from 'axios'
import router from '@/router'

// create an axios instance 创建axios实例
const instance = axios.create({
  baseURL: 'http://localhost:8081',
  withCredentials: false // 跨域时使用凭证,默认带上cookies
})

// 请求增加头信息token
instance.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = 'Bearer ' + token
    }
    return config
  }
)

// 请求验证token响应
instance.response.use(res => {
  if (res.data.code === 401) {
    router.replace('/login')
    localStorage.removeItem('token')
  }
})
