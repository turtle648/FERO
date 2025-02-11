import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  const accessToken = ref(localStorage.getItem('authToken') || null)
  const sessionId = ref(localStorage.getItem('sessionId') || null)
  const userId = ref(localStorage.getItem('userId') || null)

  function setAccessToken(token) {
    accessToken.value = token
    localStorage.setItem('authToken', token)
  }

  function setSessionId(sessionIdValue) {
    sessionId.value = sessionIdValue
    localStorage.setItem('sessionId', sessionIdValue)
  }

  function clearSession() {
    accessToken.value = null
    sessionId.value = null
    localStorage.removeItem('authToken')
    localStorage.removeItem('sessionId')
  }

  function setUserId(userIdValue) {
    userId.value = userIdValue
    localStorage.setItem('userId', userIdValue)
  }

  function isTokenValid() {
    if (!accessToken.value) return false

    try {
      const tokenParts = accessToken.value.split('.')
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

  // 로그아웃 요청을 위한 API 호출 함수
  const logOut = async () => {
    try {
      const token = accessToken.value;
      if (!token) {
        console.error('로그아웃 실패: 토큰이 없습니다.')
        return
      }

      const headers = {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
      }

      // 로그아웃 API 호출
      const response = await axios.post('https://i12e103.p.ssafy.io:8076/api/v1/auth/logout', {}, { headers })

      if (response.data.statusCode === 200) {
        console.log('로그아웃 성공')
        clearSession()  // 세션 초기화
        router.push({ name: 'Start' })  // 시작 페이지로 이동
      } else {
        console.error('로그아웃 실패: 서버에서 응답 실패')
      }
    } catch (error) {
      console.error('로그아웃 요청 중 에러 발생: ', error)
      if (error.response) {
        console.error('에러 응답:', error.response.data)
      }
    }
  }

  return { accessToken, sessionId, userId,
           setAccessToken, setSessionId, clearSession, isTokenValid, setUserId, 
           logOut, 
         }
})
