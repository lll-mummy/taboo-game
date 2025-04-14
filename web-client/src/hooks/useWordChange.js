// src/hooks/useWordChange.js
import { computed } from 'vue'
import { ElMessageBox } from 'element-plus'
import { sendMessage } from '@/api/websocket'

export function useWordChange(currentUsername, onlineUsers) {
    // 当前用户的词牌
    const currentUserWord = computed(() => {
        const user = onlineUsers.value.find(u => u.username === currentUsername.value)
        return user?.word || '暂无词牌'
    })

    // 当前用户的重生次数
    const currentUserCount = computed(() => {
        const user = onlineUsers.value.find(u => u.username === currentUsername.value)
        return user ? user.count : 0
    })

    // 更换词牌的操作逻辑
    const handleChangeWord = async () => {
        try {
            await ElMessageBox.confirm('确定要查看并更换词牌吗？', {
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            })

            const currentWord = currentUserWord.value

            await ElMessageBox.alert(`你的词牌是: ${currentWord}`, {
                confirmButtonText: '知道了',
                customClass: 'word-display-box'
            })

            sendMessage('changeWord') // 发送换词消息

        } catch (error) {
            if (error !== 'cancel') {
                console.error('操作异常:', error)
            }
        }
    }

    return {
        currentUserWord,
        currentUserCount,
        handleChangeWord
    }
}
