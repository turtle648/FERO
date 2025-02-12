<script setup>
import { ref, onMounted, onUnmounted } from "vue";

const transcript = ref(""); // 현재 인식된 텍스트
const isListening = ref(false); // 음성 인식 상태
const isWaitingCommand = ref(false); // 명령어 대기 상태
const timer = ref(0);
let recognition = null;
let wakeUpTimer = null;

// 🔹 명령어 목록
const commands = {
  "상태창": () => console.log("✅ 상태창이 열렸습니다."),
  "설정": () => console.log("✅ 설정 메뉴가 열렸습니다."),
  "종료": () => console.log("✅ 앱이 종료됩니다."),
};

// 🔹 음성 인식 초기화
onMounted(() => {
  if ("webkitSpeechRecognition" in window || "SpeechRecognition" in window) {
    recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
    recognition.continuous = true;
    recognition.interimResults = true;
    recognition.lang = "ko-KR";

    recognition.onstart = () => (isListening.value = true);
    recognition.onend = () => (isListening.value = false);
    recognition.onerror = (event) => console.error("음성 인식 오류:", event);

    recognition.onresult = (event) => {
      let finalTranscript = "";
      for (let i = event.resultIndex; i < event.results.length; i++) {
        finalTranscript += event.results[i][0].transcript;
      }
      transcript.value = finalTranscript;
      processSpeech(finalTranscript);
    };

    recognition.start(); // 🔹 백그라운드에서 계속 듣기
  } else {
    console.warn("음성 인식을 지원하지 않는 브라우저입니다.");
  }
});

// 🔹hey 감지 → 5초간 명령어 대기
const processSpeech = (text) => {
  if (isWaitingCommand.value) return; // 이미 대기 중이면 무시

  if (text.includes("hey") || text.includes("Hey") || text.includes("헤이") || text.includes("해이")) {
    startCommandListening();
  }
};

// 🔹 버튼으로 명령어 대기 시작
const startCommandListening = () => {
  console.log("🔔 명령어 입력 대기 시작 (5초)");
  transcript.value = ""; // 기존 텍스트 초기화
  isWaitingCommand.value = true;
  timer.value = 5; // 5초 타이머 시작

  const countdown = setInterval(() => {
    timer.value--;
    if (timer.value <= 0) {
      clearInterval(countdown);
    }
  }, 1000);

  clearTimeout(wakeUpTimer);
  wakeUpTimer = setTimeout(() => {
    executeCommand(transcript.value);
    isWaitingCommand.value = false;
  }, 5000);
};

// 🔹 명령어 실행
const executeCommand = (text) => {
  for (const key in commands) {
    if (text.includes(key)) {
      commands[key]();
      return;
    }
  }
  console.log("⚠️ 인식된 명령어가 없습니다.");
};

// 🔹 컴포넌트 제거 시 음성 인식 중지
onUnmounted(() => {
  recognition?.abort();
  clearTimeout(wakeUpTimer);
});
</script>

<template>
  <div class="p-4">
    <h1 class="text-xl font-bold">🎤 음성 인식 시스템</h1>
    <p class="mt-2 border p-2 min-h-[50px]">{{ transcript }}</p>
    
    <button 
      @click="startCommandListening"
      class="bg-blue-500 text-white px-4 py-2 rounded mt-4"
    >
      🎤 명령어 입력 시작
    </button>

    <p v-if="isWaitingCommand" class="text-red-500 font-bold mt-2">
      명령어 대기 중... ({{ timer }}초)
    </p>

    <p class="text-sm text-gray-500">"헤이 파소콩" 또는 버튼 클릭 → 5초간 명령어 입력 가능</p>
  </div>
</template>
