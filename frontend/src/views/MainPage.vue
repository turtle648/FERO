<template>
  <!-- 배경 이미지 -->
  <div class="absolute inset-0 flex items-center justify-center overflow-hidden">
    <img src="@/assets/images/background_image3.png" class="w-full h-full object-fill" />
  </div>

  <!-- 헤더 -->
  <header class="absolute top-0 w-full h-[7vh] bg-green-400 text-white font-bold flex justify-between items-center px-4">
    <div class="flex items-center w-1/2 cursor-pointer" @click="openModal('status')">
      <img src="@/assets/images/profile/default_profile.png" class="w-[7vh] h-[7vh] rounded-full object-cover" />
      <div class="ml-4 flex-1">
        <div class="text-[2vh]">{{ userInfo.userNickname }}</div>
        <div class="relative w-full h-[2vh] bg-gray-200 rounded-full mt-1">
          <div class="absolute inset-0 flex items-center justify-center text-black text-[1.5vh] font-bold">
            Lv. {{ userInfo.level }}
          </div>
          <div class="bg-blue-500 h-full rounded-full" :style="{ width: Math.min(userInfo.experience/2, 100) + '%' }"></div>
        </div>
      </div>
    </div>
    <img class="w-[5vh] h-[5vh] object-cover cursor-pointer" @click="openModal('setting')" src="@/assets/images/icon/setting.png" alt="설정" />
  </header>

  <!-- 알림 아이콘 -->
  <div class="absolute top-[10vh] right-[2vh] w-[4vh] h-[3vh] cursor-pointer" @click="openModal('alarm')">
    <img src="@/assets/images/icon/alarm2.png" alt="알림" />
  </div>

  <!-- 캐릭터 -->
  <div class="absolute left-1/2 bottom-[6vh] transform -translate-x-1/2 w-[40vh] h-[50vh] flex items-center justify-center overflow-hidden cursor-pointer" @click="openModal('character')">
    <img v-if="hair && face && body" :src="face" class="absolute w-[40vh] h-[40vh] top-0" />
    <img v-if="hair && face && body" :src="hair" class="absolute w-[40vh] h-[40vh] top-0" />
    <img v-if="hair && face && body" :src="body" class="absolute w-[40vh] h-[40vh] bottom-0" />
    <div v-if="!face && !hair && !body" class="text-gray-500 text-center">캐릭터 없음</div>
  </div>

  <!-- 하단 메뉴 -->
  <div class="footer absolute bottom-0 w-full h-[7vh] grid grid-cols-5 bg-white text-center">
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer" @click="openModal('friend')">친구</div>
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer" @click="openModal('calendar')">달력</div>
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer" @click="openModal('fitness')">운동</div>
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer" @click="openModal('record')">전적</div>
    <div class="footer-btn p-[1vh] bg-blue-500 text-white cursor-pointer">퀘스트</div>
  </div>

  <!-- 모달 컴포넌트 -->
  <StatusModal v-if="modals.status" @close-modal="closeModal('status')" @open-modal="openModal('status')"/>
  <SettingModal v-if="modals.setting" @close-modal="closeModal('setting')" @open-modal="openModal('setting')" />
  <AlarmModal v-if="modals.alarm" @close-modal="closeModal('alarm')" @open-modal="openModal('alarm')" />
  <CharacterModal v-if="modals.character" @close-modal="closeModal('character')" @open-modal="openModal('character')" />
  <FriendListModal v-if="modals.friend" @close-modal="closeModal('friend')" @open-modal="openModal('friend')" />
  <CalendarModal v-if="modals.calendar" @close-modal="closeModal('calendar')" @open-modal="openModal('calendar')" />
  <FitnessModal v-if="modals.fitness" @close-modal="closeModal('fitness')" @open-modal="openModal('fitness')" />
  <MatchRecordModal v-if="modals.record" @close-modal="closeModal('record')" @open-modal="openModal('record')" />
</template>

<script setup>
// ==================================================
import { onMounted, ref, computed, watchEffect } from "vue"
import { storeToRefs } from "pinia"
import { useUserDataStore } from "@/stores/userDataStore"
import { useMainStore } from "@/stores/mainStore"
import { assets } from '@/assets.js'

import StatusModal from "@/components/modal/StatusModal.vue"
import SettingModal from "@/components/modal/SettingModal.vue"
import AlarmModal from "@/components/modal/AlarmModal.vue"
import CharacterModal from "@/components/modal/CharacterModal.vue"
import FriendListModal from "@/components/modal/FriendListModal.vue"
import CalendarModal from "@/components/modal/CalendarModal.vue"
import FitnessModal from "@/components/modal/FitnessModal.vue"
import MatchRecordModal from "@/components/modal/MatchRecordModal.vue"


// 스토어 가져오기 ==================================================
const userDataStore = useUserDataStore()
const mainStore = useMainStore()
// 반응형 상태 유지 ==================================================
const { userInfo } = storeToRefs(userDataStore)


// 아바타  ==================================================
const hair = ref(""); const face = ref(""); const body = ref("")
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
const modals = ref({ status: false, record: false, setting: false, friend: false, calendar: false, fitness: false, alarm: false, character: false })
const isAnyModalOpen = computed(() => Object.values(modals.value).some(v => v))
const openModal = (type) => { if (!isAnyModalOpen.value) modals.value[type] = true }
const closeModal = (type) => { modals.value[type] = false }


// 유저정보/ 튜토리얼정보 불러오기 ==================================================
onMounted(async () => {
  try { await userDataStore.checkUserInfo() } 
  catch (error) { console.error("User Data 로드 중 오류 발생:", error) }
  await mainStore.loadTutorial()
})



</script>

<style scoped>
</style>
