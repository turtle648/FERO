import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const accessToken = ref(null)
  const sessionId = ref(null)
  const userId = ref(null)

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

  return { accessToken, sessionId, userId,
           setAccessToken, setSessionId, clearSession, isTokenValid, setUserId,
         }
})
