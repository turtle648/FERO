module.exports = {
  env: {
    browser: true,
    node: true,
    jest: true,
  },
  extends: [
    "eslint:recommended",
    "plugin:vue/vue3-essential",
  ],
  parserOptions: {
    ecmaVersion: 2020,
    sourceType: 'module'
  },
  globals: {
    defineProps: "readonly",
    defineEmits: "readonly",
    defineExpose: "readonly",
    withDefaults: "readonly"
  },
  rules: {
    'no-undef': 'off',
    'vue/script-setup-uses-vars': 'error'
  }
}
