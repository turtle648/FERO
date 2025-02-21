import { defineStore } from "pinia"
import { ref } from "vue"
import axios from "axios"
import router from "@/router"

export const useUserStore = defineStore("user", () => {
  const BASE_URL = "https://i12e103.p.ssafy.io:8076/api/v1"

  const accessToken = ref(localStorage.getItem("authToken") || null)
  const sessionId = ref(localStorage.getItem("sessionId") || null)
  const userId = ref(localStorage.getItem("userId") || null)

  function setAccessToken(token) {
    accessToken.value = token
    localStorage.setItem("authToken", token)
  }

  function setSessionId(sessionIdValue) {
    sessionId.value = sessionIdValue
    localStorage.setItem("sessionId", sessionIdValue)
  }

  function clearSession() {
    localStorage.removeItem("authToken")
    localStorage.removeItem("sessionId")
    localStorage.removeItem("userId")
    accessToken.value = null
    sessionId.value = null
    userId.value = null
  }

  function setUserId(userIdValue) {
    userId.value = userIdValue
    localStorage.setItem("userId", userIdValue)
  }

  function isTokenValid() {
    if (!accessToken.value) return false

    try {
      const tokenParts = accessToken.value.split(".")
      if (tokenParts.length !== 3) return false

      const payload = JSON.parse(atob(tokenParts[1])) // JWT payload 디코딩
      const exp = payload.exp // 만료 시간 (초 단위)

      if (!exp) return false

      const currentTime = Math.floor(Date.now() / 1000) // 현재 시간 (초 단위)
      return exp > currentTime // 만료되지 않았다면 true, 만료되었으면 false
    } catch (error) {
      return false
    }
  }

  // 로그인 API 호출
  const logIn = async (id, password) => {
    try {
      const response = await axios.post(`${BASE_URL}/auth/login`, { id, password })
      accessToken.value = response.data["accessToken"]
      userId.value = id
      localStorage.setItem("authToken", response.data["accessToken"])
      localStorage.setItem("userId", id)
      return response.data["statusCode"]
    } catch (error) {
      console.error(`로그인 요청 중 에러 발생: `, error)
      throw error.response?.status || 500
    }
  }

  // 로그아웃 요청을 위한 API 호출 함수
  const logOut = async () => {
    try {
      const token = accessToken.value
      if (!token) {
        console.error("로그아웃 실패: 토큰이 없습니다.")
        return false
      }

      const headers = {
        Authorization: `${token}`,
        "Content-Type": "application/json",
      }

      // 로그아웃 API 호출
      const response = await axios.post(`${BASE_URL}/auth/logout`, {}, { headers })

      if (response.data.statusCode === 200) {
        console.log("로그아웃 성공")
        clearSession() // 세션 초기화
        router.push({ name: "Start" }) // 시작 페이지로 이동
      } else {
        console.error("로그아웃 실패: 서버에서 응답 실패")
      }
    } catch (error) {
      console.error("로그아웃 요청 중 에러 발생: ", error)
      if (error.response) {
        console.error("에러 응답:", error.response.data)
      }
    }
  }

  const signUp = async (userId, password, userName, phoneNumber, userEmail) => {
    try {
      const response = await axios.post(`${BASE_URL}/users`, { userId, password, userName, phoneNumber, userEmail })
      console.log("회원가입 1단계 API 호출 결과:", response)
      return response.data
    } catch (error) {
      console.error(error)
      throw new Error("회원가입 실패")
    }
  }

  // 이메일 중복확인 API
  const checkEmailDuplicateAPI = async (email) => {
    try {
      const response = await axios.get(`${BASE_URL}/users/check-email`, {
        params: { email },
      })
      if (response.data.statusCode === 200) {
        console.log("이메일 중복 확인 결과:", response)
        return true
      }
    } catch (error) {
      alert("이메일 중복 확인 실패")
    }
  }

  // 이메일 인증코드 발송 API
  const sendEmail = async (email) => {
    try {
      // `email`을 쿼리스트링으로 전달
      const response = await axios.post(`${BASE_URL}/users/send-email?email=${encodeURIComponent(email)}`)
      console.log("인증메일 발송 결과:", response)
      return response.data?.statusCode === 200
    } catch (error) {
      console.error("인증메일 발송 실패:", error)
      alert("인증메일 발송 실패")
      return false // 실패 시 false 반환
    }
  }

  // 이메일 인증확인 API
  const verifyEmail = async (code, email) => {
    try {
      // `code`와 `email`을 쿼리스트링으로 전달
      const response = await axios.post(`${BASE_URL}/users/verify-email?code=${code}&email=${encodeURIComponent(email)}`)
      console.log("이메일 인증 결과:", response)
      return response.data?.statusCode === 200
    } catch (error) {
      console.error("이메일 인증 실패:", error)
      alert("이메일 인증에 실패했습니다.")
      return false // 실패 시 false 반환
    }
  }

  // 캐릭터 등록 API 호출
  const registerCharacter = async (gender, userNickname, avatar, sessionId) => {
    try {
      const response = await axios.post(`${BASE_URL}/users/character?sessionId=${encodeURIComponent(sessionId)}`, { gender, userNickname, avatar })
      console.log("캐릭터 등록 결과:", response)
      return response.data?.statusCode === 200
    } catch (error) {
      console.error("캐릭터 등록 실패:", error)
      throw new Error("캐릭터 등록 실패")
    }
  }

  return {
    accessToken,
    sessionId,
    userId,
    setAccessToken,
    setSessionId,
    clearSession,
    isTokenValid,
    setUserId,
    logIn,
    logOut,
    signUp,
    checkEmailDuplicateAPI,
    sendEmail,
    verifyEmail,
    registerCharacter,
  }
})
