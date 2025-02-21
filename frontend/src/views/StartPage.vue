<!-- views/
└── StartPage.vue
    └── layouts/
        └── StartPageLayout.vue
            ├── BackgroundOrganism.vue
            │   └── molecules/
            │       ├── BackgroundImageMolecule.vue
            │       │   └── atoms/
            │       │       └── BaseImageAtom.vue
            │       └── BackgroundMusicMolecule.vue
            │           └── atoms/
            │               └── AudioAtom.vue
            ├── AudioControlButtonMolecule.vue [NEW]  // 새로 추가된 컴포넌트
            │   └── atoms/
            │       └── ButtonAtom.vue
            ├── LogoOrganism.vue
            │   └── molecules/
            │       └── LogoImageMolecule.vue
            │           └── atoms/
            │               └── BaseImageAtom.vue
            └── StartButtonOrganism.vue
                └── molecules/
                    ├── BaseButtonMolecule.vue
                    │   └── atoms/
                    │       └── ButtonAtom.vue
                    └── ButtonTextMolecule.vue
                        └── atoms/
                            ├── TextAtom.vue
                            └── HighlightedTextAtom.vue -->

<!-- views/StartPage.vue -->
<template>
  <StartPageLayout
    :showModal="showModal"
    @goToMain="goToMain"
    @closeModal="closeModal"
    ref="audioPlayerRef"
  />
</template>

<script setup>
import { ref,nextTick } from 'vue'
import { useRouter } from 'vue-router'
import StartPageLayout from '@/components/layouts/StartPageLayout.vue'

const router = useRouter()
const showModal = ref(false)
const token = localStorage.getItem('authToken')

const goToMain = async () => {
  if (!token) {
    showModal.value = true
    await nextTick()
  } else {
    try {
      await router.push('/main')
    } catch (error) {
      console.error('라우팅 에러:', error)
    }
  }
}

const closeModal = () => { 
  showModal.value = false 
}
</script>
