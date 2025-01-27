// userAPI.js

import axios from 'axios';
const BASE_URL = '/api/v1'; // API 기본 URL 설정


// 임시
// User Status 받아오는 API 호출 (토큰 인증 필요)
export const getStatus = async (userId, token) => {
    try {
      // 사용자 정보 요청 보내기 (Authorization 헤더에 토큰 포함)
      // 임의로 만든 url임
      const response = await axios.get(`${BASE_URL}/user/status/${userId}`, {
        headers: {
          Authorization: `Bearer ${token}`,  // 토큰을 Authorization 헤더에 포함
        },
      });
      localStorage.setItem('nickName', response.data.nickName)
      localStorage.setItem('rank', response.data.rank)
      localStorage.setItem('level', response.data.level)
      return
    } catch (error) {
      console.error(`사용자 정보 요청 중 에러 발생: ${userId}`, error);
    //   throw error;  // 에러를 다시 던져서 호출한 쪽에서 처리할 수 있도록
    }
  };
