<template>
  <div class="main-container">
    <div class="background-wrapper">
      <img class="background-image" src="@/assets/images/backgroundImage.png" alt="배경이미지" />
      <img class="calendar" src="@/assets/images/tmpImage.png" alt="임시달력" />
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.main-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.background-wrapper {
  position: relative;
  width: 27%;
  height: 100%;
}

.background-image {
  width: 100%;
  height: 100%;
}

.calendar {
  width: 30%;
  position: absolute;
  left: 0;
  top: 20%;
}

/* 반응형 */
@media screen and (max-width: 600px) {
  .background-wrapper {
    width: 100%;
    height: auto;
  }

  .background-image {
    width: 100%;
    height: auto;
  }

  .calendar {
    width: 25%;
    top: 20%;
  }
}
</style>
