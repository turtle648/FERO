import { createRouter, createWebHistory } from 'vue-router'
import StartPage from '@/views/StartPage.vue'
import MainPage from '@/views/MainPage.vue'
import LoginPage from '@/views/LoginPage.vue'
import VideoRoomPage from "@/views/VideoRoomPage.vue";

const routes = [
  {
    path: '/start',
    name: 'Start',
    component: StartPage
  },
  {
    path: '/main',
    name: 'Main',
    component: MainPage,
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginPage
  },
  {
    path: '/fitness',
    name: 'VideoRoom',
    component: VideoRoomPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 네비게이션 가드 설정
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('authToken')
  
  if (to.meta.requiresAuth && !token) {
    next('/start')
    console.log('토큰 없어서 start페이지로 돌아감:)')
  } else {
    next()
  }
})

export default router