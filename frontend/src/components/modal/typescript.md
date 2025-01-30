```typescript
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
          <!-- 요일 헤더 -->
          <div class="weekdays">
            <span>일</span>
            <span>월</span>
            <span>화</span>
            <span>수</span>
            <span>목</span>
            <span>금</span>
            <span>토</span>
          </div>
          <div ref="daysContainer" class="days"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
// Vue Composition API와 TypeScript 관련 기능 가져오기
import { defineEmits, onMounted, Ref } from "vue"   // Ref는 타입스크립트에서 사용하는 ref

// 객체나 배열은 reactive를 사용하는 것이 더 적합할 수 있음
// 특정 속성만 반응형으로 만들고 싶다면 toRef()와 함께 사용할 수 있음

const emit = defineEmits<{
  (e: "closeCalendar"): void // "closeCalendar" 이벤트 정의, "closeCalendar"라는 특정 문자열만 허용
}>()

// e: 함수의 제네릭(Generic) 타입 정의에서 사용된 이벤트 이름

// 캘린더 모달 닫기 함수
const closeCalendarModal = (): void => {
  emit("closeCalendar")
}

// void 함수의 반환값이 없음

// 현재 날짜와 관련된 상태 관리
const currentDate = new Date()
const currentYear = ref<number>(currentDate.getFullYear())
const currentMonth = ref<number>(currentDate.getMonth())
const daysContainer: Ref<HTMLDivElement | null> = ref(null)

// 캘린더를 렌더링하는 함수
const renderCalendar = (): void => {
  // 해당 월의 첫 번째 날의 요일 (0: 일요일 ~ 6: 토요일)
  const firstDayOfMonth = new Date(currentYear.value, currentMonth.value, 1).getDay()
  // 해당 월의 마지막 날짜
  const lastDateOfMonth = new Date(currentYear.value, currentMonth.value + 1, 0).getDate()
  // 이전 달의 마지막 날짜
  const lastDayOfPrevMonth = new Date(currentYear.value, currentMonth.value, 0).getDate()

  let daysHTML = ""

  for (let i = firstDayOfMonth; i > 0; i--) {
    daysHTML += `<span class="inactive">${lastDayOfPrevMonth - i + 1}</span>`
  }

  // 현재 달 날짜 추가
  for (let i = 1; i <= lastDateOfMonth; i++) {
    const isToday =
      i === currentDate.getDate() &&
      currentMonth.value === currentDate.getMonth() &&
      currentYear.value === currentDate.getFullYear()
        ? "active" // 오늘 날짜 강조 클래스 추가
        : ""
    daysHTML += `<span class="${isToday}">${i}</span>`
  }

  // 다음 달 날짜 추가 (비활성화 상태)
  const nextDays = 42 - (firstDayOfMonth + lastDateOfMonth)
  for (let i = 1; i <= nextDays; i++) {
    daysHTML += `<span class="inactive">${i}</span>`
  }

  // DOM 요소에 HTML 삽입
  if (daysContainer.value) {
    daysContainer.value.innerHTML = daysHTML
  }
}

// 이전 달로 이동하는 함수
const prevMonth = (): void => {
  if (currentMonth.value === 0) {
    currentMonth.value = 11 // 이전 해의 마지막 달(12월)
    currentYear.value-- // 연도 감소
  } else {
    currentMonth.value-- // 월 감소
  }
  renderCalendar() // 변경된 월로 캘린더 다시 렌더링
}

// 다음 달로 이동하는 함수
const nextMonth = (): void => {
  if (currentMonth.value === 11) {
    currentMonth.value = 0 // 다음 해의 첫 번째 달(1월)
    currentYear.value++ // 연도 증가
  } else {
    currentMonth.value++ // 월 증가
  }
  renderCalendar() // 변경된 월로 캘린더 다시 렌더링
}

// 컴포넌트가 마운트되었을 때 초기 설정 실행
onMounted((): void => {
  daysContainer.value = document.querySelector(".days") as HTMLDivElement // DOM 요소 참조 저장, as로 타입지정
  renderCalendar() // 초기 캘린더 렌더링 실행
})
</script>

<style scoped>
/* 모달 스타일 */
.calendar-modal {
  width: 40vh;
  height: 55vh;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: antiquewhite;
}

/* 콘텐츠 스타일 */
.content {
  text-align: center;
}

/* 캘린더 헤더 스타일 */
.calendar-header {
  display: flex;
}

/* 요일 및 날짜 스타일 */
.weekdays,
.days {
}
.days .inactive {
}
</style>

```
