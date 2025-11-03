import { createRouter, createWebHistory } from 'vue-router'

// 导入页面组件
import Home from '../views/Home.vue'
import Register from '../views/Register.vue'
import MainLayout from '../layouts/MainLayout.vue'

// 路由配置
const routes = [
  {
    path: '/',
    component: MainLayout,
    meta: {
      requiresAuth: true
    },
    children: [
      {
        path: '',
        name: 'Home',
        component: Home,
        meta: {
          title: '人生拼图 - 首页'
        }
      }
    ]
  },
  // 登录页面路由
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: {
      title: '登录',
      requiresAuth: false
    }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: {
      title: '人生拼图 - 用户注册',
      requiresAuth: false
    }
  },
  // 捕获所有未匹配的路由，重定向到首页
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由前置守卫 - 设置页面标题和认证检查
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title || '人生拼图'
  
  // 检查是否需要认证
  const requiresAuth = to.meta.requiresAuth !== false // 默认需要认证
  const hasToken = !!localStorage.getItem('token')
  
  if (requiresAuth && !hasToken) {
    // 需要认证但没有token，重定向到登录页
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (!requiresAuth && hasToken && to.path === '/login') {
    // 不需要认证且有token，且当前是登录页，重定向到首页
    next({ path: '/' })
  } else {
    // 其他情况正常继续
    next()
  }
})

export default router