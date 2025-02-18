<script setup>
import { onMounted, ref, computed, watchEffect } from "vue"
import { storeToRefs } from "pinia"
import { useUserDataStore } from "@/stores/userDataStore"
import { useUserStore } from "@/stores/store"
import { useMainStore, TUTORIAL_IDS } from "@/stores/mainStore"
import { assets } from "@/assets.js"

import StatusModal from "@/components/modal/StatusModal.vue"
import SettingModal from "@/components/modal/SettingModal.vue"
import CharacterModal from "@/components/modal/CharacterModal.vue"
import CalendarModal from "@/components/modal/CalendarModal.vue"
import FitnessModal from "@/components/modal/FitnessModal.vue"
import RecordModal from "@/components/modal/RecordModal.vue"
import QuestModal from "@/components/modal/QuestModal.vue"
import SpeechRecognitionHandler from "@/components/voice/SpeechRecognitionHandler.vue"
import TutorialConfirmModal from "@/components/modal/TutorialConfirmModal.vue"
import MainTutorialComponent from "@/components/MainTutorialComponent.vue"
import SmallBaseModal from "@/components/modal/SmallBaseModal.vue"

// 스토어 가져오기 ==================================================
const userDataStore = useUserDataStore()
const userStore = useUserStore()
const mainStore = useMainStore()

// 메인페이지 튜토리얼=====================
const showTutorialConfirm = ref(false)
const showTutorialComponent = ref(false)

const startTutorial = () => {
  showTutorialConfirm.value = false
  showTutorialComponent.value = true
}

// 튜토리얼 종료
const closeTutorial = () => {
  showTutorialComponent.value = false
}

// 튜토리얼 거절
const rejectTutorial = async () => {
  showTutorialConfirm.value = false
  await mainStore.completeTutorial(TUTORIAL_IDS.MAIN_PAGE)
}

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

// 로그아웃 ==================================================
// 로그아웃 확인 모달 상태
const showLogoutConfirm = ref(false)

// 로그아웃 확인 모달 표시
const handleCloseClick = () => {
  showLogoutConfirm.value = true
}

// 로그아웃 처리
const handleLogout = async () => {
  await userStore.logOut()
}

// 유저정보/ 튜토리얼정보 불러오기 ==================================================
onMounted(async () => {
  try {
    await userDataStore.checkUserInfo()
    await mainStore.loadTutorial()

    // 메인페이지 튜토리얼 진행 여부 확인
    const mainTutorial = mainStore.tutorial.find((t) => t.tutorialId === TUTORIAL_IDS.MAIN_PAGE)

    if (!mainTutorial?.completed) {
      showTutorialConfirm.value = true
    }
  } catch (error) {
    console.error("User Data 로드 중 오류 발생:", error)
  }
  await mainStore.loadTutorial()
})
</script>

<template>
  <div class="fixed inset-0 w-full h-full overflow-hidden">
    <!-- Windows 98 스타일 테두리 -->
    <div class="absolute inset-0">

      <!-- 배경 이미지 -->
      <div class="absolute inset-0 flex items-center justify-center w-screen overflow-hidden" style="image-rendering: pixelated">
        <img src="@/assets/images/bg_5.jpg" class="w-full h-full object-fill" />
      </div>

      <!-- 헤더 -->
      <header class="absolute top-0 w-full flex flex-col">
        <!-- 타이틀 바 -->
        <div class="w-full h-[12vh] bg-[#c3c3c3] flex flex-col relative">
          <!-- 중앙 구분선 -->
          <div class="absolute top-1/2 w-full h-[0.2vh] bg-[#818181] transform -translate-y-1/2"></div>
            
          <!-- 상단 영역 (45%, 중앙 정렬) -->
            <div class="absolute top-1/2 transform -translate-y-full h-[45%] w-full flex justify-between items-center px-[1vh]">
            <!-- 왼쪽: 타이틀 영역 -->
              <div class="flex items-center h-full" @click="openModal('status')">
                <div class="flex items-center h-[90%] px-[1vh] border-t-[0.2vh] border-l-[0.2vh] border-[#ffffff] border-r-[0.2vh] border-b-[0.2vh] border-[#818181] bg-[#c3c3c3]">
                  <img src="@/assets/images/profile/default-image-1.png" class="h-[90%] mr-[1vh]" />
                  <div class="flex flex-col">
                    <span class="text-black text-[1.8vh] font-bold">{{ userInfo.userNickname }}</span>
                    <div class="relative w-full h-[1.2vh] bg-gray-200 rounded-[0.2vh] mt-[0.5vh]">
                      <div class="absolute inset-0 flex items-center justify-center text-black text-[1vh]">
                      Lv. {{ userInfo.level }}
                      </div>
                      <div class="bg-blue-500 h-full" :style="{ width: Math.min(userInfo.experience / 2, 100) + '%' }"></div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 오른쪽: 창 제어 버튼들 -->
              <div class="flex space-x-[0.5vh] h-full items-center">
                <!-- <button class="nes-btn h-[80%] aspect-square flex items-center justify-center text-[2vh] font-bold">_</button> -->
                <button @click="handleCloseClick" class="nes-btn h-[80%] aspect-square flex items-center justify-center text-[2vh] font-bold">×</button>
              </div>
            </div>

          <!-- 하단 영역 (45%, 중앙 정렬) -->
          <div class="absolute top-1/2 h-[45%] w-full flex items-center px-[1%]">
            <div class="relative w-full nes-field flex items-center bg-white border-[0.4%] border-gray-700 px-[2%] py-[1%]" style="border-radius: 1.6%">
              <div class="w-[95%] mx-auto translate-y-[5%]">
                <span class="text-black font-bold nes-text w-full text-center whitespace-nowrap block"
                style="font-size: 100%">
                  <template v-for="(char, index) in 'Fero.From.ISAEKAI.com'" :key="index">
                    <span class="inline-block animate-pixel-wave" 
                        :style="{ 'animation-delay': `${index * 0.1}s` }">
                    {{ char === " " ? "\u00A0" : char }}
                    </span>
                  </template>
                </span>
              </div>
            </div>
          </div>
        </div>
      </header>


      <!-- 캐릭터 -->
      <div class="absolute left-1/2 bottom-[10vh] w-[30vw] h-[30vh] transform -translate-x-1/2 -translate-y-1/2" @click="openModal('character')"></div>
      <div class="absolute left-1/2 bottom-[2vh] transform -translate-x-1/2 w-[30vh] h-[72vh] flex items-center justify-center overflow-hidden cursor-pointer pointer-events-none">
        <img v-if="hair && face && body" :src="face" class="absolute w-[30vh] h-[30vh] top-[16vh] pointer-events-none" />
        <img v-if="hair && face && body" :src="hair" class="absolute w-[30vh] h-[30vh] top-[16vh] pointer-events-none" />
        <img v-if="hair && face && body" :src="body" class="absolute w-[30vh] h-[48vh] bottom-[7vh] pointer-events-none" />
        <div v-if="!face && !hair && !body" class="text-gray-500 text-center">캐릭터 없음</div>
      </div>

      <!-- 작업 표시줄 (푸터) -->
      <footer class="absolute bottom-0 w-full h-[7vh] bg-[#c3c3c3] border-t-2 border-[#ffffff] flex">
        <button class="w-1/5 h-full flex items-center justify-center relative border-t border-l border-[#818181] border-r border-b border-[#ffffff] bg-[#c3c3c3]" @click="openModal('calendar')">
          <div class="flex flex-col items-center w-full">
            <img src="@/assets/images/icon/icon-calendar.png" class="w-[5vh]" />
            <span class="absolute bottom-[1%] w-full text-center text-[1.2vh] drop-shadow-[0_0_2px_rgba(255,255,255,1)] font-semibold">Calendar</span>
          </div>
        </button>

        <button class="w-1/5 h-full flex items-center justify-center relative border-t border-l border-[#818181] border-r border-b border-[#ffffff] bg-[#c3c3c3]" @click="openModal('record')">
          <div class="flex flex-col items-center w-full">
            <img src="@/assets/images/icon/icon-report.png" class="w-[6vh]" />
            <span class="absolute bottom-[1%] w-full text-center text-[1.2vh] drop-shadow-[0_0_2px_rgba(255,255,255,1)] font-semibold">Record</span>
          </div>
        </button>

        <button class="w-1/5 h-full flex items-center justify-center relative border-t border-l border-[#818181] border-r border-b border-[#ffffff] bg-[#c3c3c3]" @click="openModal('fitness')">
          <div class="flex flex-col items-center w-full">
            <img src="@/assets/images/icon/icon-fitness.png" class="w-[7vh]" />
            <span class="absolute bottom-[1%] w-full text-center text-[1.2vh] drop-shadow-[0_0_2px_rgba(255,255,255,1)] font-semibold">Fitness</span>
          </div>
        </button>

        <button class="w-1/5 h-full flex items-center justify-center relative border-t border-l border-[#818181] border-r border-b border-[#ffffff] bg-[#c3c3c3]" @click="openModal('quest')">
          <div class="flex flex-col items-center w-full">
            <img src="@/assets/images/icon/icon-quest.png" class="w-[5vh]" />
            <span class="absolute bottom-[1%] w-full text-center text-[1.2vh] drop-shadow-[0_0_2px_rgba(255,255,255,1)] font-semibold">Quest</span>
          </div>
        </button>

        <button class="w-1/5 h-full flex items-center justify-center relative border-t border-l border-[#818181] border-r border-b border-[#ffffff] bg-[#c3c3c3]" @click="openModal('setting')">
          <div class="flex flex-col items-center w-full">
            <img src="@/assets/images/icon/icon-setting.png" class="w-[5vh]" />
            <span class="absolute bottom-[1%] w-full text-center text-[1.2vh] drop-shadow-[0_0_2px_rgba(255,255,255,1)] font-semibold">Setting</span>
          </div>
        </button>
      </footer>

      <!-- 모달 컴포넌트 -->
      <StatusModal v-if="modals.status" @close-modal="closeModal('status')" @open-modal="openModal('status')"/>
      <SettingModal v-if="modals.setting" @close-modal="closeModal('setting')" @open-modal="openModal('setting')" />
      <CharacterModal v-if="modals.character" @close-modal="closeModal('character')" @open-modal="openModal('character')" />
      <CalendarModal v-if="modals.calendar" @close-modal="closeModal('calendar')" @open-modal="openModal('calendar')" />
      <FitnessModal v-if="modals.fitness" @close-modal="closeModal('fitness')" @open-modal="openModal('fitness')" />
      <RecordModal v-if="modals.record" @close-modal="closeModal('record')" @open-modal="openModal('record')" />
      <QuestModal v-if="modals.quest" @close-modal="closeModal('quest')" @open-modal="openModal('quest')" />

      <!-- 튜토리얼 -->
      <TutorialConfirmModal v-if="showTutorialConfirm" @accept="startTutorial" @reject="rejectTutorial" />
      <MainTutorialComponent v-if="showTutorialComponent" @close="closeTutorial" @open-modal="openModal" />

      <!-- 백그라운드 음성인식 -->
      <SpeechRecognitionHandler @voice-control="modalControl" />

      <!-- 로그아웃 확인 모달 추가 -->
      <SmallBaseModal
        v-if="showLogoutConfirm"
        title="Logout"
        @close-modal="showLogoutConfirm = false"
        @confirm="handleLogout"
      >
        <p>정말 로그아웃 하시겠습니까?</p>
      </SmallBaseModal>
    </div>
  </div>
</template>

<style scoped>
img {
  image-rendering: pixelated;
}
</style>
