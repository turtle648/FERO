<template>
  <div class="calendar-modal">
    <div class="content">
      <button id="close-btn" @click="closeCalendarModal">X</button>
      <div class="calendar">
        <header class="calendar-header">
          <button id="prev-month" @click="prevMonth">❮</button>
          <h2>{{ currentYear }}년 {{ currentMonth + 1 }}월</h2>
          <button id="next-month" @click="nextMonth">❯</button>
        </header>
        <div class="calendar-body">
          <div class="weekdays">
            <span>일</span>
            <span>월</span>
            <span>화</span>
            <span>수</span>
            <span>목</span>
            <span>금</span>
            <span>토</span>
          </div>
          <div class="days"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineEmits, onMounted } from "vue"
// import { useMainStore } from "@/stores/mainStore"

// const mainStore = useMainStore()

const emit = defineEmits(["close-modal"])
const closeCalendarModal = () => {
  emit("close-modal")
}

// 날짜 관련 상태 관리
const currentDate = new Date()
const currentYear = ref(currentDate.getFullYear())
const currentMonth = ref(currentDate.getMonth())
const daysContainer = ref(null) // 달력의 날짜를 렌더링할 요소

// 월별 날짜 렌더링 함수
const renderCalendar = () => {
  const firstDayOfMonth = new Date(currentYear.value, currentMonth.value, 1).getDay() // 해당 월의 첫 번째 날의 요일
  const lastDateOfMonth = new Date(currentYear.value, currentMonth.value + 1, 0).getDate() // 해당 월의 마지막 날짜
  const lastDayOfPrevMonth = new Date(currentYear.value, currentMonth.value, 0).getDate() // 이전 월의 마지막 날짜

  let daysHTML = ""

  // 이전 달 날짜
  for (let i = firstDayOfMonth; i > 0; i--) {
    daysHTML += `<span class="inactive">${lastDayOfPrevMonth - i + 1}</span>`
  }

  // 현재 달 날짜
  for (let i = 1; i <= lastDateOfMonth; i++) {
    const isToday = i === currentDate.getDate() && currentMonth.value === currentDate.getMonth() && currentYear.value === currentDate.getFullYear() ? "active" : ""
    daysHTML += `<span class="${isToday}">${i}</span>`
  }

  // 다음 달 날짜
  const nextDays = 42 - (firstDayOfMonth + lastDateOfMonth) // 총 셀 개수를 맞추기 위해 필요한 다음 달의 날짜 수 계산
  for (let i = 1; i <= nextDays; i++) {
    daysHTML += `<span class="inactive">${i}</span>`
  }

  if (daysContainer.value) {
    daysContainer.value.innerHTML = daysHTML // 계산된 HTML을 DOM에 삽입
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
  renderCalendar() // 변경된 월에 맞게 달력 다시 렌더링

  // try {
  //   const response = await mainStore.isQuestCompleted(currentYear.value, currentMonth.value + 1) // 해당 월의 퀘스트 데이터 가져오기
  //   console.log("✅ Prev Month Quest Data:", response)
  // } catch (error) {
  //   console.error("🚨 Error fetching quest data for prev month:", error)
  // }
}

const nextMonth = async () => {
  if (currentMonth.value === 11) {
    currentMonth.value = 0 // 현재 월이 12월이면 다음 달은 내년의 1월
    currentYear.value++
  } else {
    currentMonth.value++
  }
  renderCalendar()

  // try {
  //   const response = await mainStore.isQuestCompleted(currentYear.value, currentMonth.value + 1)
  //   console.log(response)
  // } catch (error) {
  //   console.error(error)
  // }
}

// 초기 렌더링
onMounted(() => {
  daysContainer.value = document.querySelector(".days")
  renderCalendar() // 초기 달력 렌더링 실행
})
</script>

<style scoped>
.calendar-modal {
  width: 40vh;
  height: 55vh;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: antiquewhite;
  display: flex;
  flex-direction: column;
}

.content {
  text-align: center;
  display: flex;
  flex-direction: column;
  height: 100%;
}

.calendar {
  width: 100%; /* 부모 요소와 동일한 너비 */
  height: 100%; /* 부모 요소와 동일한 높이 */
  display: flex;
  flex-direction: column;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
}

.calendar-body {
  display: flex;
  flex-direction: column;
  height: calc(100% - 50px); /* 헤더 높이를 제외한 나머지 공간 */
}

.weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  text-align: center;
  font-weight: bold;
}

.days {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 2px; /* 날짜 간격 */
}
.days span {
  display: flex;
  justify-content: center;
  align-items: center;
}

.days .inactive {
  color: white !important; /* 이전, 다음 달 날짜를 흰색으로 설정했는데 왜 안됨? */
}

#close-btn {
  position: absolute;
  top: 1%;
  right: 1%;
}
</style>
