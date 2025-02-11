<template>
  <img class="background-image" src="@/assets/images/background_image2.png" alt="배경이미지" />
  <div class="container absolute top-0 left-0 w-full h-full">
    <div class="header absolute top-0 w-[100vw] h-[7vh] bg-[rgb(194,255,96)] text-white font-bold flex justify-between items-center">
    <!-- 헤더 프로필 -->
    <div class="header-item header-profile w-1/2 h-[7vh] flex items-center" @click="openStatusModal">
      <!-- 프로필 이미지 (정사각형, 작은 사이즈로 동그랗게 만들기) -->
      <img src="@/assets/images/profile/default_profile.png" class="w-[7vh] h-[7vh] rounded-full object-cover">        
      
      <!-- LV 및 게이지 영역 -->
      <div class="info-box ml-4 flex-1 h-full">
        <div class="h-1/2 text-[2vh]">
          {{ nickName }}
        </div>
        <div class="level-gauge relative w-full h-[2vh] bg-gray-200 rounded-full mt-1">
          <!-- 레벨 표시 (게이지 바 중앙) -->
          <div class="absolute inset-0 flex items-center justify-center text-black text-[1.5vh] font-bold">
            Lv. {{ level }}
          </div>
          
          <!-- 경험치에 맞게 게이지 채우기 -->
          <div 
            class="gauge-bar bg-blue-500 h-full rounded-full" 
            :style="{ width: Math.min(exp, 100) + '%' }">
          </div>
        </div>
      </div>
    </div>
  
    <div class="flex justify-between items-center p-[1vh]">
      <!-- 설정 버튼 -->
      <img class="w-[5vh] h-[5vh] rounded-full object-cover" 
        @click="openSettingModal"
        src="@/assets/images/icon/setting.png" alt="" />
    </div>
  </div>
  
  <!-- 알림 아이콘 -->
  <div class="absolute top-[10vh] right-[2vh] w-[4vh] h-[3vh] object-cover" @click="openAlarmModal">
    <img src="@/assets/images/icon/alarm2.png" alt="" />
  </div>
  
    <!-- 캐릭터 -->
    <div class="absolute left-1/2 top-[calc(100vh/1.5)] transform -translate-x-1/2 -translate-y-1/2
      w-[min(45vw,45vh)] h-[min(45vw,45vh)] flex items-center justify-center relative overflow-hidden"
      @click="openCharacterModal">

      <!-- 얼굴 이미지 -->
      <img v-if="hair && face && body" :src="face" class="absolute w-[80%] top-0" />
      <!-- 머리 이미지 -->
      <img v-if="hair && face && body" :src="hair" class="absolute w-[80%] top-0" />
      <!-- 몸 이미지 -->
      <img v-if="hair && face && body" :src="body" class="absolute w-[80%] bottom-0" />

      <!-- 이미지 없을 경우 -->
      <div v-if="!face && !hair && !body" class="text-gray-500 text-center">
        캐릭터 없음
      </div>
    </div>
 
  
  
    <!-- 상태창 -->
    <StatusModal v-if="showStatusModal" @closeStatus="closeStatusModal" />
  
    <!-- 전적창 -->
    <MatchRecordModal v-if="showRecordModal" @closeRecord="closeRecordModal" />
  
    <!-- 설정 -->
    <SettingModal v-if="showSettingModal" @closeSetting="closeSettingModal" @openSetting="openSettingModal" />
  
    <!-- 알림 모달 -->
    <AlarmModal v-if="showAlarmModal" @closeAlarm="closeAlarmModal" @openAlarm="openAlarmModal" />
  
    <!-- 운동 모드 선택 버튼들 -->
    <!-- <div class="exercise-options" v-show="showExerciseOptions"> -->
    <!-- <div class="option-item" @click="handleSoloExercise">혼자하기</div> -->
    <!-- <div class="option-item" @click="handleMultiExercise">같이하기</div> -->
    <!-- </div> -->
  
    <!-- 새로운 모달 추가 -->
    <!-- <AloneModal v-if="showAloneModal" @closeAlone="closeAloneModal" /> -->
    <!-- <WithModal v-if="showWithModal" @closeWith="closeWithModal" /> -->
  
    <div class="footer w-[100vw] h-[7vh]">
      <!-- class명 추가해서 쓰기 -->
      <!-- <div class="grid-item" @click="toggleExerciseOptions">운동</div> -->
  
      <div class="grid-item" @click="openFriendModal">친구</div>
      <div class="grid-item" @click="openCalendarModal">달력</div>
      <div class="grid-item" @click="openFitnessModal">운동</div>
      <div class="grid-item" @click="openRecordModal">전적</div>
      <div class="grid-item">퀘스트</div>
    </div>
  
    <!-- 캐릭터 모달 -->
    <CharacterModal v-if="showCharacterModal" @closeCharacter="closeCharacterModal" />
  
    <!-- 친구 모달 -->
    <FriendListModal v-if="showFriendModal" @closeFriend="closeFriendModal" @openFriend="openFriendModal" />
  
    <!-- 달력 모달 -->
    <CalendarModal v-if="showCalendarModal" @closeCalendar="closeCalendarModal" @openCalendar="openCalendarModal" />
  
    <!-- 운동 모달 -->
    <FitnessModal v-if="showFitnessModal" @closeFitness="closeFitnessModal" @openFitness="openFitnessModal" />
  
    <!-- 퀘스트 모달 -->
  </div>
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
.background-image {
  max-width: 100vw;
  height: 100vh; /* 뷰포트 기준 100% */
  object-fit: cover;
  /* background-size: cover; */
  display: block; /* 이미지가 인라인 요소로 처리X */
  margin: 0 auto; /* 가로 기준 가운데 정렬 */
}

.footer {
  position: absolute;
  bottom: 0%;
  display: grid;
  grid-template-columns: repeat(5, 1fr); /* 열 4개로로 */
  z-index: 1;
  gap: 2%;
  background-color: rgb(255, 255, 255);
  height: 7%;
}

.grid-item {
  background-color: rgba(88, 104, 255, 0.8);
  text-align: center;
  /* border-radius: 50%; */
}

.exercise-options {
  position: absolute;
  bottom: 7%; /* footer의 height와 동일 */
  left: 0;
  width: calc(100% - 20px);
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2%;
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.9);
  z-index: 1;
}

.option-item {
  background-color: rgba(88, 104, 255, 0.8);
  text-align: center;
  padding: 10px;
  cursor: pointer;
}

/* 모바일 */
@media screen and (max-width: 821px) {
  .background-wrapper {
    width: 100%;
    height: 100vh;
  }

  .background-image {
    width: 100%;
    height: 100vh;
    object-fit: cover;
    overflow: hidden;
  }

  .footer {
    grid-template-columns: repeat(5, 1fr);
    gap: 2%;
    padding: 10px;
  }
}

/* 추후 수정필요 */
/* 태블릿
@media screen and (min-device-width: 700px) and (max-device-width: 821px) {
  .background-wrapper {
    width: 100%;
    height: 100vh;
  }

  .background-image {
    width: 100%;
    height: 100vh;
    object-fit: cover;
    overflow: hidden;
  }

  .footer {
    grid-template-columns: repeat(4, 1fr);
    gap: 10px;
    width: calc(100% - 20px);
    padding: 10px;
  }
} */
</style>
