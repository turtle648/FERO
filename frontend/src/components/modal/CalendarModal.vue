<!-- components/modal/CalendarModal.vue -->
<template>
  <BaseModal @close-modal="$emit('close-modal')">
    <div class="flex flex-col w-full h-full max-w-[40vh] bg-white">
      <!-- 달력 헤더 -->
      <header class="flex justify-between items-center p-4 border-b">
        <button 
          @click="prevMonth"
          class="w-8 h-8 flex items-center justify-center text-gray-600 hover:bg-gray-100 rounded-full transition-colors"
        >
          ❮
        </button>
        <h2 class="text-lg font-bold">
          {{ currentYear }}년 {{ currentMonth + 1 }}월
        </h2>
        <button 
          @click="nextMonth"
          class="w-8 h-8 flex items-center justify-center text-gray-600 hover:bg-gray-100 rounded-full transition-colors"
        >
          ❯
        </button>
      </header>

      <!-- 달력 본문 -->
      <div class="flex-1 p-4">
        <!-- 요일 헤더 -->
        <div class="grid grid-cols-7 mb-2">
          <span 
            v-for="day in ['일', '월', '화', '수', '목', '금', '토']" 
            :key="day"
            class="text-center font-medium text-gray-600 text-sm py-2"
          >
            {{ day }}
          </span>
        </div>

        <!-- 날짜 그리드 -->
        <div 
          ref="daysContainer"
          class="grid grid-cols-7 gap-1 days"
        >
          <!-- 날짜들은 renderCalendar 함수에서 동적으로 추가됨 -->
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useMainStore } from "@/stores/mainStore"
import BaseModal from './BaseModal.vue'

const mainStore = useMainStore()

defineEmits(["close-modal"])

// 날짜 관련 상태
const currentDate = new Date()
const currentYear = ref(currentDate.getFullYear())
const currentMonth = ref(currentDate.getMonth())
const daysContainer = ref(null)
const questData = ref([])

// 퀘스트 데이터 가져오기
const fetchQuestData = async () => {
  try {
    const response = await mainStore.isQuestCompleted(
      currentYear.value, 
      currentMonth.value + 1
    )
    questData.value = response?.length > 0 ? response : "no-data"
  } catch (error) {
    console.error("🚨 Error fetching quest data:", error)
    questData.value = "no-data"
  } finally {
    renderCalendar()
  }
}

// 달력 렌더링
const renderCalendar = () => {
  const firstDay = new Date(currentYear.value, currentMonth.value, 1).getDay()
  const lastDate = new Date(currentYear.value, currentMonth.value + 1, 0).getDate()
  const lastDatePrev = new Date(currentYear.value, currentMonth.value, 0).getDate()

  let daysHTML = ""

  // 이전 달 날짜
  for (let i = firstDay; i > 0; i--) {
    daysHTML += `
      <span class="text-center py-2 text-gray-400 text-sm">
        ${lastDatePrev - i + 1}
      </span>
    `
  }

  // 현재 달 날짜
  for (let i = 1; i <= lastDate; i++) {
    const isToday = i === currentDate.getDate() && 
                    currentMonth.value === currentDate.getMonth() && 
                    currentYear.value === currentDate.getFullYear()
    
    const questStatus = questData.value !== "no-data" && 
                       questData.value.find(q => q.day === i)
    
    const classes = [
      'text-center py-2',
      isToday ? 'bg-blue-100 font-bold rounded' : '',
      questStatus?.isCompleted ? 'text-green-600' : '',
      !questStatus?.isCompleted && questStatus ? 'text-red-600' : ''
    ].filter(Boolean).join(' ')

    daysHTML += `<span class="${classes}">${i}</span>`
  }

  // 다음 달 날짜
  const remainingDays = 42 - (firstDay + lastDate)
  for (let i = 1; i <= remainingDays; i++) {
    daysHTML += `
      <span class="text-center py-2 text-gray-400 text-sm">
        ${i}
      </span>
    `
  }

  if (daysContainer.value) {
    daysContainer.value.innerHTML = daysHTML
  }
}

// 월 이동
const prevMonth = async () => {
  if (currentMonth.value === 0) {
    currentYear.value--
    currentMonth.value = 11
  } else {
    currentMonth.value--
  }
  await fetchQuestData()
}

const nextMonth = async () => {
  if (currentMonth.value === 11) {
    currentYear.value++
    currentMonth.value = 0
  } else {
    currentMonth.value++
  }
  await fetchQuestData()
}

onMounted(async () => {
  daysContainer.value = document.querySelector(".days")
  await fetchQuestData()
})
</script>
