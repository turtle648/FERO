<template>
  <BaseModal title="Records" @close-modal="$emit('close-modal')">
    <!-- 전적 리스트 컨테이너 -->
    <div class="h-full overflow-y-auto" ref="matchDataContainer" @touchstart="handleTouchStart" @touchmove="handleTouchMove" @touchend="handleTouchEnd">
      <!-- 전적 아이템 -->
      <div v-for="(match, index) in matchData" :key="index" class="flex justify-between items-center p-3 border-b border-gray-200 hover:bg-gray-50">
        <!-- 왼쪽: 매치 정보 -->
        <div class="flex-1 text-left">
          <div class="text-sm text-gray-600">{{ formatDate(match.createdAt) }} | {{ match.gameId }}</div>
          <div class="font-medium">
            <span :class="match.result === 'WIN' ? 'text-blue-600' : match.result === 'LOSE' ? 'text-red-600' : 'text-gray-600'">
              {{ match.result === "WIN" ? "승리" : match.result === "LOSE" ? "패배" : "무승부" }}
            </span>
            <span class="ml-2">{{ match.userScore }}점</span>
          </div>
        </div>

        <!-- 오른쪽: 참가자 정보 -->
        <div class="flex flex-col items-end">
          <span class="text-xs text-gray-500">상대 점수: {{ match.opponentScore }}</span>
          <button class="mt-1 px-2 py-1 text-xs bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors">참여자 확인</button>
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import { ref, onMounted } from "vue"
import BaseModal from "./BaseModal.vue"
import { useUserStore } from "@/stores/store"
import axios from "axios"

const userStore = useUserStore()

const matchData = ref([])

defineEmits(["close-modal"])

// 매치 시간 계산 함수
// const computeMatchTime = (date, time) => {
//   const matchDateString = `${date}T${time}`
//   const matchDate = new Date(matchDateString)
//   const today = new Date()
//   const timeDiff = today - matchDate
//   const diffMinutes = Math.floor(timeDiff / (1000 * 60))
//   const diffHours = Math.floor(timeDiff / (1000 * 60 * 60))
//   const diffDays = Math.floor(timeDiff / (1000 * 60 * 60 * 24))
//   const diffMonths = today.getMonth() - matchDate.getMonth() + (today.getFullYear() - matchDate.getFullYear()) * 12
//   const diffYears = today.getFullYear() - matchDate.getFullYear()

//   if (diffMinutes < 60) return `${diffMinutes}분 전`
//   if (diffHours < 24) return `${diffHours}시간 전`
//   if (diffDays < 7) return `${diffDays}일 전`
//   if (diffDays < 10) return `1주 전`
//   if (diffDays < 17) return `2주 전`
//   if (diffDays < 24) return `3주 전`
//   if (diffDays < 28) return `4주 전`
//   if (diffDays < 30) return `1개월 전`
//   if (diffMonths < 12) return `${diffMonths}개월 전`
//   return `${diffYears}년 전`
// }

// 날짜 포맷팅 함수
const formatDate = (dateString) => {
  const options = { year: "numeric", month: "long", day: "numeric", hour: "2-digit", minute: "2-digit" }
  return new Date(dateString).toLocaleDateString("ko-KR", options)
}

// 게임 결과 가져오기
const fetchGameResults = async () => {
  try {
    console.log("게임 결과 요청 시작")

    const token = userStore.accessToken

    // API 호출
    const response = await axios.get("https://i12e103.p.ssafy.io:8076/api/gameResults", {
      headers: {
        Authorization: `Bearer ${token}`, // 헤더에 토큰 추가
        "Content-Type": "application/json", // JSON 형식 명시
      },
    })

    if (response.status === 200) {
      console.log("게임 결과 가져오기 성공:", response.data)

      // 응답 데이터를 matchData에 저장
      matchData.value = response.data
    } else {
      console.error("게임 결과 가져오기 실패:", response.status, response.data)
    }
  } catch (error) {
    console.error("게임 결과 가져오는 중 오류 발생:", error.response?.data || error.message)
  }
}

// 데이터 로드
onMounted(() => {
  fetchGameResults()
})
</script>
