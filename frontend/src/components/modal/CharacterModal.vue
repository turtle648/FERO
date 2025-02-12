<template>
  <div class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50" @click="closeCharacterModal">
    <div class="relative w-[70vw] h-[85vh] bg-white/90 rounded-lg shadow-xl overflow-hidden flex flex-col items-center p-6"
         @click.stop>
      
      <!-- 모달 종료 버튼 -->
      <button @click="closeCharacterModal"
              class="absolute top-2 right-2 text-gray-600 hover:text-black text-lg">
        ✖
      </button>

      <!-- 선택된 캐릭터 미리보기 -->
      <div class="relative w-[40vw] aspect-square flex items-center justify-center mb-4 border-2 border-gray-500 bg-gray-100 rounded-md">
        <img v-if="selected.face" :src="selected.face[0]" class="absolute w-[80%] top-0">
        <img v-if="selected.hair" :src="selected.hair[0]" class="absolute w-[80%] top-0">
        <img v-if="selected.body" :src="selected.body[0]" class="absolute w-[80%] bottom-0">
      </div>

      <!-- 카테고리 선택 -->
      <div class="flex space-x-4">
        <button v-for="category in categories" :key="category"
          @click="currentCategory = category"
          class="w-[15vw] h-[3rem] flex items-center justify-center border rounded-md transition duration-200 ease-in-out"
          :class="{ 'bg-blue-500 text-white': currentCategory === category }">
          {{ category }}
        </button>
      </div>

      <hr class="w-full border-gray-300 mb-4">

      <!-- 선택 가능한 요소들 -->
      <div class="flex flex-col items-center w-full overflow-y-auto" :style="{ maxHeight: selectionBoxHeight }">
        <div class="grid grid-cols-3 rounded-md">
          <img v-for="item in assets[currentCategory]" :key="item[1]"
            :src="item[0]" @click="selectItem(currentCategory, item)"
            class="w-[15vw] aspect-square border-2 p-1 bg-white cursor-pointer rounded-md transition-all duration-200 ease-in-out"
            :class="{ 
              'border-blue-600 bg-blue-100': selected[currentCategory]?.[1] === item[1], 
              'border-gray-300': selected[currentCategory]?.[1] !== item[1] 
            }"/>
        </div>
      </div>

      <!-- 완료 버튼 -->
      <div class="absolute bottom-0 left-0 w-full flex justify-center p-4 rounded-b-lg">
        <button @click="confirmSelection"
                class="w-[15vw] h-[7.5vw] border rounded-md bg-blue-500 text-white hover:bg-green-600 transition duration-200 ease-in-out">
          완료
        </button>
      </div>

    </div>
  </div>
</template>
  
<script setup>
import { ref, computed, watchEffect, defineEmits, onMounted } from 'vue'
import { storeToRefs } from "pinia"
import { assets } from '@/assets.js'
import { useUserDataStore } from "@/stores/userDataStore"

const userDataStore = useUserDataStore()
const { userInfo } = storeToRefs(userDataStore)

// 모달 닫기 이벤트
const emit = defineEmits(['close-modal'])
const closeCharacterModal = () => emit('close-modal')

// 선택 가능한 카테고리 목록
const categories = ['hair', 'face', 'body']
const currentCategory = ref('hair')

// 선택된 캐릭터 정보 저장 (초기값: 현재 `userInfo.avatar`)
const selected = ref({ hair: null, face: null, body: null })

// `userInfo.avatar` 값이 변경될 때 자동으로 `selected` 업데이트 + 기본값 유지
watchEffect(() => {
  if (userInfo.value?.avatar) {
    selected.value.hair = selected.value.hair || [assets['hair'][userInfo.value.avatar[0]][0], userInfo.value.avatar[0]]
    selected.value.face = selected.value.face || [assets['face'][userInfo.value.avatar[1]][0], userInfo.value.avatar[1]]
    selected.value.body = selected.value.body || [assets['body'][userInfo.value.avatar[2]][0], userInfo.value.avatar[2]]
  }
})

// onMounted를 활용하여 기본값 강제 설정
onMounted(() => {
  if (!selected.value.hair) selected.value.hair = [assets['hair'][0][0], 0]
  if (!selected.value.face) selected.value.face = [assets['face'][0][0], 0]
  if (!selected.value.body) selected.value.body = [assets['body'][0][0], 0]
})

// 선택 해제 방지
const selectItem = (category, item) => {
  selected.value[category] = item
}

// 완료 버튼 클릭 시 선택 데이터 처리
const confirmSelection = async () => {
  const hairIndex = selected.value.hair ? selected.value.hair[1] : 0
  const faceIndex = selected.value.face ? selected.value.face[1] : 0
  const bodyIndex = selected.value.body ? selected.value.body[1] : 0

  const newAvatar = `${hairIndex}-${faceIndex}-${bodyIndex}`
  console.log("변환된 아바타 코드:", newAvatar)

  const success = await userDataStore.updateAvatar(newAvatar)

  if (success) {
    console.log("아바타가 성공적으로 변경되었습니다!", userDataStore.userInfo.avatar)
  } else {
    console.error("아바타 변경 실패")
  }

  closeCharacterModal()
}

// 선택 요소 박스의 maxHeight 동적 계산
const selectionBoxHeight = computed(() => `calc(70vh - 5rem - 40vw)`)

</script>

