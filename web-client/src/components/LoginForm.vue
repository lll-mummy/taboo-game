<template>
  <el-card class="login-box">
    <h2 class="title">欢迎登录</h2>

    <el-form
        :model="form"
        :rules="rules"
        ref="loginFormRef"
        @keyup.enter="handleLogin"
    >
      <el-form-item prop="username">
        <el-input
            v-model="form.username"
            placeholder="请输入用户名"
            clearable
            :prefix-icon="User"
        />
      </el-form-item>

      <el-form-item prop="password">
        <el-input
            v-model="form.password"
            placeholder="请输入密码"
            type="password"
            show-password
            :prefix-icon="Lock"
        />
      </el-form-item>

      <el-form-item>
        <el-button
            type="primary"
            @click="handleLogin"
            :loading="loading"
            round
            class="login-button"
        >
          登 录
        </el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { defineEmits } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { useLogin } from '@/hooks/useLogin'
import '@/assets/css/login.css'

const emit = defineEmits(['loginSuccess'])

const {
  form,
  rules,
  loading,
  loginFormRef,
  handleLogin
} = useLogin(emit)
</script>
