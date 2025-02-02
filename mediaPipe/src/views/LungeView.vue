<template>
  <div class="exercise-view">
    <div class="header">
      <router-link to="/" class="back-btn">← 돌아가기</router-link>
      <h1>런지</h1>
    </div>
    <LungeCounter />
  </div>
</template>

<script>
import LungeCounter from "../components/LungeCounter.vue";

export default {
  name: "LungeView",
  components: {
    LungeCounter,
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
