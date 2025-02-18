<!-- components/modal/CalendarModal.vue -->
<template>
  <BaseModal title="Calendar" @close-modal="$emit('close-modal')">
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
        <div ref="daysContainer" class="grid grid-cols-7 gap-1 days">
          <!-- 날짜들은 renderCalendar 함수에서 동적으로 추가됨 -->
        </div>
      </div>
      <div class="flex flex-col w-full h-[50%]">
        <Bar ref="chartRef" :data="chartData" :options="options" />
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import BaseModal from "./BaseModal.vue";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from "chart.js";
import { ref, defineEmits, onMounted } from "vue";
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

// defineEmits(["close-modal"])

// 날짜 관련 상태
// const currentDate = new Date()
// const currentYear = ref(currentDate.getFullYear())
// const currentMonth = ref(currentDate.getMonth())
// const daysContainer = ref(null)
// const questData = ref([])
// 선택된 날짜 상태 관리
const selectedDate = ref(new Date());

// 차트 데이터 computed 속성으로 변경
const chartData = ref({
  labels: ["팔", "다리", "가슴", "복근", "등", "체력"],
  datasets: [
    {
      label: "운동 데이터",
      backgroundColor: "rgba(236, 72, 153, 1)",
      pointBackgroundColor: "rgba(236, 72, 153, 1)",
      pointBorderColor: "#fff",
      pointHoverBackgroundColor: "#fff",
      pointHoverBorderColor: "rgba(236, 72, 153, 1)",
      data: [0, 0, 0, 0, 0, 0], // 기본값 설정
    },
  ],
});

const options = ref(chartConfig.options);

defineEmits(["close-modal"]);

// 날짜 관련 상태 관리
const currentDate = new Date();
const currentYear = ref(currentDate.getFullYear());
const currentMonth = ref(currentDate.getMonth());
const daysContainer = ref(null);
const chartRef = ref(null);

const updateChartData = async () => {
  const data = await getSelectedDateData();
  console.log(data);

  chartData.value = {
    ...chartData.value,
    datasets: [
      {
        ...chartData.value.datasets[0],
        data: data,
      },
    ],
  };
};

// 선택된 날짜의 데이터 가져오기
const getSelectedDateData = async () => {
  const year = selectedDate.value.getFullYear();
  const month = selectedDate.value.getMonth() + 1;
  const date = selectedDate.value.getDate();
  console.log("📅" + year + "년 " + month + "월 " + date + "일");

  const response = await mainStore.getMonthStatus(year, month);
  console.log(response);

  const dateData = response.find((value) => value.date == date);

  return dateData ? dateData.status : [0, 0, 0, 0, 0, 0];
};

// 퀘스트 데이터를 저장
const questData = ref([]);

// 퀘스트 데이터 가져오기
const fetchQuestData = async () => {
  console.log("💯" + currentYear.value + ":" + currentMonth.value);

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
const handleDateSelect = async (day) => {
  selectedDate.value = new Date(currentYear.value, currentMonth.value, day);
  await updateChartData();
};

// 달력 렌더링
const renderCalendar = () => {
  // const firstDay = new Date(currentYear.value, currentMonth.value, 1).getDay()
  // const lastDate = new Date(currentYear.value, currentMonth.value + 1, 0).getDate()
  // const lastDatePrev = new Date(currentYear.value, currentMonth.value, 0).getDate()
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

// 월 이동
const prevMonth = async () => {
  if (currentMonth.value === 0) {
    currentYear.value--;
    currentMonth.value = 11;
  } else {
    currentMonth.value--;
  }
  await fetchQuestData();
};

const nextMonth = async () => {
  if (currentMonth.value === 11) {
    currentYear.value++;
    currentMonth.value = 0;
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
  await updateChartData();
});
</script>

<style scoped></style>
