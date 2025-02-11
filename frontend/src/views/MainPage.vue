<template>
  <!-- 배경 이미지 -->
  <div class="absolute inset-0 flex items-center justify-center overflow-hidden">
    <img src="@/assets/images/background_image3.png" class="w-full h-full object-fill" />
  </div>

  <!-- 헤더 -->
  <header class="absolute top-0 w-full h-[7vh] bg-green-400 text-white font-bold flex justify-between items-center px-4">
    <div class="flex items-center w-1/2 cursor-pointer" @click="openStatusModal">
      <img src="@/assets/images/profile/default_profile.png" class="w-[7vh] h-[7vh] rounded-full object-cover" />
      <div class="ml-4 flex-1">
        <div class="text-[2vh]">{{ nickName }}</div>
        <div class="relative w-full h-[2vh] bg-gray-200 rounded-full mt-1">
          <div class="absolute inset-0 flex items-center justify-center text-black text-[1.5vh] font-bold">
            Lv. {{ level }}
          </div>
          <div class="bg-blue-500 h-full rounded-full" :style="{ width: Math.min(exp, 100) + '%' }"></div>
        </div>
      </div>
    </div>
    <img class="w-[5vh] h-[5vh] object-cover cursor-pointer" @click="openSettingModal" src="@/assets/images/icon/setting.png" alt="설정" />
  </header>

  <!-- 알림 아이콘 -->
  <div class="absolute top-[10vh] right-[2vh] w-[4vh] h-[3vh] cursor-pointer" @click="openAlarmModal">
    <img src="@/assets/images/icon/alarm2.png" alt="알림" />
  </div>

  <!-- 캐릭터 -->
  <div class="absolute left-1/2 bottom-[6vh] transform -translate-x-1/2 w-[40vh] h-[50vh] flex items-center justify-center overflow-hidden cursor-pointer" @click="openCharacterModal">
    <img v-if="hair && face && body" :src="face" class="absolute w-[40vh] h-[40vh] top-0" />
    <img v-if="hair && face && body" :src="hair" class="absolute w-[40vh] h-[40vh] top-0" />
    <img v-if="hair && face && body" :src="body" class="absolute w-[40vh] h-[40vh] bottom-0" />
    <div v-if="!face && !hair && !body" class="text-gray-500 text-center">캐릭터 없음</div>
  </div>

  <!-- 하단 메뉴 -->
  <div class="footer absolute bottom-0 w-full h-[7vh] grid grid-cols-5 bg-white text-center">
    <div class="footer-btn bg-blue-500 text-white cursor-pointer" @click="openFriendModal">친구</div>
    <div class="footer-btn bg-blue-500 text-white cursor-pointer" @click="openCalendarModal">달력</div>
    <div class="footer-btn bg-blue-500 text-white cursor-pointer" @click="openFitnessModal">운동</div>
    <div class="footer-btn bg-blue-500 text-white cursor-pointer" @click="openRecordModal">전적</div>
    <div class="footer-btn bg-blue-500 text-white cursor-pointer">퀘스트</div>
  </div>

  <!-- 모달 컴포넌트 -->
  <StatusModal v-if="showStatusModal" @closeStatus="closeStatusModal" @openStatus="openStatusModal"/>
  <SettingModal v-if="showSettingModal" @closeSetting="closeSettingModal" @openSetting="openSettingModal" />
  <AlarmModal v-if="showAlarmModal" @closeAlarm="closeAlarmModal" @openAlarm="openAlarmModal" />
  <CharacterModal v-if="showCharacterModal" @closeCharacter="closeCharacterModal" @openCharacter="openCharacterModal" />
  <FriendListModal v-if="showFriendModal" @closeFriend="closeFriendModal" @openFriend="openFriendModal" />
  <CalendarModal v-if="showCalendarModal" @closeCalendar="closeCalendarModal" @openCalendar="openCalendarModal" />
  <FitnessModal v-if="showFitnessModal" @closeFitness="closeFitnessModal" @openFitness="openFitnessModal" />
  <MatchRecordModal v-if="showRecordModal" @closeRecord="closeRecordModal" @openRecord="openRecordModal" />
</template>

<script setup>
import { ref, onMounted } from "vue"
import { useUserStore } from "@/stores/store"
import { useUserDataStore } from "@/stores/userDataStore"
import { assets } from '@/assets.js'

const userStore = useUserStore()
const userDataStore = useUserDataStore()
const nickName = ref('')
const level = ref('')
const exp = ref('')
const hair = ref('')
const face = ref('')
const body = ref('')

// 튜토리얼 여부 확인
import { useMainStore } from "@/stores/mainStore"
const mainStore = useMainStore()

onMounted(async () => {
  await mainStore.loadTutorial()
})

// 상태창 관련 변수 및 함수
import StatusModal from "@/components/modal/StatusModal.vue"
const showStatusModal = ref(false)
const openStatusModal = () => {
  if (!isAnyModalOpen()) {
    showStatusModal.value = true
  }
}
const closeStatusModal = () => {
  showStatusModal.value = false
}

// 전적창 관련 변수 및 함수
import MatchRecordModal from "@/components/modal/MatchRecordModal.vue"
const showRecordModal = ref(false)
const openRecordModal = () => {
  if (!isAnyModalOpen()) {
    showRecordModal.value = true
  }
}
const closeRecordModal = () => {
  showRecordModal.value = false
}

// 설정 모달
import SettingModal from "@/components/modal/SettingModal.vue"

const showSettingModal = ref(false)
const openSettingModal = () => {
  if (!isAnyModalOpen()) {
    showSettingModal.value = true
  }
}
const closeSettingModal = () => {
  showSettingModal.value = false
}

// 친구리스트 모달
import FriendListModal from "@/components/modal/FriendListModal.vue"

const showFriendModal = ref(false)
const openFriendModal = () => {
  if (!isAnyModalOpen()) {
    showFriendModal.value = true
  }
}
const closeFriendModal = () => {
  showFriendModal.value = false
}

// 달력 모달
import CalendarModal from "@/components/modal/CalendarModal.vue"

const showCalendarModal = ref(false)
const openCalendarModal = () => {
  if (!isAnyModalOpen()) {
    showCalendarModal.value = true
  }
}
const closeCalendarModal = () => {
  showCalendarModal.value = false
}

// 운동 모달
import FitnessModal from "@/components/modal/FitnessModal.vue"

const showFitnessModal = ref(false)
const openFitnessModal = () => {
  if (!isAnyModalOpen()) {
    showFitnessModal.value = true
  }
}
const closeFitnessModal = () => {
  showFitnessModal.value = false
}

// 알림 모달
import AlarmModal from "@/components/modal/AlarmModal.vue"

const showAlarmModal = ref(false)
const openAlarmModal = () => {
  if (!isAnyModalOpen()) {
    showAlarmModal.value = true
  }
}
const closeAlarmModal = () => {
  showAlarmModal.value = false
}

// 캐릭터 모달
import CharacterModal from "@/components/modal/CharacterModal.vue"
const showCharacterModal = ref(false)
const openCharacterModal = () => {
  if (!isAnyModalOpen()) {
    showCharacterModal.value = true
  }
}
const closeCharacterModal = () => {
  showCharacterModal.value = false

}
// 현재 활성화된 모든 모달 상태를 확인하는 함수
const isAnyModalOpen = () => {
  return showStatusModal.value || showRecordModal.value || showSettingModal.value || showAlarmModal.value || showFriendModal.value || showCalendarModal.value || showFitnessModal.value
}

onMounted(async () => {
  try {
    userStore
    // userDataStore.userCharacter = [hairIndex, faceIndex, bodyIndex]
    const [hairIndex, faceIndex, bodyIndex] = userDataStore.userCharacter    
    // Base64 이미지 로드
    hair.value = assets.hair[hairIndex]?.[0] || ""
    face.value = assets.face[faceIndex]?.[0] || ""
    body.value = assets.body[bodyIndex]?.[0] || ""

    nickName.value = await userDataStore.checkUserNickname()
    level.value = await userDataStore.checkUserLevel()
    exp.value = await userDataStore.checkUserExperience()
    
    await mainStore.fetchData()
  } catch (error) {
    console.log("User Data 로드 중 오류 발생:", error)
  }
})
</script>

<style scoped>
.footer {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  background-color: white;
  text-align: center;
  height: 7vh;
}
.footer-btn {
  background-color: rgba(88, 104, 255, 0.8);
  color: white;
  padding: 1vh;
  cursor: pointer;
}
</style>
