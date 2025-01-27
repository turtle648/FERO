<template>
  <div class="main-container">
    <div class="background-wrapper">
      <img class="background-image" src="@/assets/images/background_image.jpg" alt="배경이미지" />
    </div>
  </div>
</template>

<script setup>
// import { ref } from "vue"
</script>

<style scoped>
.main-container {
  width: 100%;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.background-wrapper {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
}

.background-image {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

/* 모바일 */
@media screen and (max-width: 600px) {
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
}

/* Pad */
@media only screen and (min-device-width: 768px) and (max-device-width: 1024px) and (orientation: portrait) {
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
}
</style>
