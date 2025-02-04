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
            <label for="user-name">Name</label>
            <input type="text" id="user-name" v-model="name" placeholder="이름을 입력하세요" required />
          </div>
          <div class="input-group">
            <label for="user-phone">Phone Number</label>
            <input type="text" id="user-phone" v-model="phone" placeholder="전화번호를 입력하세요" required />
          </div>
          <div class="input-group">
            <label for="user-email">Email</label> 
            <input type="email" id="user-email" v-model="email" placeholder="이메일을 입력하세요" required :disabled="isEmailAvailable" />
            <button type="button" @click="checkEmailDuplicate(email)">중복 확인</button>
          </div>
          <div>
            <button type="submit">다음</button>
          </div>
        </form>

        <form v-if="signUpPage === 2" @submit.prevent="handleSignUp2">
          <div class="input-group">
            <input type="text" v-model="emailConfirmCode" placeholder="인증 코드를 입력하세요" required />
            <button type="submit">인증 확인</button>
          </div>
        </form>

        <form v-if="signUpPage === 3" @submit.prevent="handleSignUp3">
          <div class="input-group">
            <label for="user-gender">Character Gender</label>
            <input type="text" id="gender" v-model="gender" placeholder="남 = M / 여 = F" required />
          </div>
          <div class="input-group">
            <label for="user-nickname">User Nickname</label>
            <input type="text" id="user-nickname" v-model="userNickname" placeholder="유저 닉네임" required />
          </div>
          <div>
            <hr>
            캐릭터 고르는 칸으로 만들 예정입니다.
            <hr>
            캐릭터 고르는 칸으로 만들 예정입니다.
            <hr>
            캐릭터 고르는 칸으로 만들 예정입니다.
            <hr>
            캐릭터 고르는 칸으로 만들 예정입니다.
          </div>
          <div>
            <button type="submit">완료</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
  
<script setup>
import { ref } from 'vue'
import { defineEmits } from 'vue'
import { useRouter } from 'vue-router'
import { login, checkEmailDuplicateAPI, signUp, verifyEmail, registerCharacter} from '@/api/accountAPI'
import { useUserStore } from '@/stores/store'

const emit = defineEmits(['close'])
const userStore = useUserStore() // Pinia store 사용
const router = useRouter()
const isSignUpMode = ref(false)
const signUpPage = ref(1)

const id = ref('')
const password = ref('')
const confirmPassword = ref('')
const name = ref('')
const phone = ref('')
const email = ref('')
const isEmailAvailable = ref(false)
const emailConfirmCode = ref('')
const gender = ref('')
const userNickname = ref('')

// 변수 초기화
const resetFields = () => {
  id.value = ''
  password.value = ''
  confirmPassword.value = ''
  email.value = ''
  phone.value = ''
  name.value = ''
  gender.value = ''
  isEmailAvailable.value = false
  emailConfirmCode.value = ''
  userNickname.value = ''
}

// 로그인 API 호출
const handleSubmit = async () => {
  try {
    const result = await login(id.value, password.value)
    if (result) { 
      userStore.setAccessToken(result)
      router.push('/main') 
    }
  } catch (error) {console.log(error)}
}

// 이메일 중복확인 API
const checkEmailDuplicate = (email) => {
  checkEmailDuplicateAPI(email)
    .then((result) => {
      if (result) {
        isEmailAvailable.value = true
        alert('이메일 중복확인이 완료되었습니다.')
      }
    })
}

// 회원가입 API 1번
const handleSignUp1 = () => {
  if (password.value !== confirmPassword.value) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  } else if (!isEmailAvailable.value) {
    alert('이메일 중복체크를 진행해 주세요.')
    return
  } else {
    signUp(id.value, password.value, name.value, phone.value, email.value)
      .then((result) => {
        userStore.setSessionId(result.message) // Pinia에서 sessionId 설정
        signUpPage.value = 2
      });
  }
};

// 인증 코드 확인 후 3페이지로 이동
const handleSignUp2 = async () => {
  const result = await verifyEmail(emailConfirmCode.value, email.value)
  if (result) { signUpPage.value = 3} 
  else { alert('인증 코드가 유효하지 않습니다. 다시 시도해주세요.')}
}

const handleSignUp3 = async () => {
  const result = await registerCharacter(gender.value, userNickname.value, userStore.sessionId)
  if (result) {
    alert('회원가입이 완료되었습니다.')
  }
  closeSignUp()
}

const handleSocialLogin = (platform) => {
  alert(`${platform} 소셜 로그인! 아직 구현 안했습니다.`)
}

const openSignUp = () => {
  isSignUpMode.value = true
  resetFields()
}

const closeSignUp = () => {
  isSignUpMode.value = false
  resetFields()
}

const close = () => {
  resetFields()
  emit('close')
}

const closeModalOutside = () => {
  resetFields()
  emit('close')
}
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
    background-color: blanchedalmond;
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
  