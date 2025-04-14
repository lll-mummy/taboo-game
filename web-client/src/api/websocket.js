let socket = null
let isConnected = false

const listeners = []

/**
 * 初始化 WebSocket 连接
 * @param {string} token 用户 token
 * @param {Function} onMessage 回调处理消息
 */
const baseWsUrl = process.env.VUE_APP_WS_BASE_URL

export function initWebSocket(token, onMessage) {
    if (isConnected || (socket && socket.readyState === WebSocket.OPEN)) return

    const url = `${baseWsUrl}/chat?token=${token}`
    socket = new WebSocket(url)

    socket.onopen = () => {
        // console.log('[WebSocket] 已连接')
        isConnected = true
    }

    socket.onmessage = (event) => {
        try {
            const data = JSON.parse(event.data)
            onMessage && onMessage(data)
            listeners.forEach((cb) => cb(data))
        } catch (e) {
            console.error('[WebSocket] 消息解析错误:', e)
        }
    }

    socket.onerror = (error) => {
        console.error('[WebSocket] 连接错误:', error)
    }

    socket.onclose = () => {
        console.log('[WebSocket] 已关闭')
        isConnected = false
    }
}

/**
 * 发送消息
 * @param {string|Object} message
 */
export function sendMessage(message) {
    if (!isConnected || !socket || socket.readyState !== WebSocket.OPEN) {
        console.error('[WebSocket] 连接未就绪，发送失败')
        return
    }

    const msg = typeof message === 'string' ? message : JSON.stringify(message)
    socket.send(msg)
}

/**
 * 主动关闭 WebSocket
 */
export function closeWebSocket() {
    if (socket) {
        socket.close()
        socket = null
        isConnected = false
    }
}
