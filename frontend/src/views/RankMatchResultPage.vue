<template>
  <div>
    <p>roomId: {{ roomId }}</p>
    <hr />
    <p>peerToken: {{ peerToken }}</p>
    <p>userToken: {{ userToken }}</p>
    <hr />
    <p v-if="result">{{ result }}</p>
    <hr />
    <button @click="exit">[게임 종료]</button>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from "vue";
import { getRouteData } from "@/router/routeData";
import { useUserStore } from "@/stores/store";
import { useRouter } from "vue-router";
import axios from "axios";

const userStore = useUserStore();
const roomId = ref("");
const userToken = ref("");
const peerToken = ref("");
const result = ref("");
const router = useRouter();

const endGame = async () => {
  if (!peerToken.value) {
    result.value = "상대방 토큰이 없습니다. API 호출 중지!";
    return;
  }

  try {
    console.log("API 호출 시작:", {
      gameId: roomId.value,
      opponentToken: peerToken.value,
      userToken: userToken.value,
    });

    const response = await axios.post(
      "https://i12e103.p.ssafy.io:8076/api/v1/matching/endGame",
      {
        gameId: roomId.value,
        opponentToken: peerToken.value,
        userToken: userToken.value,
      }
    );
    result.value = `API 응답: ${JSON.stringify(response.data)}`;
  } catch (error) {
    result.value = `API 오류: ${error.message}`;
  }
};

const exit = () => {
  router.push({ name: "Main" });
};

// `peerToken` 값이 바뀔 때 API 실행
watch(peerToken, (newValue) => {
  if (newValue) {
    console.log("peerToken 설정 완료:", newValue);
    endGame();
  }
});

onMounted(() => {
  const data = getRouteData();
  if (data) {
    roomId.value = data.roomId;
    userToken.value = userStore.accessToken;

    // peerToken이 즉시 존재하면 API 호출
    if (data.peerToken) {
      peerToken.value = data.peerToken;
    }
  }

  console.log("roomId:", roomId.value);
  console.log("peerToken 초기값:", peerToken.value);
  console.log("userToken:", userToken.value);
});
</script>
