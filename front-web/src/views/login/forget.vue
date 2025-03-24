<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import router from '@/router'

const activeStep = ref(0)
const forgetForm = ref({
  email: '',
  verificationCode: '',
  newPassword: '',
  confirmPassword: ''
})

const handleSendCode = () => {
  if (!forgetForm.value.email) {
    ElMessage.error('请输入邮箱地址')
    return
  }
  // TODO: 实现发送验证码逻辑
  ElMessage.success('验证码已发送')
}

const handleVerifyCode = () => {
  if (!forgetForm.value.verificationCode) {
    ElMessage.error('请输入验证码')
    return
  }
  // TODO: 实现验证码验证逻辑
  activeStep.value = 1
}

const handleResetPassword = () => {
  if (!forgetForm.value.newPassword || !forgetForm.value.confirmPassword) {
    ElMessage.error('请填写完整信息')
    return
  }
  if (forgetForm.value.newPassword !== forgetForm.value.confirmPassword) {
    ElMessage.error('两次输入的密码不一致')
    return
  }
  // TODO: 实现重置密码逻辑
  ElMessage.success('密码重置成功')
  router.push('/login')
}
</script>

<template>
  <el-card class="login-card">
    <div class="login-header">
      <h2>重置密码</h2>
      <p>请按步骤完成密码重置</p>
    </div>

    <el-steps :active="activeStep" finish-status="success" class="steps" align-center>
      <el-step title="验证身份" />
      <el-step title="设置新密码" />
    </el-steps>

    <el-form :model="forgetForm" class="login-form">
      <!-- 步骤一：验证身份 -->
      <div v-show="activeStep === 0">
        <el-form-item>
          <el-input
            v-model="forgetForm.email"
            placeholder="请输入邮箱"
            prefix-icon="Message"
          />
        </el-form-item>
        <el-form-item>
          <div class="verification-code">
            <el-input
              v-model="forgetForm.verificationCode"
              placeholder="请输入验证码"
              prefix-icon="Key"
            />
            <el-button type="primary" @click="handleSendCode" style="margin-left: 15px;">获取验证码</el-button>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-button" @click="handleVerifyCode">
            验证
          </el-button>
        </el-form-item>
      </div>

      <!-- 步骤二：设置新密码 -->
      <div v-show="activeStep === 1">
        <el-form-item>
          <el-input
            v-model="forgetForm.newPassword"
            type="password"
            placeholder="请输入新密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="forgetForm.confirmPassword"
            type="password"
            placeholder="请确认新密码"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-button" @click="handleResetPassword">
            重置密码
          </el-button>
        </el-form-item>
      </div>

      <div class="form-footer">
        <el-link type="primary" class="back-link" @click="router.push('/login')">
          返回登录
        </el-link>
      </div>
    </el-form>
  </el-card>
</template>

<style scoped>
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

.steps {
  margin: 0 30px 30px 30px;
}

:deep(.el-steps) {
  --el-text-color-regular: rgba(255, 255, 255, 0.9);
}

:deep(.el-step__title) {
  color: rgba(255, 255, 255, 0.9);
  font-size: 14px;
}

:deep(.el-step__head.is-process) {
  color: #409EFF;
}

:deep(.el-step__head.is-success) {
  color: #67C23A;
}

:deep(.el-step__line) {
  background-color: rgba(255, 255, 255, 0.3);
}

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

.form-footer {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.back-link {
  font-size: 14px;
  text-decoration: none;
  transition: all 0.3s ease;
  color: rgba(255, 255, 255, 0.9) !important;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.1);
  user-select: none;
}

.back-link:hover {
  opacity: 0.8;
  transform: translateY(-1px);
  color: #fff !important;
}

@media screen and (max-width: 480px) {
  .login-card {
    width: 90%;
    margin: 0 20px;
  }
}

.verification-code {
  display: flex;
  gap: 10px;
}

.verification-code :deep(.el-input__wrapper) {
  flex: 1;
}

.verification-code .el-button {
  border-radius: 20px;
  background: linear-gradient(45deg, #409EFF, #36cfc9);
  border: none;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.verification-code .el-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}
</style>