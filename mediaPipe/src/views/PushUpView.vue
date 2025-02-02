<!-- src/views/PushUpView.vue -->
<template>
  <div class="exercise-view">
    <div class="header">
      <router-link to="/" class="back-btn">← 돌아가기</router-link>
      <h1>팔굽혀펴기</h1>
    </div>
    <PushUpCounter />
  </div>
</template>

<script>
import PushUpCounter from "../components/PushUpCounter.vue";

export default {
  name: "PushUpView",
  components: {
    PushUpCounter,
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
