import { createRouter, createWebHistory } from "vue-router"
import StartPage from "@/views/StartPage.vue"
import MainPage from "@/views/MainPage.vue"
import LoginPage from "@/views/LoginPage.vue"
import TutorialPage from "@/views/tutorial/SquatTutorialPage.vue"
import VideoRoomPage from "@/views/VideoRoomPage.vue"
import mediapipeComponent from "@/components/mediapipeComponent.vue" // fix: 수정예정
import SquartComponent from "@/components/SquartComponent.vue"

const routes = [
  {
    path: "/start",
    name: "Start",
    component: StartPage,
  },
  {
    path: "/main",
    name: "Main",
    component: MainPage,
    meta: { requiresAuth: true },
  },
  {
    path: "/login",
    name: "Login",
    component: LoginPage,
  },
  {
    path: "/tutorial",
    name: "Tutorial",
    component: TutorialPage,
  },
  {
    path: "/fitness",
    name: "VideoRoom",
    component: VideoRoomPage,
  },
  {
    path: "/mediapipe",
    name: "mediapipe",
    component: mediapipeComponent,
  },
  {
    path: "/squart",
    name: "squart",
    component: SquartComponent,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 네비게이션 가드 설정
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("authToken")

  if (to.meta.requiresAuth && !token) {
    next("/start")
    console.log("토큰 없어서 start페이지로 돌아감:)")
  } else {
    next()
  }
})

export default router
