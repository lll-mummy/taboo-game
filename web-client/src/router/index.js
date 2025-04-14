import { createRouter, createWebHashHistory } from 'vue-router'
import LoginView from '@/views/LoginView.vue'
import MainView from '@/views/MainView.vue'
import {ElMessage} from "element-plus";

const routes = [
    {
        path: '/',
        name: 'Login',
        component: LoginView,
        meta: {
            public: true,
            title: '登录'
        }
    },
    {
        path: '/main',
        name: 'Main',
        component: MainView,
        meta: {
            requiresAuth: true,
            title: '主页面'
        }
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

router.beforeEach((to) => {
    // 设置页面标题
    if (to.meta.title) {
        document.title = to.meta.title
    }
    const token = localStorage.getItem('token')
    if (to.meta.public) return true
    if (!token) {
        ElMessage.error('请重新登录')  // ✅ 弹出提示
        return {
            path: '/',
            query: { redirect: to.fullPath } // 携带重定向路径
        }
    }
    return true
})

export default router