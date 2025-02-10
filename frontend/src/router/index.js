import { createRouter, createWebHistory } from "vue-router"
import StartPage from "@/views/StartPage.vue"
import MainPage from "@/views/MainPage.vue"
import LoginPage from "@/views/LoginPage.vue"
import TutorialPage from "@/views/tutorial/SquatTutorialPage.vue"
import VideoRoomPage from "@/views/VideoRoomPage.vue"
import SingleModePage from "@/views/SingleModePage.vue"
import MultiModePage from "@/views/MultiModePage.vue"
import RankModePage from "@/views/RankModePage.vue"
import mediapipeComponent from "@/components/mediapipeComponent.vue" // fix: 수정예정
import SquartComponent from "@/components/SquartComponent.vue"
import QRComponent from "@/components/QRComponent.vue"

const routes = [
  {
    path: "/start",
    name: "Start",
    component: StartPage,
    meta: { isMobile: true },
  },
  {
    path: "/main",
    name: "Main",
    component: MainPage,
    meta: { isMobile: true },
  },
  {
    path: "/login",
    name: "Login",
    component: LoginPage,
    meta: { isMobile: true },
  },
  {
    path: "/tutorial",
    name: "Tutorial",
    component: TutorialPage,
    meta: { isMobile: true },
  },
  {
    path: "/fitness",
    name: "VideoRoom",
    component: VideoRoomPage,
    meta: { isMobile: true },
  },
  {
    path: "/mediapipe",
    name: "mediapipe",
    component: mediapipeComponent,
    meta: { isMobile: true },
  },
  {
    path: "/single-mode/:exercise/:count",
    name: "SingleMode",
    component: SingleModePage,
    props: true,
  },
  {
    path: "/multi-mode/:exercise",
    name: "MultiMode",
    component: MultiModePage,
    props: true,
  },
  {
    path: "/rank-mode/:exercise",
    name: "RankMode",
    component: RankModePage,
    props: true,
  },
  {
    path: "/squart",
    name: "squart",
    component: SquartComponent,
  },
  {
    path: "/qr",
    name: "QR",
    component: QRComponent,
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 네비게이션 가드 설정
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("authToken")
  const isMobile = /Mobi|Android|iPhone/i.test(navigator.userAgent)

  if (to.meta.isMobile && !isMobile) {
    next("/qr")
    console.log("📱 모바일이 아닙니다")
    return
  }

  if (to.meta.requiresAuth && !token) {
    next("/start")
    console.log("토큰 없어서 start페이지로 돌아감:)")
  } else {
    next()
  }
})

export default router
