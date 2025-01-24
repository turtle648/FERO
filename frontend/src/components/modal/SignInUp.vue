<template>
    <div class="modal" @click="closeModalOutside">
      <div class="modal-content" @click.stop>
        <button class="close-btn" @click="close">닫기</button>
  
        <!-- 로그인 모드 -->
        <div v-if="!isSignUpMode">
          <form @submit.prevent="handleSubmit">
            <div class="input-group">
              <label for="id">ID</label>
              <input type="text" id="id" v-model="id" placeholder="아이디를 입력하세요" required />
            </div>
            <div class="input-group">
              <label for="password">Password</label>
              <input type="password" id="password" v-model="password" placeholder="비밀번호를 입력하세요" required />
            </div>
            <div>
              <button type="submit">로그인</button>
            </div>
          </form>
  
          <hr>

          <div class="social-login">
            <button class="social-button kakao" @click="handleSocialLogin('kakao')">카카오</button>
            <button class="social-button facebook" @click="handleSocialLogin('facebook')">페북</button>
            <button class="social-button google" @click="handleSocialLogin('google')">구글</button>
            <button class="social-button naver" @click="handleSocialLogin('naver')">네이버</button>
          </div>  
          <hr>  
          <div>
            <button @click="openSignUp">회원가입</button>
          </div>
        </div>
  
        <!-- 회원가입 모드 -->
        <div v-else>
          <p> {{ signUpPage }} 페이지에유</p>
          <form v-if="signUpPage === 1" @submit.prevent="handleSignUp1">
            <div class="input-group">
              <label for="signup-id">ID</label>
              <input type="text" id="signup-id" v-model="id" placeholder="아이디를 입력하세요" required />
            </div>
            <div class="input-group">
              <label for="signup-password">Password</label>
              <input type="password" id="signup-password" v-model="password" placeholder="비밀번호를 입력하세요" required />
            </div>
            <div class="input-group">
              <label for="confirm-password">Confirm Password</label>
              <input type="password" id="confirm-password" v-model="confirmPassword" placeholder="비밀번호를 다시 입력하세요" required />
            </div>
            <div class="input-group">
              <label for="user-email">Email</label>
              <input type="email" id="user-email" v-model="email" placeholder="이메일을 입력하세요" required />
            </div>
            <div class="input-group">
              <label for="user-phone">Phone Number</label>
              <input type="text" id="user-phone" v-model="phone" placeholder="전화번호를 입력하세요" required />
            </div>
            <div class="input-group">
              <label for="user-name">Name</label>
              <input type="text" id="user-name" v-model="name" placeholder="이름을 입력하세요" required />
            </div>
            <div>
              <button type="submit">다음</button>
            </div>
          </form>

          <form v-if="signUpPage === 2" @submit.prevent="handleSignUp2">
            <div class="input-group">
              <label for="user-gender">Gender</label>
              <input type="text" id="gender" v-model="gender" placeholder="남 = M / 여 = F" required />
            </div>
            <div class="input-group">
              <label for="user-nickname">Nickname</label>
              <input type="text" id="user-nickname" v-model="userNickname" placeholder="닉네임을 입력하세요" required />
            </div>
            <div>
              <button type="submit">완료</button>
            </div>
          </form>
  
          <div>
            <button @click="closeSignUp">로그인 페이지로 가기</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  import { defineEmits } from 'vue';
  import { useRouter } from 'vue-router';
  import { login, signUp, registerCharacter } from '@/api/accountAPI'; // API import
  
  const emit = defineEmits(['close']);
  
  const isSignUpMode = ref(false)
  const id = ref('')
  const password = ref('')
  const confirmPassword = ref('')
  const email = ref('')
  const phone = ref('')
  const name = ref('')
  const gender = ref('')
  const userNickname = ref('')

  const signUpPage = ref(1)
  
  const router = useRouter();
  
  // 변수 초기화
  const setObjectDefault = () => {
    id.value = '' 
    password.value = '' 
    confirmPassword.value = '' 
    email.value = '' 
    phone.value = '' 
    name.value = '' 
    gender.value = '' 
    userNickname.value = '' 
  }

  const handleSubmit = async () => {
    try {
      const data = await login(id.value, password.value);
      if (data.token) {
        localStorage.setItem('authToken', data.token)
        alert('로그인 성공^.^')
        setObjectDefault()
        router.push('/main')
      } else {
        alert('로그인 실패: 서버에서 토큰을 반환하지 않았습니다.');
      }
    } catch (error) {
      console.error('로그인 처리 중 에러 발생:', error);
      alert('로그인 처리 중 문제가 발생했습니다.');
    }
  };
  
  const handleSignUp1 = async () => {
    if (password.value !== confirmPassword.value) {
      alert('비밀번호가 일치하지 않습니다.')
      return
    }
  
    const requestData = {
      userId: id.value,
      password: password.value,
      userEmail: email.value,
      phoneNumber: phone.value,
      userName: name.value,
    }
  
    try {
      await signUp(requestData)
      alert('1단계 완료!')
      signUpPage.value = 2
    //   closeSignUp()
    } catch (error) {
      console.error('회원가입 처리 중 에러 발생:', error)
      alert('회원가입 처리 중 문제가 발생했습니다.')
    }
  }

  const handleSignUp2 = async () => {
    try {
      await registerCharacter(id.value, gender.value, userNickname.value)
      alert('회원가입 완료!')
      // 페이지 데이터/ 유저정보 등 전부 초기화 해야할듯...
      closeSignUp()
    } catch (error) {
      console.error('회원가입 처리 중 에러 발생:', error)
      alert('회원가입 처리 중 문제가 발생했습니다.')
    }
  }
  
  const handleSocialLogin = (platform) => {
    alert(`${platform} 소셜 로그인! 아직 구현 안했습니다.`);
  };
  
  const openSignUp = () => {
    isSignUpMode.value = true;
    resetFields();
  };
  
  const closeSignUp = () => {
    isSignUpMode.value = false;
    resetFields();
  };
  
  const resetFields = () => {
    id.value = '';
    password.value = '';
    confirmPassword.value = '';
    email.value = '';
    phone.value = '';
    name.value = '';
  };
  
  const close = () => emit('close');
  const closeModalOutside = () => emit('close');
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
    width: 320px;
    height: auto;
    max-height: 500px;
    overflow-y: auto;
  }
  
  button {
    margin-top: 10px;
    padding: 8px 16px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
  }
  
  button.close-btn {
    width: 100%;
    margin-top: 0;
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
    font-size: 12px;
    cursor: pointer;
    text-align: center;
  }
  
  .input-group {
    margin-bottom: 15px;
  }
  
  .input-group input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  
  hr {
    margin: 20px 0;
  }
  </style>
  