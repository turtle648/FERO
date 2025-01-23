<template>
  <div>
    <img src="@/assets/backgroundImage.png" alt="배경이미지" />
  </div>
</template>

<script setup></script>

<style scoped>
div {
  width: 100%;
  height: 100%;
}
img {
  width: 100%;
  height: 100%;
  border: 1px solid black;
  display: flex;
  justify-content: center;
  align-items: center;
}

@media screen and (max-width: 600px) {
  img {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
}
</style>
