<template>
    <div class="modal" @click="closeModalOutside">
      <div class="modal-content" @click.stop>
        <!-- 닫기 버튼 -->
        <button @click="close">Close</button>
  
        <!-- 로그인 모드 -->
        <div v-if="!isSignUpMode">
          <!-- 아이디/비밀번호 폼 -->
          <form @submit.prevent="handleSubmit">
            <div>
              <label for="id">ID</label>
              <input type="text" id="id" v-model="id" placeholder="Enter your ID" required />
            </div>
            <div>
              <label for="password">Password</label>
              <input type="password" id="password" v-model="password" placeholder="Enter your password" required />
            </div>
            <div>
              <button type="submit">로그인</button>
            </div>
          </form>
  
          <hr>
          <!-- 소셜 로그인 버튼들 -->
          <div class="social-login">
            <button class="social-button kakao" @click="handleSocialLogin('kakao')">카카오</button>
            <button class="social-button facebook" @click="handleSocialLogin('facebook')">페북</button>
            <button class="social-button google" @click="handleSocialLogin('google')">구글</button>
            <button class="social-button naver" @click="handleSocialLogin('naver')">네이버</button>
          </div>
          <hr>
  
          <!-- 회원가입 버튼 -->
          <div>
            <button @click="openSignUp">회원가입</button>
          </div>
        </div>
  
        <!-- 회원가입 모드 -->
        <div v-else>
          <form @submit.prevent="handleSignUp">
            <div>
              <label for="signup-id">ID</label>
              <input type="text" id="signup-id" v-model="id" placeholder="Enter your ID" required />
            </div>
            <div>
              <label for="signup-password">Password</label>
              <input type="password" id="signup-password" v-model="password" placeholder="Enter your password" required />
            </div>
            <div>
              <label for="confirm-password">Confirm Password</label>
              <input type="password" id="confirm-password" v-model="confirmPassword" placeholder="Confirm your password" required />
            </div>
            <div>
              <label for="user-email">Email</label>
              <input type="email" id="user-email" v-model="email" placeholder="Enter your email" required />
            </div>
            <div>
              <label for="user-phone">Phone Number</label>
              <input type="text" id="user-phone" v-model="phone" placeholder="Enter your phone number" required />
            </div>
            <div>
              <label for="user-name">Name</label>
              <input type="text" id="user-name" v-model="name" placeholder="Enter your name" required />
            </div>
            <div>
              <button type="submit">Sign Up</button>
            </div>
          </form>
  
          <div>
            <button @click="closeSignUp">Back to Sign In</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue'
  import { defineEmits } from 'vue'
  import { useRouter } from 'vue-router'
  
  // 부모 컴포넌트로 모달 닫기 이벤트 전송
  const emit = defineEmits(['close'])
  
  // 로그인/회원가입 모드 상태
  const isSignUpMode = ref(false)
  
  // 사용자 입력 상태
  const id = ref('')
  const password = ref('')
  const confirmPassword = ref('')
  const email = ref('')
  const phone = ref('')
  const name = ref('')
  
  // 라우터 사용
  const router = useRouter()
  
  // 로그인 폼 제출 처리
  const handleSubmit = async () => {
    try {
      // 실제 API 호출이 아닌 임시 응답으로 처리
      const mockResponse = { token: '' }
      const data = mockResponse
  
      if (data.token) {
        // 토큰이 있으면 저장 후, 메인 페이지로 이동
        localStorage.setItem('authToken', data.token)
        router.push('/main')
      } else {
        alert('로그인 실패: 서버에서 토큰을 반환하지 않았습니다.')
      }
    } catch (error) {
      console.error('로그인 처리 중 에러 발생:', error)
      alert('로그인 처리 중 문제가 발생했습니다.')
    }
  }
  
  // 회원가입 폼 제출 처리
  const handleSignUp = async () => {
    // 회원가입 요청 데이터
    const requestData = {
      userId: id.value,
      password: password.value,
      userEmail: email.value,
      phoneNumber: phone.value,
      userName: name.value
    }
  
    try {
      // Spring Boot API 호출 (이 부분은 실제 API 호출로 변경 필요)
      const response = await fetch('/api/v1/users', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestData)
      })
  
      if (response.ok) {
        alert('회원가입 완료!')
        closeSignUp()
      } else {
        alert('회원가입 실패!')
      }
    } catch (error) {
      console.error('회원가입 중 에러 발생:', error)
      alert('회원가입 처리 중 문제가 발생했습니다.')
    }
  }
  
  // 소셜 로그인 처리
  const handleSocialLogin = (platform) => {
    alert(`${platform} 소셜 로그인! 아직 구현 안했습니다만?`)
  }
  
  // 회원가입 모달 열기
  const openSignUp = () => {
    isSignUpMode.value = true
    id.value = ''
    password.value = ''
    confirmPassword.value = ''
    email.value = ''
    phone.value = ''
    name.value = ''
  }
  
  // 로그인 모달 열기
  const closeSignUp = () => {
    isSignUpMode.value = false
    id.value = ''
    password.value = ''
    confirmPassword.value = ''
    email.value = ''
    phone.value = ''
    name.value = ''
  }
  
  // 모달 닫기 처리
  const close = () => emit('close')
  
  // 배경 클릭 시 모달 닫기 처리
  const closeModalOutside = () => emit('close')
  </script>
  
  <style scoped>
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal-content {
    background-color: white;
    padding: 20px;
    border-radius: 8px;
    width: 280px;
    height: 300px;
  }
  button {
    margin-top: 10px;
  }
  .social-login {
    display: flex;
    justify-content: space-between;
    gap: 10px;
    margin-top: 20px;
  }
  .social-button {
    flex: 1;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    font-size: 8px;
    cursor: pointer;
    text-align: center;
  }
  </style>
  