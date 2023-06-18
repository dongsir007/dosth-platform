import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'login',
    component: LoginView
  },
  {
    path: '/index',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

const whiteList = ['/']
router.beforeEach((to, from, next) => {
  // 登录或关于直接跳转
  if (to.path === '/' || to.path === '/about') {
    next()
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      const token = window.localStorage.token
      token ? next() : next({ path: '/' })
    }
  }
})

export default router
