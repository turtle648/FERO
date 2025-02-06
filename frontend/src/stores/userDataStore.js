// 레벨/경험치 조회 API
import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { useUserStore } from "@/stores/store"


export const useUserDataStore = defineStore('userData', () => {
  const BASE_URL = 'https://i12e103.p.ssafy.io:8076/api/v1'
  const userStore = useUserStore()

  const userLevel = ref('1')
  const userExperience = ref('30')

  // 본인정보 확인 API 호출
  const checkUserInfo = async () => {
    try {
      const token = userStore.accessToken
      if (!token) { console.error('정보조회 실패: 토큰이 없습니다.'); return}

      const headers = {
        'Authorization': token,  // Bearer 붙이지 않음 (확인 필요)
        'Content-Type': 'application/json'
      }

      const response = await axios.get('https://i12e103.p.ssafy.io:8076/api/v1/auth/me', { headers })

      if (response.status === 200) { console.log('조회 성공', response.data) } 
      else { console.error('조회 실패: 서버에서 응답 실패') }
    } catch (error) {
      console.error('조회 요청 중 에러 발생: ', error)
      if (error.response) { console.error('에러 응답:', error.response.data) }
    }
  }
  // 유저 레벨 조회
  const checkUserLevel = async () => {
    try {
        const token = userStore.accessToken
        if (!token) { console.error('정보조회 실패: 토큰이 없습니다.'); return}
  
        const headers = {
          'Authorization': token,  // Bearer 붙이지 않음 (확인 필요)
          'Content-Type': 'application/json'
        }
  
        const response = await axios.get(`${BASE_URL}/UserStats/level`, { headers })
  
        if (response.status === 200) { 
            console.log('조회 성공', response.data) 
            userLevel.value = response.data
        } 
        else { console.error('레벨 실패: 서버에서 응답 실패') }
      } catch (error) {
        console.error('레벨 조회 요청 중 에러 발생: ', error)
        if (error.response) { console.error('에러 응답:', error.response.data) }
      }    
  }

  // 유저 경험치 조회
  const checkUserExperience = async () => {
    try {
        const token = userStore.accessToken
        if (!token) { console.error('정보조회 실패: 토큰이 없습니다.'); return}
  
        const headers = {
          'Authorization': token,  // Bearer 붙이지 않음 (확인 필요)
          'Content-Type': 'application/json'
        }
  
        const response = await axios.get(`${BASE_URL}/UserStats/exp`, { headers })
  
        if (response.status === 200) { 
            console.log('조회 성공', response.data) 
            userExperience.value = response.data
        } 
        else { console.error('레벨 실패: 서버에서 응답 실패') }
      } catch (error) {
        console.error('레벨 조회 요청 중 에러 발생: ', error)
        if (error.response) { console.error('에러 응답:', error.response.data) }
      }    
  }

  return { 
    userLevel, userExperience,
    checkUserInfo, checkUserLevel, checkUserExperience,
         }
})
