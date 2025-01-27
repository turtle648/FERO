<template>
  <img class="background-image" src="@/assets/images/background_image.jpg" alt="배경이미지" />
  <div class="container" v-if="!isDesktop">
    <div class="header">
      <div class="header-item level" @click="openStatusModal">레벨, 경험치 (상태창)</div>
      <div class="header-item experience">설정</div>
      <div class="header-item quest">퀘스트</div>
    </div>

    <!-- 상태창 -->
    <StatusModal v-if="showStatusModal" @closeStatus="closeStatusModal" />

    <div class="footer">
      <!-- class명 추가해서 쓰기 -->
      <div class="grid-item">운동</div>
      <div class="grid-item">달력</div>
      <div class="grid-item">친구</div>
      <div class="grid-item">????</div>
    </div>
  </div>
  <div v-else class="qr-code">
    <h1>(나중에 큐알 들어갈 자리)</h1>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue"

const isDesktop = ref(false)

const checkScreenSize = () => {
  isDesktop.value = window.innerWidth > 821
}

onMounted(() => {
  checkScreenSize()
  window.addEventListener("resize", checkScreenSize)
})

onUnmounted(() => {
  window.removeEventListener("resize", checkScreenSize)
})

// 상태창 관련 변수 및 함수
import StatusModal from '@/components/modal/StatusModal.vue'
const showStatusModal = ref(false)
const openStatusModal = () => { showStatusModal.value = true }
const closeStatusModal = () => { showStatusModal.value = false}

</script>

<style scoped>
.background-image {
  max-width: 100%;
  height: 100vh; /* 뷰포트 기준 100% */
  object-fit: cover;
  display: block; /* 이미지가 인라인 요소로 처리X */
  margin: 0 auto; /* 가로 기준 가운데 정렬 */
}

.footer {
  position: absolute;
  bottom: 0%;
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 열 4개로로 */
  z-index: 1;
  gap: 2%;
  background-color: rgb(255, 255, 255);
  height: 5%;
  width: calc(100% - 20px);
}

.grid-item {
  background-color: rgba(88, 104, 255, 0.8);
  text-align: center;
  /* border-radius: 50%; */
}

.header {
  display: flex;
  top: 0;
  left: 0;
  z-index: 2;
  justify-content: space-around;
  width: 100%;
  height: 5vh;
  position: absolute;
}

.header-item {
  background-color: rgb(194, 255, 96);
  flex: 1;
}

.qr-code {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 2;
  text-align: center;
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
    grid-template-columns: repeat(4, 1fr);
    gap: 2%;
    width: calc(100% - 20px);
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
