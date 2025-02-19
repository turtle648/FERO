<!-- components/modal/QuestModal.vue -->
<template>
  <BaseModal title="Quest" @close-modal="$emit('close-modal')">
    <!-- 로딩 상태 -->
    <div v-if="!questData" class="flex justify-center items-center h-full">
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
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import BaseModal from './BaseModal.vue'
import { questData } from "@/stores/mainStore"

defineEmits(['close-modal'])
</script>
