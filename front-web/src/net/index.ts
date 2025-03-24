// 导入必要的依赖
import axios from "axios";
import {ElMessage} from "element-plus";
import {useStore} from "@/store";

// 存储认证信息的键名
const authItemName = "authorize"

// 生成包含访问令牌的请求头
const accessHeader = () => {
    return {
        'Authorization': `Bearer ${takeAccessToken()}`
    }
}

// 默认错误处理函数
const defaultError = (error:boolean) => {
    console.error(error)
    ElMessage.error('发生了一些错误，请联系管理员')
}

// 默认请求失败处理函数
const defaultFailure = (message:string, status:string, url:string) => {
    console.warn(`请求地址: ${url}, 状态码: ${status}, 错误信息: ${message}`)
    ElMessage.warning(message)
}

// 获取访问令牌，如果令牌过期则删除并返回null
function takeAccessToken() {
    const str = localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName);
    if(!str) return null
    const authObj = JSON.parse(str)
    if(new Date(authObj.expire) <= new Date()) {
        deleteAccessToken()
        ElMessage.warning("登录状态已过期，请重新登录！")
        return null
    }
    return authObj.token
}

// 存储访问令牌，根据remember参数决定存储在localStorage还是sessionStorage
function storeAccessToken(remember:boolean, token:string, expire:Date){
    const authObj = {
        token: token,
        expire: expire
    }
    const str = JSON.stringify(authObj)
    if(remember)
        localStorage.setItem(authItemName, str)
    else
        sessionStorage.setItem(authItemName, str)
}

// 删除存储的访问令牌
function deleteAccessToken() {
    localStorage.removeItem(authItemName)
    sessionStorage.removeItem(authItemName)
}

// 内部POST请求处理函数
function internalPost(url:string, data:any, headers:any, success:any, failure:any, error:any = defaultError){
    axios.post(url, data, { headers: headers }).then(({data}) => {
        if(data.code === 200)
            success(data.data)
        else
            failure(data.message, data.code, url)
    }).catch(err => error(err))
}

// 内部GET请求处理函数
function internalGet(url:string, headers:any, success:any, failure:any, error = defaultError){
    axios.get(url, { headers: headers }).then(({data}) => {
        if(data.code === 200)
            success(data.data)
        else
            failure(data.message, data.code, url)
    }).catch(err => error(err))
}

// 用户登录处理函数
function login(username:string, password:string, remember:boolean, success:any, failure = defaultFailure){
    internalPost('/api/auth/login', {
        username: username,
        password: password
    }, {
        'Content-Type': 'application/x-www-form-urlencoded'
    }, (data:any) => {
        storeAccessToken(remember, data.token, data.expire)
        const store = useStore()
        store.user.role = data.role
        store.user.username = data.username
        store.user.email = data.email
        ElMessage.success(`登录成功，欢迎 ${data.username} 来到我们的系统`)
        success(data)
    }, failure)
}

// 对外暴露的POST请求方法
function post(url:string, data:any, success:any, failure = defaultFailure) {
    internalPost(url, data, accessHeader() , success, failure)
}

// 用户登出处理函数
function logout(success:any, failure = defaultFailure){
    get('/api/auth/logout', () => {
        deleteAccessToken()
        ElMessage.success(`退出登录成功，欢迎您再次使用`)
        success()
    }, failure)
}

// 对外暴露的GET请求方法
function get(url:string, success:any, failure = defaultFailure) {
    internalGet(url, accessHeader(), success, failure)
}

// 检查用户是否未认证
function unauthorized() {
    return !takeAccessToken()
}

// 导出公共方法
export { post, get, login, logout, unauthorized }
