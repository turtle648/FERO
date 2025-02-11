import { createRouter, createWebHistory } from "vue-router"
import StartPage from "@/views/StartPage.vue"
import MainPage from "@/views/MainPage.vue"
import LoginPage from "@/views/LoginPage.vue"
import FitnessTutorialPage from "@/views/FitnessTutorialPage.vue"
import VideoRoomPage from "@/views/VideoRoomPage.vue"
import SingleModePage from "@/views/SingleModePage.vue"
import MultiModePage from "@/views/MultiModePage.vue"
import RankModePage from "@/views/RankModePage.vue"
// import MediapipeComponent from "@/components/MediapipeComponent.vue" // fix: 수정예정
import QRComponent from "@/components/QRComponent.vue"
import RankMatchPage from "@/views/RankMatchPage.vue"
import UiTutorialPage from "@/views/UiTutorialPage.vue"

// 랭크매치, 랭크모드에 인증 관련 메타데이터 고려해볼 것
// meta: {
//   requiresAuth: true,  // 인증 필요
//   isMobile: true      // 모바일 전용

// 에러페이지 라우팅 고려해볼 것것
// {
//   path: '/:pathMatch(.*)*',
//   name: 'NotFound',
//   component: NotFoundPage
// }

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
    path: "/tutorial/:exercise",
    name: "Tutorial",
    component: FitnessTutorialPage,
    meta: { isMobile: true },
  },
  {
    path: "/fitness",
    name: "VideoRoom",
    component: VideoRoomPage,
    meta: { isMobile: true },
  },
  // {
  //   path: "/mediapipe",
  //   name: "Mediapipe",
  //   component: MediapipeComponent,
  //   meta: { isMobile: true },
  // },
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
    path: "/qr",
    name: "QR",
    component: QRComponent,
  },
  {
    path: "/rank-match/:exercise",
    name: "RankMatch",
    component: RankMatchPage,
    props: true,
  },
  {
    path: '/ui-tutorial',
    name: 'UiTutorial',
    component: UiTutorialPage,
    meta: { isMobile: true }
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 네비게이션 가드 설정
// router.beforeEach((to, from, next) => {
//   const token = localStorage.getItem("authToken")
//   const isMobile = "ontouchstart" in window || navigator.maxTouchPoints > 0 // 터치스크린 기기 감지

//   if (to.path !== "/qr" && to.meta.isMobile && !isMobile) {
//     next("/qr")
//     console.log("모바일이 아닙니다 → /qr로 이동")
//     return
//   }

//   if (to.meta.requiresAuth && !token) {
//     next("/start")
//     console.log("토큰 없어서 /start로 이동")
//   } else {
//     next()
//   }
// })

export default router
