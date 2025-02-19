<template>
  <BaseModal title="Records" @close-modal="$emit('close-modal')">
    <!-- 전적 리스트 컨테이너 -->
    <div class="h-full w-full overflow-y-auto" ref="matchDataContainer" @touchstart="handleTouchStart" @touchmove="handleTouchMove" @touchend="handleTouchEnd">
      <!-- 전적 아이템 -->
      <div v-for="(match, index) in matchData" :key="index" class="w-full flex flex-col border-b border-gray-200 hover:bg-gray-50 p-4">
        <!-- 매치 날짜 -->
        <p class="text-base w-full text-gray-500 mb-2">
          {{ formatDate(match.createdAt) }}
        </p>

        <!-- 매치 정보 -->
        <div class="info-container flex w-full items-center">
          <div class="result text-3xl w-[35%] text-white text-center p-2" :class="match.result === 'WIN' ? 'bg-green-500' : match.result === 'LOSE' ? 'bg-red-500' : 'bg-gray-500'">
            {{ match.result }}
          </div>

          <div class="result-info flex-1 ml-4 flex flex-col items-center">
            <p class="text-4xl flex items-center justify-center gap-2">
              <span class="user">
                {{ match.userScore }}
                <br />
                <span class="text-lg">me</span>
              </span>
              <span class="center">
                :
                <br />
                <span class="text-base">:</span>
              </span>
              <span class="opponent">
                {{ match.opponentScore }}
                <br />
                <span class="text-lg">{{ match.opponentId }}</span>
              </span>
            </p>
            <!-- <p class="text-lg flex items-center justify-center gap-2">
              <span class="user">ME</span>
              <span class="center">:</span>
              <span class="opponent">{{ match.opponentId }}</span>
            </p> -->
          </div>
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

// 날짜 포맷팅 함수
const formatDate = (dateString) => {
  const date = new Date(dateString)

  // 날짜 부분 포맷
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, "0") // 1월이 0부터 시작하므로 +1
  const day = String(date.getDate()).padStart(2, "0")

  // 시간 부분 포맷
  let hours = date.getHours()
  const minutes = String(date.getMinutes()).padStart(2, "0")
  const period = hours >= 12 ? "PM" : "AM"

  // 12시간제로 변환
  hours = hours % 12 || 12

  return `${year}.${month}.${day} ${period} ${String(hours).padStart(2, "0")}:${minutes}`
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
