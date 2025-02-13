<template>
  <div class="fixed inset-0 flex items-center justify-center bg-white h-[55vh] w-[40vh]">
    <div class="flex flex-col bg-white relative">
      <button id="close-btn" @click="closeCalendarModal" class="absolute top-2 right-2 text-lg">X</button>
      <div class="flex flex-col w-full h-full">
        <header class="flex justify-between items-center p-4">
          <button id="prev-month" @click="prevMonth" class="text-lg">❮</button>
          <h2>{{ currentYear }}년 {{ currentMonth + 1 }}월</h2>
          <button id="next-month" @click="nextMonth" class="text-lg">❯</button>
        </header>
        <div class="flex flex-col h-full">
          <div class="grid grid-cols-7 text-center font-bold">
            <span>일</span>
            <span>월</span>
            <span>화</span>
            <span>수</span>
            <span>목</span>
            <span>금</span>
            <span>토</span>
          </div>
          <div class="grid grid-cols-7 gap-0.5 days"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits, onMounted } from "vue"
import { useMainStore } from "@/stores/mainStore"

const mainStore = useMainStore()

const emit = defineEmits(["close-modal"])
const closeCalendarModal = () => {
  emit("close-modal")
}

// 날짜 관련 상태 관리
const currentDate = new Date()
const currentYear = ref(currentDate.getFullYear())
const currentMonth = ref(currentDate.getMonth())
const daysContainer = ref(null) // 달력의 날짜를 렌더링할 요소

// 퀘스트 데이터를 저장
const questData = ref([])

// 데이터 가져오기 함수
const fetchQuestData = async () => {
  try {
    const response = await mainStore.isQuestCompleted(currentYear.value, currentMonth.value + 1)

    if (response && response.length > 0) {
      // 데이터가 존재하면 저장
      questData.value = response
    } else {
      // 데이터가 없으면 빈 배열로 초기화
      questData.value = []
    }
  } catch (error) {
    if (error.response && error.response.status === 204) {
      console.log("🚨 No data for this month")
      questData.value = "no-data" // 데이터 없음 표시
    } else {
      console.error("🚨 Error fetching quest data:", error)
    }
  } finally {
    renderCalendar() // 데이터를 기반으로 달력을 다시 렌더링
  }
}

// 월별 날짜 렌더링 함수
const renderCalendar = () => {
  const firstDayOfMonth = new Date(currentYear.value, currentMonth.value, 1).getDay() // 해당 월의 첫 번째 날의 요일
  const lastDateOfMonth = new Date(currentYear.value, currentMonth.value + 1, 0).getDate() // 해당 월의 마지막 날짜
  const lastDayOfPrevMonth = new Date(currentYear.value, currentMonth.value, 0).getDate() // 이전 월의 마지막 날짜

  let daysHTML = ""

  // 이전 달 날짜
  for (let i = firstDayOfMonth; i > 0; i--) {
    daysHTML += `<span class="text-gray-400">${lastDayOfPrevMonth - i + 1}</span>`
  }

  // 현재 달 날짜
  for (let i = 1; i <= lastDateOfMonth; i++) {
    const isToday = i === currentDate.getDate() && currentMonth.value === currentDate.getMonth() && currentYear.value === currentDate.getFullYear() ? "bg-blue-200 font-bold" : ""

    const questStatus = questData.value !== "no-data" && questData.value.find((q) => q.day === i)
    const questClass = questStatus ? (questStatus.isCompleted ? "text-green-500" : "text-red-500") : ""

    daysHTML += `<span class="${isToday} ${questClass}">${i}</span>`
  }

  // 다음 달 날짜
  const nextDays = 42 - (firstDayOfMonth + lastDateOfMonth) // 총 셀 개수를 맞추기 위해 필요한 다음 달의 날짜 수 계산
  for (let i = 1; i <= nextDays; i++) {
    daysHTML += `<span class="text-gray-400">${i}</span>`
  }

  if (daysContainer.value) {
    daysContainer.value.innerHTML = daysHTML

    if (questData.value === "no-data") {
      daysContainer.value.classList.add("bg-red-100")
    } else {
      daysContainer.value.classList.remove("bg-red-100")
    }
  }
}

// 이전/다음 달 이동 함수
const prevMonth = async () => {
  if (currentMonth.value === 0) {
    currentMonth.value = 11 // 현재 월이 1월이면 이전 달은 작년의 12월
    currentYear.value--
  } else {
    currentMonth.value-- // 이전 달로 이동
  }

  await fetchQuestData()
}

const nextMonth = async () => {
  if (currentMonth.value === 11) {
    currentMonth.value = 0 // 현재 월이 12월이면 다음 달은 내년의 1월
    currentYear.value++
  } else {
    currentMonth.value++
  }

  await fetchQuestData()
}

// 초기 렌더링
onMounted(async () => {
  daysContainer.value = document.querySelector(".days")

  await fetchQuestData()
})
</script>
<style scoped></style>
