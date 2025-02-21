<template>
  <BaseModal title="Settings" @close-modal="$emit('close-modal')">
    <div class="flex flex-col h-full">
      <!-- 메인 메뉴 리스트 -->
      <ul class="flex-1 flex justify-center items-center">
        <button class="text-gray-700 nes-btn is-error w-[30vh]" @click="openConfirmModal">회원탈퇴</button>
      </ul>

      <!-- 크레딧 섹션 -->
      <section class="mt-auto pt-4 border-t border-gray-200">
        <h2 class="text-lg font-bold text-gray-700 mb-2">Music Credits</h2>
        <p class="text-sm text-gray-600">"Getting it Done" Kevin MacLeod (incompetech.com) Licensed under Creative Commons: By Attribution 3.0</p>
      </section>
    </div>

    <!-- 회원탈퇴 확인 모달 -->
    <SmallBaseModal v-if="isConfirmModalOpen" title="탈퇴 확인" @close-modal="closeConfirmModal" @confirm="handleConfirm">
      <div class="text-center">
        <p class="text-gray-700 mb-4">탈퇴하시겠습니까?</p>
      </div>
    </SmallBaseModal>
  </BaseModal>
</template>

<script setup>
import { ref } from "vue"
import BaseModal from "./BaseModal.vue"
import SmallBaseModal from "./SmallBaseModal.vue"
import { useRouter } from "vue-router"
import axios from "axios"
import { useUserStore } from "@/stores/store"

const userStore = useUserStore()
const router = useRouter()
const isConfirmModalOpen = ref(false)

// Open confirmation modal
const openConfirmModal = () => {
  isConfirmModalOpen.value = true
}

// Close confirmation modal
const closeConfirmModal = () => {
  isConfirmModalOpen.value = false
}

// Handle confirm action from SmallBaseModal
const handleConfirm = async () => {
  try {
    // Send PUT request to backend for account deletion
    const token = userStore.accessToken

    const response = await axios.put("https://i12e103.p.ssafy.io:8076/api/v1/auth/delete", {
      headers: {
        Authorization: `Bearer ${token}`, // 헤더에 토큰 추가
        "Content-Type": "application/json", // JSON 형식 명시
      },
    })
    console.log("회원탈퇴 성공:", response.data)

    alert("회원탈퇴가 완료되었습니다.")

    closeConfirmModal()
    $emit("close-modal")

    router.push({ name: "Start" })
  } catch (error) {
    console.error("회원탈퇴 실패:", error)
    alert("회원탈퇴 중 오류가 발생했습니다. 다시 시도해주세요.")
  }
}

defineEmits(["close-modal"])
</script>
