<template>
    <div class="hidden"></div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, defineEmits } from "vue";

const emit = defineEmits(["voice-control"]);
const transcript = ref("");
const isListening = ref(false);
const isWaitingCommand = ref(false);
const collectedSpeech = ref(""); // 5초 동안 말한 내용 저장
const timer = ref(0);
let recognition = null;
let wakeUpTimer = null;

const commands = {
  "종료": () => emit("voice-control", "close"),
  "상태": () => emit("voice-control", "status"),
  "설정": () => emit("voice-control", "setting"),
  "전적": () => emit("voice-control", "record"),
  "친구": () => emit("voice-control", "friend"),
  "달력": () => emit("voice-control", "calendar"),
  "운동": () => emit("voice-control", "fitness"),
  "알림": () => emit("voice-control", "alarm"),
  "캐릭터": () => emit("voice-control", "character"),
  "퀘스트": () => emit("voice-control", "quest"),
};

const wakeWords = [
  "파소콩", "파소꽁", "파소컹", "파소콤", "파소꼼", "파소컴",
  "파서콩", "파서꽁", "파서컹", "파서콤", "파서꼼", "파서컴",
  "빠소콩", "빠소꽁", "빠소컹", "빠소콤", "빠소꼼", "빠소컴",
  "빠서콩", "빠서꽁", "빠서컹", "빠서콤", "빠서꼼", "빠서컴",
  "하소꽁", "하소콩", "하소컹", "하소콤", "하소꼼", "하소컴",
  "하서콩", "하서꽁", "하서컹", "하서콤", "하서꼼", "하서컴"
];

const startRecognition = () => {
  if (isListening.value) return;
  isListening.value = true;
  recognition.start();
};

const stopRecognition = () => {
  if (!isListening.value) return;
  isListening.value = false;
  recognition.stop();
};

onMounted(async () => {
  if (!("webkitSpeechRecognition" in window || "SpeechRecognition" in window)) {
    console.warn("⚠️ 음성 인식을 지원하지 않는 브라우저입니다.");
    return;
  }

  recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)();
  recognition.continuous = true;
  recognition.interimResults = true;
  recognition.lang = "ko-KR";
  recognition.maxAlternatives = 5;

  recognition.onstart = () => (isListening.value = true);

  recognition.onend = () => {
    isListening.value = false;
    setTimeout(startRecognition, 500);
  };

  recognition.onerror = (event) => {
    console.warn("음성 인식 오류:", event);
    setTimeout(startRecognition, 1000);
  };

  recognition.onresult = (event) => {
    let interimTranscript = "";
    for (let i = event.resultIndex; i < event.results.length; i++) {
      interimTranscript += event.results[i][0].transcript.trim() + " ";
    }
    transcript.value = normalizeText(interimTranscript);
    processSpeech(transcript.value);
  };

  try {
    await navigator.mediaDevices.getUserMedia({
      audio: { noiseSuppression: false, echoCancellation: false },
    });
    console.log("🎧 노이즈 감소 적용 완료");
  } catch (error) {
    console.warn("🎧 노이즈 감소 적용 실패:", error);
  }

  startRecognition();
});

const processSpeech = (text) => {
  if (isWaitingCommand.value) {
    collectedSpeech.value += " " + text;
    return;
  }
  if (detectWakeWord(text)) {
    console.log("🟢 웨이크워드 감지!");
    startCommandListening();
  }
};

const detectWakeWord = (text) => wakeWords.some((word) => text.includes(word));

const startCommandListening = () => {
  console.log("🔔 명령어 입력 대기 시작 (5초)");
  collectedSpeech.value = "";
  isWaitingCommand.value = true;
  timer.value = 5;

  const countdown = setInterval(() => {
    timer.value--;
    if (timer.value <= 0) clearInterval(countdown);
  }, 1000);

  clearTimeout(wakeUpTimer);
  wakeUpTimer = setTimeout(() => {
    executeCommand(collectedSpeech.value);
    isWaitingCommand.value = false;
  }, 5000);
};

const executeCommand = (text) => {
  let matchedCommand = Object.keys(commands).find((key) => text.includes(key));
  if (matchedCommand) {
    console.log(`✅ 명령어 실행: ${matchedCommand}`);
    commands[matchedCommand]();
  } else {
    console.log("⚠️ 인식된 명령어가 없습니다.");
  }
};

const normalizeText = (text) => text.replace(/\s+/g, " ").trim();

onUnmounted(() => {
  stopRecognition();
  clearTimeout(wakeUpTimer);
});
</script>
