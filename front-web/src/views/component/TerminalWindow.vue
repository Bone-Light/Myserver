<script setup lang="ts">
import {reactive, ref, watch} from "vue";
import {get, post} from "@/net";
import Terminor from "@/views/component/Terminor.vue";

const props = defineProps({
  id: Number
});

const connection = reactive({
  ip: '',
  port: 22,
  username: '',
  password: ''
});

const rules = {
  port: [
    { required: true, message: '请输入端口', trigger: ['blur', 'change'] },
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: ['blur', 'change'] },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: ['blur', 'change'] },
  ]
};

const state = ref(1);  // 1:ssh登录 2:ssh终端
const formRef = ref();
const valid = ref(false);
const onValidate = (_prop:any, isValid:any) => valid.value = isValid;

function saveConnection() {
  formRef.value.validate((isValid:boolean) => {
    if(isValid) {
      post('/api/monitor/ssh-save', {
        ...connection,
        id: props.id
      }, ()=>state.value = 2)
    }
  })
}

watch(()=> props.id, id => {
  state.value = 1;
  if(id !== -1) {
    connection.ip = '';
    get(`/api/monitor/ssh?clientId=${id}`, (data:any) => Object.assign(connection, data));
  }
}, {immediate:true});
</script>

<template>
  <div class="terminal-main">
    <div class="login" v-loading="!connection.ip" v-if="state === 1">
      <i class="fa-solid fa-terminal" style="font-size: 50px"/>
      <div style="margin-top: 10px; font-weight: bold; font-size: 20px">服务端连接信息</div>
      <el-form style="width: 400px; margin: 20px auto" :model="connection"
               @validate="onValidate" :rules="rules" ref="formRef" label-width="100">
        <div style="display: flex; gap: 10px">
          <el-form-item style="width:100%" label="服务器IP地址" prop="ip">
            <el-input v-model="connection.ip" disabled/>
          </el-form-item>
          <el-form-item style="width: 80px" prop="port" label-width="0">
            <el-input placeholder="端口" v-model="connection.port"/>
          </el-form-item>
        </div>
        <el-form-item prop="username" label="登录用户名">
          <el-input placeholder="请输入用户名..." v-model="connection.username"/>
        </el-form-item>
        <el-form-item prop="password" label="登录密码 ">
          <el-input placeholder="请输入密码..." v-model="connection.password"/>
        </el-form-item>
        <el-button type="success" @click="saveConnection" :disabled="!valid">立即连接</el-button>
      </el-form>
    </div>
    <div v-if="state === 2">
      <div style="overflow: hidden; padding: 0 10px 10px 10px">
        <terminor :id="id" @dispose="state = 1"/>
      </div>
    </div>
  </div>
</template>

<style scoped>
.terminal-main {
  width: 100%;
  height: 100%;

  .login {
    text-align: center;
    padding-top: 50px;
    height: 100%;
    box-sizing: border-box;
  }
}
</style>