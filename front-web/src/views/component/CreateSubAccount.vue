<script setup lang="ts">
import {User, Message, Lock} from "@element-plus/icons-vue";
import {osNameToIcon} from "@/tools";
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import {post} from "@/net"

defineProps({
  clients: Array<any>
});
const emits = defineEmits(['create']);
const form = reactive({
  username: '',
  email: '',
  password: ''
});
const formRef = ref();
const valid = ref(false);
const onValidate = (_prop:any, isValid:any) => valid.value = isValid;
const validateUsername = (_rule:any, value:string, callback:Function) => {
  if (value === '') {
    callback(new Error('请输入用户名'));
  } else if(!/^[a-zA-Z0-9\u4e00-\u9fa5]+$/.test(value)){
    callback(new Error('用户名不能包含特殊字符，只能是中文/英文'));
  } else {
    callback();
  }
};
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: ['blur', 'change'] },
    { validator: validateUsername, trigger: ['blur', 'change'] },
    { min: 2, max: 16, message: '用户名的长度必须在2-16个字符之间', trigger: ['blur', 'change'] },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: ['blur', 'change'] },
    { min: 6, max: 26, message: '密码的长度必须在6-26个字符之间', trigger: ['blur', 'change'] }
  ], email: [
    { required: true, message: '请输入邮件地址', trigger: ['blur', 'change'] },
    {type: 'email', message: '请输入合法的电子邮件地址', trigger: ['blur', 'change']}
  ]
};

const checkedClients:number[] = [];
const onCheck = (state:boolean, id:number) => {
  if(state){
    checkedClients.push(id);
  } else {
    const index = checkedClients.indexOf(id);
    checkedClients.splice(index, 1);
  }
}

function createSubAccount() {
  formRef.value.validate((isValid:any) => {
    if(checkedClients.length === 0){
      ElMessage.warning('请至少选择一个服务器用于子账户管理');
      return;
    }
    if(isValid) {
      post('api/user/sub/create',{
        ...form,
        clients:checkedClients
      }, () => {
        ElMessage.success('子账户创建成功');
        emits('create');
      })
    }
  });
}
</script>

<template>
  <div style="padding: 15px 20px; height: 100%">
    <div style="display: flex; flex-direction: column; height: 100%">
      <div>
        <div class="title">
          <i class="fa-solid fa-user-plus"/> 添加新的子用户
        </div>
        <div class="desc">子用户可以同样用于管理服务器,但是只能访问被给予权限的服务器</div>
        <el-divider style="margin: 10px 0"/>
      </div>
      <div>
        <el-form label-position="top" :rules="rules" :model="form"
                 @validate="onValidate" ref="formRef">
          <el-form-item label="用户名" prop="username">
            <el-input type="text" v-model="form.username"
                      :prefix-icon="User" placeholder="子账户用户名" maxlength="16"/>
          </el-form-item>
          <el-form-item label="电子邮件" prop="email">
            <el-input type="text" v-model="form.email"
                      :prefix-icon="Message" placeholder="子账户电子用户" maxlength="26"/>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input type="password" v-model="form.password"
                      :prefix-icon="Lock" placeholder="子账户密码" maxlength="16"/>
          </el-form-item>
        </el-form> <!--子用户注册表单-->
        <el-divider style="margin: 10px 0"/>
        <div class="desc">请再下方选择允许子账户访问的服务器列表</div>
      </div>
      <el-scrollbar style="flex: 1">
        <div class="client-card" v-for="item in clients">
          <el-checkbox @change="(state:boolean) => onCheck(state, item.id)"/>
          <div style="margin-left: 20px">
            <div style="font-size: 14px; font-weight: bold">
              <span :class="`flag-icon flag-icon-${item.location}`"></span>
              <span style="margin: 0 10px">{{item.name}}</span>
            </div>
            <div style="font-size: 12px; color: grey">
              操作系统:
              <i :style="{color: osNameToIcon(item.osName).color}"
                 :class="`fa-brands ${osNameToIcon(item.osName).icon}`"/>
              {{`${item.osName} ${item.osVersion}`}}
            </div>
            <div style="font-size: 12px; color: grey">
              <span style="margin-right: 10px">公网IP: {{item.ip}}</span>
            </div>
          </div>
        </div>
      </el-scrollbar>
      <div style="text-align: center; margin-top: 10px">
        <el-button @click="createSubAccount" type="success" :disabled="!valid" plain>确认创建</el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.title {
  font-size: 18px;
  font-weight: bold;
  color: dodgerblue;
}

.desc {
  font-size: 13px;
  color: grey;
  line-height: 16px;
}

.client-card {
  border-radius: 5px;
  background-color: var(--el-bg-color-page);
  padding: 10px;
  display: flex;
  align-items: center;
  margin: 10px;
}
</style>