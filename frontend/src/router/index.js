import { createRouter, createWebHistory } from "vue-router"
import StartPage from "@/views/StartPage.vue"
import MainPage from "@/views/MainPage.vue"
import FitnessTutorialPage from "@/views/FitnessTutorialPage.vue"
import SingleModePage from "@/views/SingleModePage.vue"
import QRComponent from "@/components/QRComponent.vue"
import RankMatchPage from "@/views/RankMatchPage.vue"
import UiTutorialPage from "@/views/UiTutorialPage.vue"
import TestVoice from "@/components/voice/testVoice.vue"
import RankMatchResultPage from "@/views/RankMatchResultPage.vue"
import setUp from "@/views/setUp.vue"
import { useUserStore } from "@/stores/store"
import { useUserDataStore } from "@/stores/userDataStore"

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
    path: "/",
    name: "Start",
    component: StartPage,
    meta: { isMobile: true, requiresAuth: false },
  },
  {
    path: "/main",
    name: "Main",
    component: MainPage,
    meta: { isMobile: true, requiresAuth: true },
  },
  {
    path: "/tutorial/:exercise",
    name: "Tutorial",
    component: FitnessTutorialPage,
    meta: { isMobile: true, requiresAuth: true },
  },
  {
    path: "/single-mode/:exercise/:count",
    name: "SingleMode",
    component: SingleModePage,
    props: true,
    meta: { isMobile: true, requiresAuth: true },
  },
  // {
  //   path: "/multi-mode/:exercise",
  //   name: "MultiMode",
  //   component: MultiModePage,
  //   props: true,
  //   meta: { isMobile: true, requiresAuth: true },
  // },
  {
    path: "/rank-mode/:exercise",
    name: "RankMode",
    component: RankMatchPage,
    props: true,
    meta: { isMobile: true, requiresAuth: true },
  },
  {
    path: "/rank-result",
    name: "RankResult",
    component: RankMatchResultPage,
    meta: { isMobile: true, requiresAuth: true },
  },
  {
    path: "/qr",
    name: "QR",
    component: QRComponent,
  },
  {
    path: "/setUp",
    name: "SetUp",
    component: setUp,
  },
  {
    path: "/rank-match/:exercise",
    name: "RankMatch",
    component: RankMatchPage,
    props: true,
    meta: { isMobile: true, requiresAuth: true },
  },
  {
    path: "/ui-tutorial",
    name: "UiTutorial",
    component: UiTutorialPage,
    meta: { isMobile: true, requiresAuth: true },
  },
  // 백그라운드 음성인식 반응속도 테스트용
  {
    path: "/testVoice",
    name: "testVoice",
    component: TestVoice,
    meta: { isMobile: true, requiresAuth: true },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 네비게이션 가드 설정
router.beforeEach(async (to, from, next) => {
  const userAgent = navigator.userAgent.toLowerCase()
  const isTouchDevice = "ontouchstart" in window || navigator.maxTouchPoints > 0
  const isMobileDevice = /android|iphone|ipad|apple|ipod|blackberry|opera mini|iemobile|wpdesktop/i.test(userAgent)
  const isTablet = /ipad|tablet|playbook|silk/i.test(userAgent)
  const isWindowsPC = /windows nt/i.test(userAgent) && !isTablet // Windows PC (태블릿 제외)
  const isMacPC = /macintosh/i.test(userAgent) && !isTouchDevice // Mac (터치스크린이 없을 경우)
  const isMobile = (isMobileDevice || isTablet) && !isWindowsPC && !isMacPC

  // 📌 PWA 여부 확인
  const isPWA = window.matchMedia("(display-mode: standalone)").matches || window.navigator.standalone

  console.log("User-Agent:", userAgent)
  console.log("터치 디바이스 여부:", isTouchDevice)
  console.log("모바일 디바이스:", isMobileDevice)
  console.log("태블릿 여부:", isTablet)
  console.log("Windows PC:", isWindowsPC)
  console.log("Mac PC:", isMacPC)
  console.log("최종 모바일 판정:", isMobile)
  console.log("PWA 여부:", isPWA)

  // ==================================================
  if (to.path === "/qr") {
    return next()
  }
  if (!isMobile) {
    return next("/qr")
  }

  // PWA가 아닌 경우 `/setUp` 페이지로 이동
  if (!isPWA && to.path !== "/setUp") {
    return next("/setUp")
  }
  
  if (to.path === "/" || to.path === "" || to.path === ".") {
    return next()
  }

  const userStore = useUserStore()
  const userDataStore = useUserDataStore()
  // 토큰 없는경우
  if (to.meta.requiresAuth && (!localStorage.getItem("authToken") || !localStorage.getItem("userId"))) {
    console.log("토큰 없음")
    userStore.clearSession()
    return next("/")
  }
  // 토큰 유효성 검증 실패
  const isTokenValid = await userDataStore.checkUserInfo()
  console.log(isTokenValid)
  if (!isTokenValid) {
    console.log("토큰 유효성 검사 실패")
    userStore.clearSession()
    return next("/")
  }

  return next()
})

export default router
