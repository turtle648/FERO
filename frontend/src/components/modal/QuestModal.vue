<!-- components/modal/QuestModal.vue -->
<template>
  <BaseModal title="Quest" @close-modal="$emit('close-modal')" class="overflow-hidden">
    <!-- 로딩 상태 -->
    <div v-if="!questData" class="flex justify-center items-center h-64">
      <div class="text-gray-500">데이터 불러오는 중...</div>
    </div>

    <!-- 퀘스트 데이터 -->
    <div v-else class="max-h-[80vh] px-6 py-3">
      <div class="flex flex-col items-center space-y-3">
        <!-- 헤더 섹션 -->
        <div class="text-center w-full">
          <h1 class="text-4xl font-bold text-gray-800">Today's Fitness</h1>
          <h2 class="text-3xl font-semibold text-blue-600 mt-1">[SQUAT]</h2>
          <div class="flex justify-between items-center mt-2">
            <span class="text-lg font-medium">일일 퀘스트</span>
            <span :class="[
              questData.isCompleted 
                ? 'text-blue-500 font-semibold'
                : 'text-gray-500'
            ]">
              {{ questData.isCompleted ? '성공' : '진행중' }}
            </span>
          </div>
        </div>

        <!-- 퀘스트 상태 카드 -->
        <div class="w-full bg-gray-50 rounded-xl p-5">
          <!-- 강자 되기 섹션 -->
          <div>
            <h3 class="text-lg font-medium">강자가 되기 위한 준비</h3>
            <p class="text-gray-700 mt-2">{{ questData.message }}</p>
          </div>

          <hr class="border-gray-300 border-t-2 my-3">

          <!-- 진행도 표시 -->
          <div class="flex items-center justify-center py-2">
            <div class="flex items-center gap-4">
              <span class="font-bold text-xl">NOW</span>
              <div class="flex items-center gap-2">
                <span :class="[
                  'text-2xl font-bold',
                  questData.isCompleted ? 'text-blue-600' : 'text-gray-600'
                ]">{{ questData.realCnt }}</span>
                <span class="text-gray-500">/</span>
                <span class="text-xl">{{ questData.exerciseCnt }}</span>
              </div>
            </div>
          </div>

          <hr class="border-gray-300 border-t-2 my-3">

          <!-- 보상 버튼 -->
          <!-- <div class="text-center">
            <button 
              class="px-6 py-2 bg-indigo-600 hover:bg-indigo-700 text-white
                     
                     font-medium nes-btn primary"
            >
              레벨 {{ userInfo.level+1 }} 달성 보상
            </button>
          </div> -->
        </div>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import BaseModal from './BaseModal.vue'
import { questData } from "@/stores/mainStore"
// import { useUserDataStore } from '@/stores/userDataStore'

// const userDataStore = useUserDataStore() 
// const userInfo = userDataStore.userInfo
defineEmits(['close-modal'])
</script>
