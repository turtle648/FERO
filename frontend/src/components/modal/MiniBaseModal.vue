<template>
  <Teleport to="body">
    <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center" @click.self="$emit('close-modal')">
      <!-- 모달 본체: 화면 중앙 70% x 20% -->
      <div class="absolute bg-white rounded-lg shadow-lg flex flex-col" style="width: 70%; height: 20%; top: 50%; left: 50%; transform: translate(-50%, -50%)" @click.stop>
        <!-- 헤더 영역 -->
        <div
          class="relative w-full h-[8vh] flex justify-between items-center pl-[4vw] pr-[4vw] text-white text-xl font-bold"
          :style="{
            backgroundImage: `url(${require('@/assets/images/modal_header_background.png')})`,
            backgroundSize: '100% 100%',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: 'center',
          }"
        >
          <h2 class="font-bold" style="font-size: 2vh">{{ title }}</h2>
        </div>

        <!-- 컨텐츠 영역 -->
        <div
          class="flex-1 flex flex-col items-center justify-center"
          :style="{
            backgroundImage: `url(${require('@/assets/images/modal_body_background.png')})`,
            backgroundSize: '100% 100%',
            backgroundRepeat: 'no-repeat',
            backgroundPosition: 'center',
          }"
        >
          <div class="text-center mb-6 font-dgm">
            <slot></slot>
          </div>
          <!-- 버튼 컨테이너: 중앙 기준 좌우 대칭 -->
          <div class="w-full flex justify-center">
            <div class="w-[80%] flex">
              <!--  버튼 -->
              <button
                class="font-dgm text-bold nes-btn is-primary whitespace-nowrap flex items-center justify-center ml-auto mb-5 z-50"
                :style="{
                  width: '40%',
                  height: '70%',
                  fontSize: 'clamp(1rem, 3vw, 1.5rem)',
                }"
                @click="$emit('close-modal')"
              >
                확인
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: "확인",
  },
})

defineEmits(["close-modal"])
</script>
