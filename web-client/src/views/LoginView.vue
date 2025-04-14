<template>
  <div class="login-container">
    <login-form @loginSuccess="handleLoginSuccess" />
  </div>
</template>

<script setup>
import LoginForm from '@/components/LoginForm.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const handleLoginSuccess = (userData) => {
  localStorage.setItem('token', userData.token)
  router.push('/main')  // 跳转到主页面
}
</script>
