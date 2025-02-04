<template>
  <div class="tutorial-page">
    <h1>스쿼트 튜토리얼</h1>
    <!-- 튜토리얼 내용 -->
    <div class="time"></div>
    <div class="count"></div>

    <button @click="completeTutorial">튜토리얼 완료</button>
  </div>
  <div class="desciption"></div>
</template>

<script setup>
import { useRouter } from "vue-router"

const router = useRouter()

const completeTutorial = () => {
  // 여기에 튜토리얼 완료 로직 추가
  router.push("/main")
}
</script>

<style scoped>
.tutorial-page {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border: 1px solid black;
  position: relative;
}

.desciption {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1;
}
</style>
