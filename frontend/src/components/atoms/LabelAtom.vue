<template>
    <label 
      :for="htmlFor" 
      class="block mb-2 text-sm font-medium text-gray-900"
      :class="[error ? 'text-red-500' : '', $attrs.class]"
    >
      <slot></slot>
      <span v-if="required" class="text-red-500 ml-1">*</span>
    </label>
  </template>
  
  <script setup>
  import { defineProps } from 'vue'
  
  defineProps({
    htmlFor: String,
    required: Boolean,
    error: Boolean
  })
  </script>