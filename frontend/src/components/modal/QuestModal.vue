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
          <p class="text-gray-700">시간: {{ questData.quest_time }}</p>
          <p class="text-gray-700">운동 횟수: {{ questData.real_cnt }}</p>
          <p class="text-gray-700">목표 갯수: {{ questData.exercise_cnt }}</p>
          <p class="text-gray-700">
            상태:
            <span
              :class="
                questData.is_completed ? 'text-green-600' : 'text-red-600'
              "
            >
              {{ questData.is_completed ? "완료" : "미완료" }}
            </span>
          </p>
          <p class="text-gray-700">메세지: {{ questData.message }}</p>
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import BaseModal from "./BaseModal.vue";

const questData = ref([]);
const isLoading = ref(false);

defineEmits(["close-modal"]);

const getQuestData = async () => {
  isLoading.value = true;
  try {
    const today = new Date().toISOString().split("T")[0];
    const token = localStorage.getItem("authToken");

    const response = await axios.get("/dummy_data/getQuestData.json", {
      headers: {
        Authorization: `${token}`,
      },
      params: {
        date: today,
      },
    });

    questData.value = response.data;
  } catch (error) {
    console.error("퀘스트 데이터 조회 실패", error);
  } finally {
    isLoading.value = false;
  }
};

onMounted(() => {
  getQuestData();
});
</script>
