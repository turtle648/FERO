<!-- components/modal/CharacterModal.vue -->
<template>
  <BaseModal title="Customizer" @close-modal="$emit('close-modal')">
    <div class="flex flex-col items-center h-full">
      <!-- 선택된 캐릭터 미리보기 -->
      <div class="relative w-[40vw] aspect-square flex items-center justify-center mb-4 
                  border-2 border-gray-500 bg-gray-100 rounded-md">
        <img 
          v-if="selected.face" 
          :src="selected.face[0]" 
          class="absolute w-[80%] top-0"
        >
        <img 
          v-if="selected.hair" 
          :src="selected.hair[0]" 
          class="absolute w-[80%] top-0"
        >
        <img 
          v-if="selected.body" 
          :src="selected.body[0]" 
          class="absolute w-[80%] bottom-0"
        >
      </div>

      <!-- 카테고리 선택 -->
      <div class="flex space-x-4 mb-4">
        <button 
          v-for="category in categories" 
          :key="category"
          @click="currentCategory = category"
          class="w-[15vw] h-12 flex items-center justify-center border rounded-md 
                 transition duration-200 ease-in-out"
          :class="{ 'bg-blue-500 text-white': currentCategory === category }"
        >
          {{ category }}
        </button>
      </div>

      <div class="w-full border-t border-gray-300 mb-4"></div>

      <!-- 선택 가능한 요소들 -->
      <div 
        class="flex-1 w-full overflow-y-auto"
        :style="{ maxHeight: selectionBoxHeight }"
      >
        <div class="grid grid-cols-3 gap-2">
          <img 
            v-for="item in assets[currentCategory]" 
            :key="item[1]"
            :src="item[0]" 
            @click="selectItem(currentCategory, item)"
            class="w-[15vw] aspect-square border-2 p-1 bg-white cursor-pointer 
                   rounded-md transition-all duration-200 ease-in-out"
            :class="{ 
              'border-blue-600 bg-blue-100': selected[currentCategory]?.[1] === item[1], 
              'border-gray-300': selected[currentCategory]?.[1] !== item[1] 
            }"
          />
        </div>
      </div>

      <!-- 완료 버튼 -->
      <div class="w-full mt-4 flex justify-center">
        <button 
          @click="confirmSelection"
          class="w-[15vw] h-[7.5vw] border rounded-md bg-blue-500 text-white 
                 hover:bg-green-600 transition duration-200 ease-in-out"
        >
          완료
        </button>
      </div>
    </div>
  </BaseModal>
</template>

<script setup>
import { ref, computed, watchEffect, onMounted } from 'vue'
import { storeToRefs } from "pinia"
import { assets } from '@/assets.js'
import { useUserDataStore } from "@/stores/userDataStore"
import BaseModal from './BaseModal.vue'

const userDataStore = useUserDataStore()
const { userInfo } = storeToRefs(userDataStore)

const emit = defineEmits(['close-modal'])

// 선택 가능한 카테고리 목록
const categories = ['hair', 'face', 'body']
const currentCategory = ref('hair')

// 선택된 캐릭터 정보 저장
const selected = ref({ hair: null, face: null, body: null })

// userInfo.avatar 값 변경 시 selected 업데이트
watchEffect(() => {
  if (userInfo.value?.avatar) {
    selected.value.hair = selected.value.hair || [assets['hair'][userInfo.value.avatar[0]][0], userInfo.value.avatar[0]]
    selected.value.face = selected.value.face || [assets['face'][userInfo.value.avatar[1]][0], userInfo.value.avatar[1]]
    selected.value.body = selected.value.body || [assets['body'][userInfo.value.avatar[2]][0], userInfo.value.avatar[2]]
  }
})

// 기본값 설정
onMounted(() => {
  if (!selected.value.hair) selected.value.hair = [assets['hair'][0][0], 0]
  if (!selected.value.face) selected.value.face = [assets['face'][0][0], 0]
  if (!selected.value.body) selected.value.body = [assets['body'][0][0], 0]
})

// 아이템 선택
const selectItem = (category, item) => {
  selected.value[category] = item
}

// 선택 완료 처리
const confirmSelection = async () => {
  const hairIndex = selected.value.hair?.[1] ?? 0
  const faceIndex = selected.value.face?.[1] ?? 0
  const bodyIndex = selected.value.body?.[1] ?? 0

  const newAvatar = `${hairIndex}-${faceIndex}-${bodyIndex}`
  
  const success = await userDataStore.updateAvatar(newAvatar)
  
  if (success) {
    console.log("아바타가 성공적으로 변경되었습니다!", userDataStore.userInfo.avatar)
  } else {
    console.error("아바타 변경 실패")
  }

  emit('close-modal')
}

// 선택 박스 높이 계산
const selectionBoxHeight = computed(() => `calc(70vh - 5rem - 40vw)`)
</script>
