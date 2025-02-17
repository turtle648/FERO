<!-- atoms/BaseImageAtom.vue -->
<template>
    <img 
      :src="src"
      :alt="alt"
      :class="$attrs.class"
    >
</template>

<script setup>
import { defineProps } from 'vue'

defineProps({
src: {
    type: String,
    required: true
},
alt: {
    type: String,
    default: ''
}
})
</script>