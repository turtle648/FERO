<!-- components/modal/RecordModal.vue -->
<template>
    <BaseModal title="Records" @close-modal="$emit('close-modal')">
      <!-- 전적 리스트 컨테이너 -->
      <div 
        class="h-full overflow-y-auto"
        ref="matchDataContainer"
        @touchstart="handleTouchStart"
        @touchmove="handleTouchMove"
        @touchend="handleTouchEnd"
      >
        <!-- 전적 아이템 -->
        <div 
          v-for="(match, index) in matchData" 
          :key="index"
          class="flex justify-between items-center p-3 border-b border-gray-200 hover:bg-gray-50"
        >
          <!-- 왼쪽: 매치 정보 -->
          <div class="flex-1 text-left">
            <div class="text-sm text-gray-600">
              {{ computeMatchTime(match.date, match.time) }} | {{ match.type }}
            </div>
            <div class="font-medium">
              <span 
                :class="match.result === '승리' ? 'text-blue-600' : 'text-red-600'"
              >
                {{ match.result }}
              </span>
              <span class="ml-2">{{ match.score }}점</span>
            </div>
          </div>
  
          <!-- 오른쪽: 참가자 정보 -->
          <div class="flex flex-col items-end">
            <span class="text-xs text-gray-500">
              {{ match.partners?.length }}인 게임
            </span>
            <button 
              class="mt-1 px-2 py-1 text-xs bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors"
            >
              참여자 확인
            </button>
          </div>
        </div>
      </div>
    </BaseModal>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { getMatchData } from '@/api/userAPI'
  import BaseModal from './BaseModal.vue'
  
  const matchData = ref()
  defineEmits(['close-modal'])
  
  // 매치 시간 계산 함수
  const computeMatchTime = (date, time) => {
      const matchDateString = `${date}T${time}`
      const matchDate = new Date(matchDateString)
      const today = new Date()
      const timeDiff = today - matchDate
      const diffMinutes = Math.floor(timeDiff / (1000 * 60))
      const diffHours = Math.floor(timeDiff / (1000 * 60 * 60))
      const diffDays = Math.floor(timeDiff / (1000 * 60 * 60 * 24))
      const diffMonths = today.getMonth() - matchDate.getMonth() + 
                        (today.getFullYear() - matchDate.getFullYear()) * 12
      const diffYears = today.getFullYear() - matchDate.getFullYear()
  
      if (diffMinutes < 60) return `${diffMinutes}분 전`
      if (diffHours < 24) return `${diffHours}시간 전`
      if (diffDays < 7) return `${diffDays}일 전`
      if (diffDays < 10) return `1주 전`
      if (diffDays < 17) return `2주 전`
      if (diffDays < 24) return `3주 전`
      if (diffDays < 28) return `4주 전`
      if (diffDays < 30) return `1개월 전`
      if (diffMonths < 12) return `${diffMonths}개월 전`
      return `${diffYears}년 전`
  }
  
  // 데이터 로드
  onMounted(() => {
      matchData.value = getMatchData()
  })
  </script>