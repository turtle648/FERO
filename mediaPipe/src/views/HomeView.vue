<template>
  <div class="home">
    <h1 class="title">운동 선택</h1>
    <div class="exercise-buttons">
      <router-link to="/pushup">
        <button class="exercise-btn">팔굽혀펴기</button>
      </router-link>
      <router-link to="/lunge">
        <button class="exercise-btn">런지</button>
      </router-link>
      <router-link to="/plank">
        <button class="exercise-btn">플랭크</button>
      </router-link>
      <router-link to="/squat">
        <button class="exercise-btn">스쿼트</button>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomeView",
};
</script>

<style scoped>
.home {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  padding: 20px;
}

.title {
  font-size: 2.5rem;
  margin-bottom: 2rem;
  color: #333;
}

.exercise-buttons {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  width: 100%;
  max-width: 300px;
}

.exercise-btn {
  width: 100%;
  padding: 1rem;
  font-size: 1.2rem;
  border: none;
  border-radius: 8px;
  background-color: #4caf50;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.exercise-btn:hover {
  background-color: #45a049;
}
</style>
