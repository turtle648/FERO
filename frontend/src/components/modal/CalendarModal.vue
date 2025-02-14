<template>
  <div
    class="fixed inset-0 flex items-center justify-center bg-white h-[70vh] w-[40vh]"
  >
    <div class="flex flex-col bg-white relative w-full h-full pt-5 px-3">
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
        </div>
      </div>
      <div class="flex flex-col w-full h-[50%]">
        <Bar :data="chartData" :options="options" />
      </div>
    </div>
  </div>
</template>

<script setup>
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
const getSelectedDateData = () => {
  const year = selectedDate.value.getFullYear();
  const month = selectedDate.value.getMonth();
  const date = selectedDate.value.getDate();
  console.log("📅" + year + "년 " + month + "월 " + date + "일");

  console.log(chartConfig.dummy);

  const dateData = chartConfig.dummy.find((value) => {
    return value.date === date;
  });

  console.log(dateData);

  return dateData ? dateData.status : [0, 0, 0, 0, 0, 0];
};

// 퀘스트 데이터를 저장
const questData = ref([]);

// 데이터 가져오기 함수
const fetchQuestData = async () => {
  try {
    const response = await mainStore.isQuestCompleted(
      currentYear.value,
      currentMonth.value + 1
    );

    if (response && response.length > 0) {
      questData.value = response;
    } else {
      questData.value = [];
    }
  } catch (error) {
    if (error.response && error.response.status === 204) {
      console.log("🚨 No data for this month");
      questData.value = "no-data";
    } else {
      console.error("🚨 Error fetching quest data:", error);
    }
  } finally {
    renderCalendar();
  }
};

// 날짜 선택 핸들러
const handleDateSelect = (day) => {
  selectedDate.value = new Date(currentYear.value, currentMonth.value, day);
};

// 월별 날짜 렌더링 함수
const renderCalendar = () => {
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

  let daysHTML = "";

  // 이전 달 날짜
  for (let i = firstDayOfMonth; i > 0; i--) {
    daysHTML += `<span class="text-gray-400">${
      lastDayOfPrevMonth - i + 1
    }</span>`;
  }

  // 현재 달 날짜
  for (let i = 1; i <= lastDateOfMonth; i++) {
    const isToday =
      i === currentDate.getDate() &&
      currentMonth.value === currentDate.getMonth() &&
      currentYear.value === currentDate.getFullYear()
        ? "bg-blue-200"
        : "";

    const isSelected =
      i === selectedDate.value.getDate() &&
      currentMonth.value === selectedDate.value.getMonth() &&
      currentYear.value === selectedDate.value.getFullYear()
        ? "bg-blue-500 text-white"
        : "";

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

// 이전/다음 달 이동 함수
const prevMonth = async () => {
  if (currentMonth.value === 0) {
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
    handleDateSelect(event.detail);
  });

  await fetchQuestData();
});
</script>

<style scoped></style>
