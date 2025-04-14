<template>
  <div class="mobile-container">
    <!-- 顶部标题 -->
    <div class="header">
      <h1>用户列表</h1>
    </div>

    <!-- 可滚动的两列内容区域 -->
    <div class="scrollable-content">
      <div class="two-column-layout">
        <!-- 左列 -->
        <div class="column">
          <div v-for="(user, index) in leftColumnUsers" :key="'left-'+user.id" class="user-card">
            <el-avatar :size="40" :src="user.avatar || defaultAvatar" class="user-avatar"></el-avatar>
            <div class="user-info">
              <span class="username">{{ user.username }}</span>
              <div class="user-word">{{ user.word }}</div>
            </div>
            <div class="user-score">{{ user.count }}</div>
          </div>
        </div>

        <!-- 右列 -->
        <div class="column">
          <div v-for="(user, index) in rightColumnUsers" :key="'right-'+user.id" class="user-card">
            <el-avatar :size="40" :src="user.avatar || defaultAvatar" class="user-avatar"></el-avatar>
            <div class="user-info">
              <span class="username">{{ user.username }}</span>
              <div class="user-word">{{ user.word }}</div>
            </div>
            <div class="user-score">{{ user.count }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部操作栏 -->
    <div class="bottom-action-bar">
      <el-button type="primary" class="action-btn" @click="handleChangeWord">
        更换词牌
      </el-button>
      <div class="user-count">
        重生次数: {{ currentUserCount }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import service from '@/api/request'
import { useRequest } from '@/hooks/useRequest'
import { initWebSocket } from '@/api/websocket' // 引入 WebSocket 初始化方法
import { useWordChange } from '@/hooks/useWordChange' // 引入封装好的 useWordChange

import '@/assets/css/main.css'

const currentUsername = ref('')
const onlineUsers = ref([])
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'

// 初始化 WebSocket
const init = async () => {
  try {
    const { data, error } = await useRequest(() => service.get('/auth/username'))
    if (error) return

    currentUsername.value = data
    const token = localStorage.getItem('token')

    initWebSocket(token, (data) => {
      if (data.system && Array.isArray(data.message)) {
        onlineUsers.value = data.message.map(user => ({
          id: user.id,
          username: user.username,
          avatar: user.avatar || defaultAvatar,
          count: user.count,
          word: user.word,
        }))
      }
    })
  } catch (err) {
    console.error('初始化失败:', err)
  }
}

onMounted(() => {
  init()
})

// 使用封装的 useWordChange 组合式函数
const {currentUserCount, handleChangeWord } = useWordChange(currentUsername, onlineUsers)

// 过滤后的左右两列用户
const leftColumnUsers = computed(() =>
    onlineUsers.value
        .filter(user => user.username !== currentUsername.value)
        .filter((_, i) => i % 2 === 0)
)

const rightColumnUsers = computed(() =>
    onlineUsers.value
        .filter(user => user.username !== currentUsername.value)
        .filter((_, i) => i % 2 !== 0)
)
</script>



