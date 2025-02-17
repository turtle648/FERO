<!-- atoms/ButtonAtom.vue -->
<template>
    <button 
      :class="[
        'bg-transparent border-none cursor-pointer',
        'transition-all duration-200 ease-in-out',
        $attrs.class
      ]"
      @click="$emit('click')"
    >
      <slot></slot>
    </button>
</template>

<script setup>
import {defineEmits} from 'vue'

defineEmits(['click'])
</script>
