import { STATUS_CODES } from '@/constants/statusCodes'
/**
 * 通用请求封装：自动 try/catch + 统一错误提示
 * @param {Function} fn - 一个返回 Promise 的函数，例如 () => axios.get('/xxx')
 * @returns {Promise<{ data: any, error: any }>}
 */
export async function useRequest(fn) {
    try {
        const res = await fn()
        if (res.code === STATUS_CODES.SUCCESS.code) {
            return { data: res.data, error: null }
        } else {
            return { data: null, error: new Error(res.message) }
        }
    } catch (err) {
        return { data: null, error: err }
    }
}
