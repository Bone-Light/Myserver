<script setup lang="ts">
import {reactive, ref} from 'vue'
import router from '@/router'
import {ElMessage} from "element-plus";
import { login } from '@/net';

const loginRef = ref();
const rules = {
  username: [
    { required: true, message: '请输入用户名' }
  ],
  password: [
    { required: true, message: '请输入密码'}
  ]
}

const loginForm = reactive({
  username: '',
  password: '',
  remember: false,
})

function userLogin() {
  loginRef.value.validate((valid:boolean) => {
    if (valid) {
      ElMessage.success("登录成功");
      login(loginForm.username, loginForm.password, loginForm.remember, ()=>{router.push("/monitor")});
    }
  })
}


const goToForget = () => {
  router.push("/login/forget")
}
</script>

<template>
  <el-card class="login-card">
      <div class="login-header">
        <h2>欢迎来到封灯云</h2>
        <p>请输入您的账号和密码</p>
      </div>
      <el-form :model="loginForm" class="login-form" :rules="rules" ref="loginRef">
        <el-form-item>
          <el-input
            v-model="loginForm.username"
            placeholder="用户名"
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-checkbox v-model="loginForm.remember" label="记住我" style="color:white; font-size: 14px"/>
        <div class="form-footer">
          <el-link type="primary" class="forget-password" @click="goToForget">忘记密码？</el-link>
        </div>
        <el-form-item>
          <el-button type="primary" class="login-button" @click="userLogin">
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
</template>

<style scoped>
.login-button {
  width: 100%;
  height: 40px;
  font-size: 16px;
  border-radius: 20px;
  background: linear-gradient(45deg, #409EFF, #36cfc9);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

:deep(.el-input__wrapper) {
  border-radius: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.8);
}

:deep(.el-input__wrapper:hover) {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
  background: rgba(255, 255, 255, 0.9);
}

:deep(.el-input__inner) {
  color: #333;
}

:deep(.el-input__prefix) {
  color: #666;
}

@media screen and (max-width: 480px) {
  .login-card {
    width: 90%;
    margin: 0 20px;
  }
}

.form-footer {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 20px;
}

.forget-password {
  font-size: 14px;
  text-decoration: none;
  transition: all 0.3s ease;
  color: rgba(255, 255, 255, 0.9) !important;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  user-select: none;
}

.forget-password:hover {
  opacity: 0.8;
  transform: translateY(-1px);
  color: #fff !important;
}

.login-card {
  width: 400px;
  padding: 20px;
  border-radius: 15px;
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  transition: transform 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.login-card:hover {
  transform: translateY(-5px);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  color: #fff;
  margin-bottom: 10px;
  font-size: 30px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.login-header p {
  color: rgba(255, 255, 255, 0.9);
  font-size: 16px;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
}

.login-form {
  margin-top: 20px;
}

</style>