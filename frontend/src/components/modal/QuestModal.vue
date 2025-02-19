<!-- components/modal/QuestModal.vue -->
<template>
  <BaseModal title="Quest" @close-modal="$emit('close-modal')">
    <!-- 로딩 상태 -->
    <div v-if="isLoading" class="flex justify-center items-center h-full">
      <div class="text-gray-500">데이터 불러오는 중...</div>
    </div>

    <!-- 퀘스트 데이터 -->
    <div v-else class="space-y-4">
      <div class="bg-gray-50 rounded-lg p-4 shadow-sm">
        <div class="space-y-2">
          <!-- 전체를 감싸는 컨테이너에 flex와 중앙 정렬 적용 -->
          <div class="flex flex-col items-center w-full">
            <!-- Today's Fitness는 이미 중앙 정렬되어 있음 -->
            <h1 class="text-4xl font-bold mb-4 text-center">Today's Fitness<br>[Squart]</h1>
            
            <!-- 메시지도 이미 중앙 정렬되어 있음 -->
            <!-- <h1 
              ref="messageText" 
              class="text-2xl mb-4 text-center whitespace-nowrap" 
              :style="{ fontSize: fontSize + 'px' }">
              오늘은 20개 하세요 ~!
            </h1> -->

            <!-- Goal/Now 섹션을 위한 컨테이너 -->
            <div class="w-48"> <!-- 원하는 너비로 조정 가능 -->
              당신의 레벨에 맞는 갯수를 제안합니다.
              {{ questData.message }}
              <hr>
              <div class="grid grid-cols-[1fr_auto_1fr] gap-2">
                <p class="text-2xl font-bold mb-4 text-right">Goal</p>
                <p class="text-2xl font-bold mb-4 text-center">:</p>
                <p class="text-2xl font-bold mb-4 text-left">{{ questData.exerciseCnt }}</p>
                <p class="text-2xl font-bold mb-4 text-right">now</p>
                <p class="text-2xl font-bold mb-4 text-center">:</p>
                <p class="text-2xl font-bold mb-4 text-left">{{ questData.realCnt }}</p>
              </div>
            </div>
          </div>

          <!-- <p>{{ questData.exerciseCnt }}</p>
          <p>{{ questData }}</p> -->
          <!-- <p>{{ questData[0] }}</p>
          <p>{{ questData }}</p> -->
          
            <!-- "exerciseCnt": 0, : 목표 운동 횟수
            "exerciseId": 0, : 운동 종목 번호 / x
            "exerciseName": "string", : 운동 종목 이름 /x
            "isCompleted": true, : 달성 여부
            "message": "string", : 퀘스트 메세지 ()
            "questTime": { : 나노??? / 이건 안씀
              "hour": 0,
              "minute": 0,
              "nano": 0,
              "second": 0
            },
            "realCnt": 0 : 내가 한 횟수 -->
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue"
import axios from "axios"
import BaseModal from './BaseModal.vue'
// import { useMainStore } from "@/stores/mainStore"

const questData = ref([])
const isLoading = ref(false)

const messageText = ref(null)
const fontSize = ref(24) // 초기 폰트 사이즈 (text-2xl과 동일)

const adjustFontSize = () => {
  if (!messageText.value) return

  const container = messageText.value.parentElement
  const text = messageText.value

  // 폰트 사이즈를 초기값으로 리셋
  fontSize.value = 24

  // 텍스트가 컨테이너를 벗어나는 동안 폰트 사이즈를 줄임
  while (text.scrollWidth > container.clientWidth && fontSize.value > 8) {
    fontSize.value -= 0.5
  }
}

defineEmits(['close-modal'])

const getQuestData = async () => {
    isLoading.value = true
    try {
        // const today = new Date().toISOString().split('T')[0]
        const token = localStorage.getItem('authToken')
        console.log('token:', token) // 토큰이 제대로 저장되어 있는지 확인

        const response = await axios.get('https://i12e103.p.ssafy.io:8076/api/v1/exercise/today', {
            headers: {
                Authorization: `Bearer ${token}`
            },
            // params: {
            //     date: today
            // }
        })
        console.log('API Response:', response.data) // 리스폰스 값 확인
        questData.value = response.data[0]

    } catch (error) {
        console.error('퀘스트 데이터 조회 실패', error)
    } finally {
        isLoading.value = false
    }
}

onMounted(() => {
    getQuestData()
    adjustFontSize()
  // 창 크기 변경 시 폰트 사이즈 재조정
  window.addEventListener('resize', adjustFontSize)
})

onUnmounted(() => {
  window.removeEventListener('resize', adjustFontSize)
})
</script>
