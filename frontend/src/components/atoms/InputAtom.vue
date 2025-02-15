<!-- @/components/atoms/InputAtom.vue (신규) -->
<template>
    <input
      :type="type"
      :value="modelValue"
      @input="$emit('update:modelValue', $event.target.value)"
      :placeholder="placeholder"
      :disabled="disabled"
      :required="required"
      class="w-full px-4 py-2 text-gray-900 bg-white border border-gray-300 rounded-lg 
             focus:ring-2 focus:ring-blue-500 focus:border-blue-500
             disabled:bg-gray-100 disabled:cursor-not-allowed"
      :class="[
        error ? 'border-red-500 focus:ring-red-500 focus:border-red-500' : '',
        $attrs.class
      ]"
    />
  </template>
  
  <script setup>
  import { defineProps, defineEmits } from 'vue'
  
  defineProps({
    type: {
      type: String,
      default: 'text'
    },
    modelValue: String,
    placeholder: String,
    disabled: Boolean,
    required: Boolean,
    error: Boolean
  })
  
  defineEmits(['update:modelValue'])
  </script>