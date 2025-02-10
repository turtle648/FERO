// 레벨/경험치 조회 API
import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { useUserStore } from "@/stores/store"


export const useUserDataStore = defineStore('userData', () => {
  const BASE_URL = 'https://i12e103.p.ssafy.io:8076/api/v1'
  const userStore = useUserStore()

  const userNickname = ref('')
  const userLevel = ref('1')
  const userExperience = ref('30')
  const userCharacter = ref([1, 1, 1])
  
  // 본인정보 확인 API 호출
  const checkUserNickname = async () => {
    try {
      const token = userStore.accessToken
      if (!token) { console.error('정보조회 실패: 토큰이 없습니다.'); return}

      const headers = {
        'Authorization': token,  // Bearer 붙이지 않음 (확인 필요)
        'Content-Type': 'application/json'
      }
      
      const response = await axios.get(`${BASE_URL}/auth/me`, { headers })

      if (response.status === 200) { 
        console.log('닉네임 조회 성공', response.data) 
        userNickname.value = response.data['userNickname']
        return response.data['userNickname']
      } 
      else { console.error('닉네임 조회 실패: 서버에서 응답 실패'); return 'Error' }
    } catch (error) {
      console.error('닉네임 조회 요청 중 에러 발생: ', error)
      if (error.response) { console.error('에러 응답:', error.response.data) }
      return 'Error'
    }
  }
  // 유저 레벨 조회
  const checkUserLevel = async () => {
    try {
        const token = userStore.accessToken
        if (!token) { console.error('정보조회 실패: 토큰이 없습니다.'); return }
  
        const headers = {
          'Authorization': token,  // Bearer 붙이지 않음 (확인 필요)
          'Content-Type': 'application/json'
        }
  
        const response = await axios.get(`${BASE_URL}/userStats/level`, { headers })
  
        if (response.status === 200) { 
            console.log('조회 성공', response.data) 
            userLevel.value = response.data
            return response.data
        } 
        else { console.error('레벨 실패: 서버에서 응답 실패'); return 9999}
      } catch (error) {
        console.error('레벨 조회 요청 중 에러 발생: ', error)
        if (error.response) { console.error('에러 응답:', error.response.data) }
        return 9999
      }    
  }

  // 유저 경험치 조회
  const checkUserExperience = async () => {
    try {
        const token = userStore.accessToken
        if (!token) { console.error('경험치 조회 실패: 토큰이 없습니다.'); return 50}
  
        const headers = {
          'Authorization': token,  // Bearer 붙이지 않음 (확인 필요)
          'Content-Type': 'application/json'
        }
  
        const response = await axios.get(`${BASE_URL}/userStats/exp`, { headers })
  
        if (response.status === 200) { 
            console.log('경험치 조회 성공', response.data) 
            userExperience.value = response.data
            return response.data
        } 
        else { console.error('경험치 조회 실패: 서버에서 응답 실패'); return 0}
      } catch (error) {
        console.error('경험치 조회 요청 중 에러 발생: ', error)
        if (error.response) { console.error('에러 응답:', error.response.data)}
        return 0
      }    
  }

  return { 
    userNickname, userLevel, userExperience, userCharacter,
    checkUserNickname, checkUserLevel, checkUserExperience,
         }
})
