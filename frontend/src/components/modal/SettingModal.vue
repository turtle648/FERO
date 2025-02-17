<!-- components/modal/SettingModal.vue -->
<template>
  <BaseModal title="Settings" @close-modal="$emit('close-modal')">
    <div class="flex flex-col h-full">
      <!-- 메인 메뉴 리스트 -->
      <ul class="flex-1 flex justify-center items-center">
        <!-- <li class="text-gray-700">회원정보</li> -->
        <button class="text-gray-700 nes-btn is-error">회원탈퇴</button>

        <!-- 버튼 그룹 -->
        <!-- <li class="space-y-2"> -->
        <!-- <button @click="goToStart" class="w-full py-2 px-4 bg-red-500 text-white rounded-lg hover:bg-red-600 transition-colors">로그아웃</button> -->

        <!-- <button @click="checkUserNickname" class="w-full py-2 px-4 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors">본인정보조회</button> -->

        <!-- <button @click="checkUserLevel" class="w-full py-2 px-4 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors">본인레벨조회</button> -->

        <!-- <button @click="checkUserExperience" class="w-full py-2 px-4 bg-blue-500 text-white rounded-lg hover:bg-blue-600 transition-colors">본인경험치조회</button> -->

        <!-- <button @click="routeRTC" class="w-full py-2 px-4 bg-green-500 text-white rounded-lg hover:bg-green-600 transition-colors">테스트webRTC</button> -->
        <!-- </li> -->
      </ul>

      <!-- 크레딧 섹션 -->
      <section class="mt-auto pt-4 border-t border-gray-200">
        <h2 class="text-lg font-bold text-gray-700 mb-2">Music Credits</h2>
        <p class="text-sm text-gray-600">"Getting it Done" Kevin MacLeod (incompetech.com) Licensed under Creative Commons: By Attribution 3.0</p>
      </section>
    </div>
  </BaseModal>
</template>

<script setup>
// import { useRouter } from "vue-router"
// import { useUserStore } from "@/stores/store"
// import { useUserDataStore } from "@/stores/userDataStore"
import BaseModal from "./BaseModal.vue"

// const router = useRouter()
// const userStore = useUserStore()
// const userDataStore = useUserDataStore()

defineEmits(["close-modal"])

// 라우팅 함수
// const routeRTC = () => {
//   router.push({ name: "VideoRoom" })
// }

// 로그아웃 함수
// const goToStart = async () => {
//   await userStore.logOut()
// }

// 사용자 정보 관련 함수들
// const checkUserNickname = () => {
//   userDataStore.checkUserInfo()
// }
// const checkUserLevel = () => {
//   userDataStore.checkUserLevel()
// }
// const checkUserExperience = () => {
//   userDataStore.checkUserExperience()
// }
</script>
