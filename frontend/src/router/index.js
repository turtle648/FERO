import { createRouter, createWebHistory } from 'vue-router'
import StartPage from '@/views/StartPage.vue'
import MainPage from '@/views/MainPage.vue'

const routes = [
  {
    path: '/start',
    name: 'Start',
    component: StartPage
  },
  {
    path: '/main',
    name: 'Main',
    component: MainPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
