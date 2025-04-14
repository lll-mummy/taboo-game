import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { STATUS_CODES } from '@/constants/statusCodes'

const TIMEOUT = 10000;

function handleAuthError() {
    localStorage.removeItem('token');
    router.push('/').then();
}

const service = axios.create({
    baseURL: process.env.VUE_APP_API_BASE_URL,
    timeout: TIMEOUT,
    withCredentials: true
});

// ✅ 请求拦截器
service.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => Promise.reject(error)
);

// ✅ 响应拦截器
service.interceptors.response.use(
    response => {
        const res = response.data;
        // 成功响应，返回数据
        if (res.code === STATUS_CODES.SUCCESS.code) return res;
        ElMessage.error(res.message || STATUS_CODES.SERVER_ERROR.message);

        if (res.code === STATUS_CODES.UNAUTHORIZED.code || res.code === STATUS_CODES.TOKEN_EXPIRED.code) {
            handleAuthError();
        }
        return Promise.reject(new Error(res.message || STATUS_CODES.SERVER_ERROR.message));
    },

    error => {
        // 错误状态码：401，跳转到登录页面
        if (error.response?.status === STATUS_CODES.UNAUTHORIZED.code) {
            ElMessage.error(STATUS_CODES.UNAUTHORIZED.message);
            handleAuthError();
        } else if (error.response?.status === STATUS_CODES.SERVER_ERROR.code) {
            ElMessage.error(STATUS_CODES.SERVER_ERROR.message);
        } else {
            ElMessage.error(STATUS_CODES.UNKNOWN_ERROR.message);
        }
        return Promise.reject(error);
    }
);

export default service;
