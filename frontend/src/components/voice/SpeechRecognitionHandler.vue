<template>
    <div class="hidden"></div> <!-- UI 요소 없음 -->
  </template>
  
  <script setup>
  import { ref, onMounted, onUnmounted, defineEmits } from "vue"
  
  const emit = defineEmits(["voice-control"])
  
  const transcript = ref("") // 현재 인식된 텍스트
  const isListening = ref(false) // 음성 인식 상태
  const isWaitingCommand = ref(false) // 명령어 대기 상태
  const timer = ref(0) // 5초 카운트다운
  let recognition = null
  let wakeUpTimer = null
  
  // 🔹 실행할 명령어 목록 
  const commands = {
    "상태": () => emit("voice-control", "status"),
    "설정": () => emit("voice-control", "setting"),
    "전적": () => emit("voice-control", "record"),
    "친구": () => emit("voice-control", "friend"),
    "달력": () => emit("voice-control", "calendar"),
    "운동": () => emit("voice-control", "fitness"),
    "알림": () => emit("voice-control", "alarm"),
    "캐릭터": () => emit("voice-control", "character"),
    "퀘스트": () => emit("voice-control", "quest"),    
  }
  
  // 🔹 음성 인식 초기화
  onMounted(() => {
    if (!("webkitSpeechRecognition" in window || "SpeechRecognition" in window)) {
      console.warn("⚠️ 음성 인식을 지원하지 않는 브라우저입니다.")
      return
    }
  
    recognition = new (window.SpeechRecognition || window.webkitSpeechRecognition)()
    recognition.continuous = true
    recognition.interimResults = false
    recognition.lang = "ko-KR"
  
    recognition.onstart = () => isListening.value = true
    recognition.onend = () => isListening.value = false
    recognition.onerror = (event) => console.error("음성 인식 오류:", event)
  
    recognition.onresult = (event) => {
      const finalTranscript = event.results[event.results.length - 1][0].transcript.trim()
      transcript.value = finalTranscript
      processSpeech(finalTranscript)
    }
  
    recognition.start() // 🔹 백그라운드에서 계속 듣기
  })
  
  // 🔹 "헤이" 또는 "hey" 감지 → 5초간 명령어 대기
  const processSpeech = (text) => {
    if (isWaitingCommand.value) return // 이미 대기 중이면 무시
  
    if (/(헤이|해이|hey|hay|Hey|Hay)/.test(text)) {
      startCommandListening()
    }
  }
  
  // 🔹 5초간 명령어 입력 대기
  const startCommandListening = () => {
    console.log("🔔 명령어 입력 대기 시작 (5초)")
    transcript.value = "" // 기존 텍스트 초기화
    isWaitingCommand.value = true
    timer.value = 5 // 5초 타이머 시작
  
    const countdown = setInterval(() => {
      timer.value--
      if (timer.value <= 0) clearInterval(countdown)
    }, 1000)
  
    clearTimeout(wakeUpTimer)
    wakeUpTimer = setTimeout(() => {
      executeCommand(transcript.value)
      isWaitingCommand.value = false
    }, 5000)
  }
  
  // 🔹 명령어 실행
  const executeCommand = (text) => {
    for (const key in commands) {
      if (text.includes(key)) {
        commands[key]() // 
        return
      }
    }
    console.log("⚠️ 인식된 명령어가 없습니다")
  }
  
  // 🔹 컴포넌트 제거 시 음성 인식 중지
  onUnmounted(() => {
    recognition?.abort()
    clearTimeout(wakeUpTimer)
  })
  </script>
  