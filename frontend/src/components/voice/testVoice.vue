<template>
  <div>
    <button @click="startListening">🎙️ 음성 인식 시작</button>
    <button @click="stopListening">🛑 음성 인식 중지</button>
    <p>🎤 인식된 단어: {{ transcript }}</p>
  </div>
</template>

<script setup>
import { ref } from "vue";

const transcript = ref("");
const recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();

recognition.continuous = true; // ✅ 계속 듣기
recognition.interimResults = false;
recognition.lang = "ko-KR";

// 결과 이벤트
recognition.onresult = (event) => {
  const lastTranscript = event.results[event.results.length - 1][0].transcript.trim();
  transcript.value = lastTranscript;
  console.log("🎤 인식된 단어:", lastTranscript);
  
  executeCommand(lastTranscript);
};

// 🔥 특정 단어에 따른 명령 실행
const executeCommand = (text) => {
  if (text.includes("설정")) {
    console.log("⚙️ 설정 창 열기!");
  } else if (text.includes("전적")) {
    console.log("📊 전적 창 열기!");
  }
};

// ✅ 음성 인식 종료 시 자동 재시작
recognition.onend = () => {
  console.log("🔄 음성 인식이 종료됨, 다시 시작!");
  recognition.start();
};

// 오류 이벤트 확인
recognition.onerror = (event) => {
  console.error("🚨 음성 인식 오류:", event.error);
};

// 🎙️ 음성 인식 시작
const startListening = () => {
  try {
    recognition.start();
    console.log("🎙️ 음성 인식 시작!");
  } catch (error) {
    console.error("🚨 음성 인식 시작 중 오류 발생:", error);
  }
};

// 🛑 음성 인식 중지
const stopListening = () => {
  recognition.stop();
  console.log("🛑 음성 인식 중지!");
};
</script>
