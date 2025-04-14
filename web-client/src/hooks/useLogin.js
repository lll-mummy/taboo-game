import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import {login} from '@/api/auth'
import {useRequest} from '@/hooks/useRequest'

export function useLogin(emit) {
    const form = ref({
        username: '',
        password: ''
    })

    const rules = {
        username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        password: [{required: true, message: '请输入密码', trigger: 'blur'}]
    }

    const loading = ref(false)
    const loginFormRef = ref()

    const handleLogin = async () => {
        try {
            await loginFormRef.value.validate()
            loading.value = true
            const {data, error} = await useRequest(() => login(form.value))
            if (error) return
            ElMessage.success(`欢迎 ${data.username}`)
            localStorage.setItem('token', data.token)
            emit('loginSuccess', data)
        } finally {
            loading.value = false
        }
    }

    return {
        form,
        rules,
        loading,
        loginFormRef,
        handleLogin
    }
}
