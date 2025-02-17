<!-- components/modal/CalendarModal.vue -->
<template>
<<<<<<< HEAD
  <BaseModal title="Calendar" @close-modal="$emit('close-modal')">
    <div class="flex flex-col w-full h-full max-w-[40vh] bg-white">
      <!-- 달력 헤더 -->
      <header class="flex justify-between items-center p-4 border-b">
        <button @click="prevMonth" class="w-8 h-8 flex items-center justify-center text-gray-600 hover:bg-gray-100 rounded-full transition-colors">❮</button>
        <h2 class="text-lg font-bold">{{ currentYear }}년 {{ currentMonth + 1 }}월</h2>
        <button @click="nextMonth" class="w-8 h-8 flex items-center justify-center text-gray-600 hover:bg-gray-100 rounded-full transition-colors">❯</button>
      </header>

      <!-- 달력 본문 -->
      <div class="flex-1 p-4">
        <!-- 요일 헤더 -->
        <div class="grid grid-cols-7 mb-2">
          <span v-for="day in ['일', '월', '화', '수', '목', '금', '토']" :key="day" class="text-center font-medium text-gray-600 text-sm py-2">
            {{ day }}
          </span>
        </div>

        <!-- 날짜 그리드 -->
        <div ref="daysContainer" class="grid grid-cols-7 gap-1 days">
          <!-- 날짜들은 renderCalendar 함수에서 동적으로 추가됨 -->
=======
  <div
    class="fixed inset-0 flex items-center justify-center bg-gray-500 bg-opacity-70"
    @click.self="closeCalendarModal"
  >
    <div
      class="flex flex-col bg-white relative w-[40vh] h-[70vh] max-w-[90vw] max-h-[90vh] pt-5 px-3 shadow-lg rounded-lg"
      @click.stop
    >
      <button
        id="close-btn"
        @click="closeCalendarModal"
        class="absolute top-2 right-2 text-lg"
      >
        X
      </button>
      <div class="flex flex-col w-full max-h-[50%]">
        <header class="flex justify-between items-center p-2">
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
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
        </div>
      </div>
      <div class="flex flex-col w-full h-[50%]">
        <Bar :data="chartData" :options="options" />
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
<<<<<<< HEAD
import BaseModal from "./BaseModal.vue"
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from "chart.js"
import { ref, defineEmits, onMounted, computed } from "vue"
import { useMainStore } from "@/stores/mainStore"
import { Bar } from "vue-chartjs"
import * as chartConfig from "../config/chartConfig.js"

const mainStore = useMainStore()
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

// defineEmits(["close-modal"])

// 날짜 관련 상태
// const currentDate = new Date()
// const currentYear = ref(currentDate.getFullYear())
// const currentMonth = ref(currentDate.getMonth())
// const daysContainer = ref(null)
// const questData = ref([])
// 선택된 날짜 상태 관리
const selectedDate = ref(new Date())
=======
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from "chart.js";
import { ref, defineEmits, onMounted, computed } from "vue";
import { useMainStore } from "@/stores/mainStore";
import { Bar } from "vue-chartjs";
import * as chartConfig from "../config/chartConfig.js";

const mainStore = useMainStore();
ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

// 선택된 날짜 상태 관리
const selectedDate = ref(new Date());
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

// 차트 데이터 computed 속성으로 변경
const chartData = computed(() => ({
  labels: ["팔", "다리", "가슴", "복근", "등", "체력"],
  datasets: [
    {
      label: "운동 데이터",
      backgroundColor: "rgba(236, 72, 153, 1)",
      pointBackgroundColor: "rgba(236, 72, 153, 1)",
      pointBorderColor: "#fff",
      pointHoverBackgroundColor: "#fff",
      pointHoverBorderColor: "rgba(236, 72, 153, 1)",
      data: getSelectedDateData(),
    },
  ],
<<<<<<< HEAD
}))

const options = ref(chartConfig.options)

defineEmits(["close-modal"])
// const closeCalendarModal = () => {
//   emit("close-modal")
// }

// 날짜 관련 상태 관리
const currentDate = new Date()
const currentYear = ref(currentDate.getFullYear())
const currentMonth = ref(currentDate.getMonth())
const daysContainer = ref(null)

// 선택된 날짜의 데이터 가져오기
const getSelectedDateData = async () => {
  const year = selectedDate.value.getFullYear()
  const month = selectedDate.value.getMonth()
  const date = selectedDate.value.getDate()
  console.log("📅" + year + "년 " + month + "월 " + date + "일")

  const response = await mainStore.getMonthStatus(year, month)
  console.log(response)

  const dateData = response.find((value) => {
    return value.date === date
  })

  console.log(dateData)

  return dateData ? dateData.status : [0, 0, 0, 0, 0, 0]
}
=======
}));

const options = ref(chartConfig.options);

const emit = defineEmits(["close-modal"]);
const closeCalendarModal = () => {
  emit("close-modal");
};

// 날짜 관련 상태 관리
const currentDate = new Date();
const currentYear = ref(currentDate.getFullYear());
const currentMonth = ref(currentDate.getMonth());
const daysContainer = ref(null);

// 선택된 날짜의 데이터 가져오기
const getSelectedDateData = async () => {
  const year = selectedDate.value.getFullYear();
  const month = selectedDate.value.getMonth();
  const date = selectedDate.value.getDate();
  console.log("📅" + year + "년 " + month + "월 " + date + "일");

  const response = await mainStore.getMonthStatus(year, month);
  console.log(response);

  const dateData = response.find((value) => {
    return value.date === date;
  });

  console.log(dateData);

  return dateData ? dateData.status : [0, 0, 0, 0, 0, 0];
};
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

// 퀘스트 데이터를 저장
const questData = ref([]);

// 퀘스트 데이터 가져오기
const fetchQuestData = async () => {
  try {
    const response = await mainStore.isQuestCompleted(
      currentYear.value,
      currentMonth.value + 1
    );

    if (response && response.length > 0) {
<<<<<<< HEAD
      questData.value = response
    } else {
      questData.value = []
    }
  } catch (error) {
    if (error.response && error.response.status === 204) {
      console.log("🚨 No data for this month")
      questData.value = "no-data"
=======
      questData.value = response;
    } else {
      questData.value = [];
    }
  } catch (error) {
    if (error.response && error.response.status === 204) {
      console.log("🚨 No data for this month");
      questData.value = "no-data";
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
    } else {
      console.error("🚨 Error fetching quest data:", error);
    }
  } finally {
<<<<<<< HEAD
    renderCalendar()
=======
    renderCalendar();
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
  }
};

// 날짜 선택 핸들러
const handleDateSelect = (day) => {
  selectedDate.value = new Date(currentYear.value, currentMonth.value, day);
};

// 날짜 선택 핸들러
const handleDateSelect = (day) => {
  selectedDate.value = new Date(currentYear.value, currentMonth.value, day)
}

// 달력 렌더링
const renderCalendar = () => {
<<<<<<< HEAD
  // const firstDay = new Date(currentYear.value, currentMonth.value, 1).getDay()
  // const lastDate = new Date(currentYear.value, currentMonth.value + 1, 0).getDate()
  // const lastDatePrev = new Date(currentYear.value, currentMonth.value, 0).getDate()
  const firstDayOfMonth = new Date(currentYear.value, currentMonth.value, 1).getDay()
  const lastDateOfMonth = new Date(currentYear.value, currentMonth.value + 1, 0).getDate()
  const lastDayOfPrevMonth = new Date(currentYear.value, currentMonth.value, 0).getDate()
=======
  const firstDayOfMonth = new Date(
    currentYear.value,
    currentMonth.value,
    1
  ).getDay();
  const lastDateOfMonth = new Date(
    currentYear.value,
    currentMonth.value + 1,
    0
  ).getDate();
  const lastDayOfPrevMonth = new Date(
    currentYear.value,
    currentMonth.value,
    0
  ).getDate();
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

  let daysHTML = "";

  // 이전 달 날짜
  for (let i = firstDayOfMonth; i > 0; i--) {
    daysHTML += `<span class="text-gray-400">${
      lastDayOfPrevMonth - i + 1
    }</span>`;
  }

  // 현재 달 날짜
  for (let i = 1; i <= lastDateOfMonth; i++) {
<<<<<<< HEAD
    const isToday = i === currentDate.getDate() && currentMonth.value === currentDate.getMonth() && currentYear.value === currentDate.getFullYear() ? "bg-blue-200" : ""

    const isSelected =
      i === selectedDate.value.getDate() && currentMonth.value === selectedDate.value.getMonth() && currentYear.value === selectedDate.value.getFullYear() ? "bg-blue-500 text-white" : ""
=======
    const isToday =
      i === currentDate.getDate() &&
      currentMonth.value === currentDate.getMonth() &&
      currentYear.value === currentDate.getFullYear()
        ? "bg-blue-200"
        : "";
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d

    const isSelected =
      i === selectedDate.value.getDate() &&
      currentMonth.value === selectedDate.value.getMonth() &&
      currentYear.value === selectedDate.value.getFullYear()
        ? "bg-blue-500 text-white"
        : "";

<<<<<<< HEAD
    daysHTML += `<span
      class="${isToday} ${isSelected} ${questClass} cursor-pointer hover:bg-gray-100"
      onclick="this.dispatchEvent(new CustomEvent('date-select', {detail: ${i}, bubbles: true}))"
    >${i}</span>`
  }

  // 다음 달 날짜
  const nextDays = 42 - (firstDayOfMonth + lastDateOfMonth)
=======
    const questStatus =
      questData.value !== "no-data" && questData.value.find((q) => q.day === i);
    const questClass = questStatus
      ? questStatus.isCompleted
        ? "text-green-500"
        : "text-red-500"
      : "";

    daysHTML += `<span
      class="${isToday} ${isSelected} ${questClass} cursor-pointer hover:bg-gray-100"
      onclick="this.dispatchEvent(new CustomEvent('date-select', {detail: ${i}, bubbles: true}))"
    >${i}</span>`;
  }

  // 다음 달 날짜
  const nextDays = 42 - (firstDayOfMonth + lastDateOfMonth);
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
  for (let i = 1; i <= nextDays; i++) {
    daysHTML += `<span class="text-gray-400">${i}</span>`;
  }

  if (daysContainer.value) {
    daysContainer.value.innerHTML = daysHTML;

    if (questData.value === "no-data") {
      daysContainer.value.classList.add("bg-red-100");
    } else {
      daysContainer.value.classList.remove("bg-red-100");
    }
  }
};

// 월 이동
const prevMonth = async () => {
  if (currentMonth.value === 0) {
<<<<<<< HEAD
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
=======
    currentMonth.value = 11;
    currentYear.value--;
  } else {
    currentMonth.value--;
  }

  await fetchQuestData();
};

const nextMonth = async () => {
  if (currentMonth.value === 11) {
    currentMonth.value = 0;
    currentYear.value++;
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
  } else {
    currentMonth.value++;
  }

  await fetchQuestData();
};

// 이벤트 리스너 설정
onMounted(async () => {
  daysContainer.value = document.querySelector(".days");

  // 날짜 선택 이벤트 리스너
  daysContainer.value.addEventListener("date-select", (event) => {
<<<<<<< HEAD
    handleDateSelect(event.detail)
  })

  await fetchQuestData()
})
=======
    handleDateSelect(event.detail);
  });

  await fetchQuestData();
});
>>>>>>> 25dc83ec435c70c77449e52ec0d268259c545e2d
</script>

<style scoped></style>
