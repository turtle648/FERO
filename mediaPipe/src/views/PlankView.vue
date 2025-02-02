<template>
  <div class="exercise-view">
    <div class="header">
      <router-link to="/" class="back-btn">← 돌아가기</router-link>
      <h1>플랭크</h1>
    </div>
    <PlankCounter />
  </div>
</template>

<script>
import PlankCounter from "../components/PlankCounter.vue";

export default {
  name: "PlankView",
  components: {
    PlankCounter,
  },
};
</script>

<style scoped>
.exercise-view {
  padding: 20px;
}

.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.back-btn {
  padding: 8px 16px;
  text-decoration: none;
  color: #666;
  margin-right: 20px;
}

h1 {
  margin: 0;
}
</style>
