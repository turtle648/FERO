<template>
    <form @submit.prevent="handleSubmit" class="space-y-4 p-4">
      <div class="space-y-2">
        <LabelAtom htmlFor="id" required>아이디</LabelAtom>
        <InputAtom
          id="id"
          v-model="form.id"
          type="text"
          placeholder="아이디를 입력하세요"
          required
          :error="errors.id"
        />
      </div>
      <div class="space-y-2">
        <LabelAtom htmlFor="password" required>비밀번호</LabelAtom>
        <InputAtom
          id="password"
          v-model="form.password"
          type="password"
          placeholder="비밀번호를 입력하세요"
          required
          :error="errors.password"
        />
      </div>
      <ButtonAtom
        type="submit"
        variant="primary"
        class="w-full mt-4"
      >
        로그인
      </ButtonAtom>
    </form>
  </template>
  
  <script setup>
  import { ref, defineEmits } from 'vue'
  import LabelAtom from '@/components/atoms/LabelAtom.vue'
  import InputAtom from '@/components/atoms/InputAtom.vue'
  import ButtonAtom from '@/components/atoms/ButtonAtom.vue'
  
  const form = ref({
    id: '',
    password: ''
  })
  
  const errors = ref({
    id: false,
    password: false
  })
  
  const handleSubmit = () => {
    // 유효성 검사 로직
    errors.value = {
      id: !form.value.id,
      password: !form.value.password
    }
  
    if (!errors.value.id && !errors.value.password) {
      emit('submit', form.value)
    }
  }
  
  const emit = defineEmits(['submit'])
  </script>