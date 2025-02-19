<template>
  <BaseModal class="font-dgm" title="Status" @close-modal="$emit('close-modal')">
    <div class="h-full">
      <div class="info-container flex items-center space-x-8">
        <img class="w-[8vh]" src="@/assets/images/profile/default-image-1.png" alt="랭크" />

        <!-- 유저 정보 -->
        <div class="user-container flex flex-col">
          <p class="font-dgm text-lg">Lv.{{ userInfo.level }}</p>
          <p class="font-dgm text-lg">{{ userInfo.userNickname }}</p>
          <p class="font-dgm text-lg">경험치: {{ userInfo.experience*100/200 }}%</p>
        </div>
      </div>

      <!-- 일일 달성률 -->
      <div class="progress-container space-y-2 mt-2 mb-3">
        <span class="font-dgm">일일 달성률</span>
        <progress class="nes-progress is-primary w-[100%] h-[3vh]" 
          :value="(questData.realCnt / questData.exerciseCnt) * 100" 
          max="100">
        </progress>
        <span v-for="n in 5" :key="n"
          :class="{
            filled: (100 * questData.realCnt / questData.exerciseCnt) / 20 >= n,
            empty: (100 * questData.realCnt / questData.exerciseCnt) / 20 < n,
          }"
          class="progress-box-cell"
        ></span>
      </div>

      <!-- 그래프 이미지 -->
      <div class="status-element status-4">
        <div class="status-graph">
          <PolarArea :data="chartData" :options="options" />
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import BaseModal from "./BaseModal.vue"
import { ref, computed } from "vue"
import { useUserDataStore } from "@/stores/userDataStore"
import { questData } from "@/stores/mainStore"

import { Chart as ChartJS, RadialLinearScale, ArcElement, Tooltip, Legend } from "chart.js"
import { PolarArea } from "vue-chartjs"
import * as chartConfig from "../config/chartConfig.js"

defineEmits(["close-modal"])

ChartJS.register(RadialLinearScale, ArcElement, Tooltip, Legend)

// 글로벌 폰트 설정
ChartJS.defaults.font.family = "DungGeunMo" // 원하는 폰트 이름
ChartJS.defaults.font.size = 14 // 폰트 크기
ChartJS.defaults.font.weight = "normal" // 폰트 굵기
ChartJS.defaults.color = "#000" // 텍스트 색상

const options = ref(chartConfig.options)
const userDataStore = useUserDataStore()
const userInfo = ref(userDataStore.userInfo)

const chartData = computed(() => ({
  labels: ["팔", "다리", "가슴", "복근", "등", "체력"],
  datasets: [
    {
      label: "운동 데이터",
      backgroundColor: [
        "rgba(255, 99, 132, 0.8)", // 팔 - 빨강
        "rgba(54, 162, 235, 0.8)", // 다리 - 파랑
        "rgba(255, 206, 86, 0.8)", // 가슴 - 노랑
        "rgba(75, 192, 192, 0.8)", // 복근 - 청록
        "rgba(153, 102, 255, 0.8)", // 등 - 보라
        "rgba(255, 140, 0, 0.8)", // 체력 - 주황
      ],
      borderColor: "#fff",
      borderWidth: 1,
      hoverBackgroundColor: ["rgba(255, 99, 132, 1)", "rgba(54, 162, 235, 1)", "rgba(255, 206, 86, 1)", "rgba(75, 192, 192, 1)", "rgba(153, 102, 255, 1)", "rgba(255, 140, 0, 1)"],
      data: getSelectedDateData(),
    },
  ],
}))

const getSelectedDateData = () => {
  const data = [
    userInfo.value.userStats.armsStats, // 팔
    userInfo.value.userStats.legsStats, // 다리
    userInfo.value.userStats.chestStats, // 가슴
    userInfo.value.userStats.absStats, // 복근
    userInfo.value.userStats.backStats, // 등
    userInfo.value.userStats.staminaStats, // 체력
  ]
  return data || [0, 0, 0, 0, 0, 0]
}
</script>

<style scoped></style>
