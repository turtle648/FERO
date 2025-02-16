<script setup>
import { onMounted, ref, computed, watchEffect } from "vue"
import { storeToRefs } from "pinia"
import { useUserDataStore } from "@/stores/userDataStore"
import { useMainStore } from "@/stores/mainStore"
import { assets } from "@/assets.js"

import StatusModal from "@/components/modal/StatusModal.vue"
import SettingModal from "@/components/modal/SettingModal.vue"
import CharacterModal from "@/components/modal/CharacterModal.vue"
import CalendarModal from "@/components/modal/CalendarModal.vue"
import FitnessModal from "@/components/modal/FitnessModal.vue"
import RecordModal from "@/components/modal/RecordModal.vue"
import QuestModal from "@/components/modal/QuestModal.vue"
import SpeechRecognitionHandler from "@/components/voice/SpeechRecognitionHandler.vue"

// 스토어 가져오기 ==================================================
const userDataStore = useUserDataStore()
const mainStore = useMainStore()
// 반응형 상태 유지 ==================================================
const { userInfo } = storeToRefs(userDataStore)

// 아바타  ==================================================
const hair = ref("")
const face = ref("")
const body = ref("")
// userInfo.avatar가 변경될 때 자동으로 캐릭터 이미지 업데이트
watchEffect(() => {
  if (userInfo.value.avatar) {
    const [hairIndex, faceIndex, bodyIndex] = userInfo.value.avatar || [0, 0, 0]
    hair.value = assets.hair[hairIndex]?.[0] || ""
    face.value = assets.face[faceIndex]?.[0] || ""
    body.value = assets.body[bodyIndex]?.[0] || ""
  }
})

// 모달 상태 및 관리 메서드 ========================================
const modals = ref({ status: false, record: false, setting: false, calendar: false, fitness: false, character: false, quest: false })
const isAnyModalOpen = computed(() => Object.values(modals.value).some((v) => v))
const openModal = (type) => {
  if (!isAnyModalOpen.value) modals.value[type] = true
}
const closeModal = (type) => {
  modals.value[type] = false
}
const modalControl = (type) => {
  if (type === "close") {
    // 모든 모달 종료
    modals.value = { status: false, record: false, setting: false, friend: false, calendar: false, fitness: false, alarm: false, character: false, quest: false }
  } else if (isAnyModalOpen.value) {
    const activeModal = Object.keys(modals.value).find((key) => modals.value[key]) || null
    if (activeModal === type) {
      modals.value[type] = false
    } else {
      modals.value[activeModal] = false
      modals.value[type] = true
    }
  } else {
    modals.value[type] = true
  }
}

// 유저정보/ 튜토리얼정보 불러오기 ==================================================
onMounted(async () => {
  try {
    await userDataStore.checkUserInfo()
  } catch (error) {
    console.error("User Data 로드 중 오류 발생:", error)
  }
  await mainStore.loadTutorial()
})
</script>

<template>
  <div class="fixed inset-0 w-full h-full overflow-hidden">
    <!-- 배경 이미지 -->
  <div class="absolute inset-0 flex items-center justify-center w-screen overflow-hidden" style="image-rendering: pixelated;">
    <img src="@/assets/images/bg_5.jpg" class="w-full h-full object-fill" />
  </div>

<!-- 헤더 부분 -->
<header class="w-full h-[7vh] flex justify-between items-center px-4 nes-container is-rounded">
  <!-- 왼쪽: 유저 정보 탭 -->
  <div class="flex items-center h-full">
    <div class="nes-container is-rounded px-4 py-1 mr-2">
      <span class="nes-text">{{ userInfo.userNickname }}</span>
      <div class="text-xs">
        Lv. {{ userInfo.level }}
      </div>
    </div>
  </div>

  <!-- 오른쪽: 창 제어 버튼 -->
  <div class="flex space-x-2">
    <button 
      class="nes-btn is-warning h-8 w-8 flex items-center justify-center"
      @click="minimizeWindow"
    >
      _
    </button>
    <button 
      class="nes-btn is-error h-8 w-8 flex items-center justify-center"
      @click="openModal('setting')"
    >
      ×
    </button>
  </div>
</header>

  <!-- 캐릭터 -->
  <div class="absolute left-1/2 bottom-[10vh] transform -translate-x-1/2 w-[30vh] h-[60vh] flex items-center justify-center overflow-hidden cursor-pointer" @click="openModal('character')">
    <img v-if="hair && face && body" :src="face" class="absolute w-[30vh] h-[48vh] top-0" />
    <img v-if="hair && face && body" :src="hair" class="absolute w-[30vh] h-[48vh] top-0" />
    <img v-if="hair && face && body" :src="body" class="absolute w-[30vh] h-[48vh] bottom-0" />
    <div v-if="!face && !hair && !body" class="text-gray-500 text-center">캐릭터 없음</div>
  </div>

  <!-- 하단 메뉴 -->
  <div class="footer absolute bottom-0 w-full h-[7vh] grid grid-cols-5 bg-white text-center">
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer flex" @click="openModal('calendar')">
      <img src="@/assets/images/icon/icon-calendar.png" class="w-[5vh]" />
    </div>
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer" @click="openModal('record')">
      <img src="@/assets/images/icon/icon-report.png" class="w-[6vh]" alt="" />
    </div>
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer" @click="openModal('fitness')">
      <img src="@/assets/images/icon/icon-fitness.png" class="w-[7vh]" />
    </div>
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer" @click="openModal('quest')">
      <img src="@/assets/images/icon/icon-quest.png" class="w-[5vh]" alt="" />
    </div>
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer" @click="openModal('setting')">
      <img src="@/assets/images/icon/icon-setting.png" class="w-[5vh]" />
    </div>
  </div>

  <!-- 모달 컴포넌트 -->
  <StatusModal v-if="modals.status" @close-modal="closeModal('status')" @open-modal="openModal('status')" />
  <SettingModal v-if="modals.setting" @close-modal="closeModal('setting')" @open-modal="openModal('setting')" />
  <CharacterModal v-if="modals.character" @close-modal="closeModal('character')" @open-modal="openModal('character')" />
  <CalendarModal v-if="modals.calendar" @close-modal="closeModal('calendar')" @open-modal="openModal('calendar')" />
  <FitnessModal v-if="modals.fitness" @close-modal="closeModal('fitness')" @open-modal="openModal('fitness')" />
  <RecordModal v-if="modals.record" @close-modal="closeModal('record')" @open-modal="openModal('record')" />
  <QuestModal v-if="modals.quest" @close-modal="closeModal('quest')" @open-modal="openModal('quest')" />

  <!-- 백그라운드 음성인식 -->
  <SpeechRecognitionHandler @voice-control="modalControl" />
  </div>
  
</template>

<style scoped></style>
