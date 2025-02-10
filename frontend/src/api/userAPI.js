// userAPI.js

// import axios from 'axios';
// const BASE_URL = '/api/v1'; // API 기본 URL 설정


// 임시 User Status 받아오는 API 호출 (토큰 인증 필요)
export const getStatus = async () => {
  const userStatus = {
    // 기본정보
    'nickName': 'SSAFY_NO_1', 'level': '25', 'rank': '[Gold, 5]',
    'totalWinRate': '1397', 'totalGames': '2120', 'myBestGame': 'Plank',
    'monthlyWinRate': '84', 'monthlyGames': '120', 'myBestMonthlyGame': 'Squat',
    'questCompletes': '3114', 'monthlyQuestCompletes': '114', 'todayQuestProgress': '85', 
    // 육각으로 표시할 정보
    'stamina': 75, 'endurance': 80, 'cardio': 60, 
    'agility': 90, 'strength': 85, 'flexibility': 70,
  }

  // hasOwnProperty 호출 시 안전하게 하기 위해 Object.prototype 사용
  for (const key in userStatus) {
    if (Object.prototype.hasOwnProperty.call(userStatus, key)) {
      localStorage.setItem(key, userStatus[key])
    }
  }
}


// 임시 User 전적 받아오는 API
import matchData from '@/assets/dummy_data/getMatchData.json'
export const getMatchData = () => {
  console.log('전적 불러오기 성공 ==========')
  console.log(matchData)
  return matchData
}