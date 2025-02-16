<template>
  <div class="relative bg-white min-h-screen flex flex-col items-center p-4">
    <h1 class="text-2xl font-bold">🎤 음성 인식 테스트</h1>
    <div class="mt-4 p-4 bg-gray-100 rounded shadow-md">
      <p><strong>🎧 현재 상태:</strong> {{ isListening ? "듣는 중" : "대기 중" }}</p>
      <p><strong>🔔 웨이크워드 감지:</strong> {{ isWakeWordDetected ? "✅ 감지됨" : "❌ 대기 중" }}</p>
      <p><strong>📝 인식된 텍스트:</strong> {{ transcript }}</p>
      <p><strong>⏳ 남은 대기 시간:</strong> {{ timer }} 초</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, defineEmits } from "vue";

const emit = defineEmits(["voice-control"]);
emit
const transcript = ref("");
const isListening = ref(false);
const isWakeWordDetected = ref(false);
const timer = ref(0);
let recognition = null;

const wakeWord = "파소콘";
const commands = ["종료","상태","설정","전적","달력","운동","캐릭터","퀘스트"];
const emits = { "종료": "close", "상태": "status", "설정": "setting", "전적": "record", "달력": "calendar", "운동": "fitness", "캐릭터": "character", "퀘스트": "quest" }
const sendEmit = (command) => { console.log("voice-control", command) }

onMounted(() => {
  if (!("webkitSpeechRecognition" in window || "SpeechRecognition" in window)) {
    console.warn("⚠️ 음성 인식을 지원하지 않는 브라우저입니다.");
    return;
  }

  recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
  recognition.continuous = true;
  recognition.interimResults = false; // 🔥 최종 결과만 반영
  recognition.lang = "ko-KR";

  recognition.onstart = () => (isListening.value = true);
  recognition.onend = () => {
    isListening.value = false;
    setTimeout(() => recognition.start(), 500);
  };

  recognition.onerror = (event) => {
    console.error("음성 인식 오류:", event);
    setTimeout(() => recognition.start(), 1000);
  };

  recognition.onresult = (event) => {
    let finalTranscript = event.results[event.results.length - 1][0].transcript.trim();
    transcript.value = normalizeText(finalTranscript);
    processSpeech(transcript.value);
  };

  recognition.start();
});

const processSpeech = (text) => {
  if (isWakeWordDetected.value) {
    checkCommand(text);
    return;
  }

  const words = text.split(/\s+/); // 단어 단위로 분리
  for (const word of words) {
    if (levenshteinDistance(word, wakeWord) <= 2) {
      console.log(`🟢 웨이크워드 "${word}" 감지!`);
      startCommandListening();
      return; // 웨이크워드 감지 시 즉시 종료
    }
  }
};

const checkCommand = (text) => {
  const words = text.split(/\s+/);
  for (const word of words) {
    for (const command of commands) {
      if (levenshteinDistance(word, command) <= 1) {
        console.log(`✅ 명령어 "${word}" 감지됨!`);
        executeCommand(command);
        return;
      }
    }
  }
};

const executeCommand = (command) => {
  sendEmit(emits[command])
};

const startCommandListening = () => {
  console.log("🔔 명령어 입력 대기 시작 (5초)");
  isWakeWordDetected.value = true;
  timer.value = 5;

  const countdown = setInterval(() => {
    timer.value--;
    if (timer.value <= 0) {
      clearInterval(countdown);
      isWakeWordDetected.value = false;
      console.log("❌ 명령어 대기 종료");
    }
  }, 1000);
};

const normalizeText = (text) => text.replace(/\s+/g, " ").trim();

// 🎯 Levenshtein 거리 계산 함수
const levenshteinDistance = (a, b) => {
  const matrix = [];
  const lenA = a.length;
  const lenB = b.length;

  if (!lenA) return lenB;
  if (!lenB) return lenA;

  for (let i = 0; i <= lenA; i++) matrix[i] = [i];
  for (let j = 0; j <= lenB; j++) matrix[0][j] = j;

  for (let i = 1; i <= lenA; i++) {
    for (let j = 1; j <= lenB; j++) {
      if (a.charAt(i - 1) === b.charAt(j - 1)) {
        matrix[i][j] = matrix[i - 1][j - 1];
      } else {
        matrix[i][j] = Math.min(
          matrix[i - 1][j] + 1, // 삭제
          matrix[i][j - 1] + 1, // 삽입
          matrix[i - 1][j - 1] + 1 // 대체
        );
      }
    }
  }
  return matrix[lenA][lenB];
};

onUnmounted(() => {
  recognition?.abort();
});
</script>
