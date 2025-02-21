<template>
  <div class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50">
    <div class="bg-white w-[30vh] p-6 rounded-lg shadow-lg">
      <p class="sm:text-sm text-lg mb-4 font-dgm">메인페이지에 처음 입장하셨다면, 페이지에 대해서 안내해드릴까요?</p>
      <div class="flex justify-center gap-4">
        <button class="font-dgm nes-btn is-primary" @click="$emit('accept')">수락</button>
        <button class="font-dgm nes-btn is-error" @click="$emit('reject')">거절</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits } from "vue"

defineEmits(["accept", "reject"])
</script>
