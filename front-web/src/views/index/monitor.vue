<script setup lang="ts">
import ServerCard from "@/views/component/ServerCard.vue";
import {reactive, ref, computed} from 'vue';
import {useStore} from '@/store'
import {get} from '@/net'
import {
  Document,
  HomeFilled,
  Monitor,
  Setting,
  User,
  Medal,
  SwitchButton,
  Plus,
} from "@element-plus/icons-vue";
import {useRoute} from "vue-router";
import ClientDetail from "@/views/component/ClientDetail.vue";
import RegisterCard from "@/views/component/RegisterCard.vue";

const isCollapse = ref(false);
const activeIndex = ref('1');
const list = ref([])
const store = useStore();
const route = useRoute();

const userFunc = [
  { name: '个人名片', index: 'profile', path: '/', icon: User },
  { name: '成就', index: 'achievements', path: '/', icon: Medal },
  { name: '退出登录', index: 'logout', path: '/', icon: SwitchButton },
]

const menuItems = [
  { name: '概况', index: '1', path: '/', icon: HomeFilled },
  { name: '帮助文档', index: '2', path: '/', icon: Document },
  { name: '运维面板', index: '3', path: '/', icon: Monitor },
  { name: '用户设置', index: '4', path: '/', icon: Setting },
];

const locations = [
  {name: 'cn', desc: '中国大陆'},
  {name: 'hk', desc: '香港'},
  {name: 'jp', desc: '日本'},
  {name: 'us', desc: '美国'},
  {name: 'sg', desc: '新加坡'},
  {name: 'kr', desc: '韩国'},
  {name: 'de', desc: '德国'}
];

const checkNodes:any = ref([]);

const detail = reactive({
  show: false,
  id: -1
});

const clientList:any = computed(() => {
  if(checkNodes.value.length === 0) return list.value;
  else return list.value.filter((item:any) => checkNodes.value.indexOf(item.location) >= 0)
});

const displayClientDetails = (id:number) => {
  detail.show = true;
  detail.id = id;
}

const updateList = () => {
  if (route.name === 'monitor') {
    get('/api/monitor/list', (data:any) => {list.value = data;} )
  }
}
setInterval(updateList, 10000);
updateList();

const register = reactive({
  show: false,
  token: ''
})

const refreshToken = () => get('/api/monitor/register', (token:string) => register.token = token)

const terminal = reactive({
  show: false,
  id: -1
})

function openTerminal(id){
    terminal.show = true;
    terminal.id = id;
    detail.show = false;
}

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
}

const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
}

</script>

<template>
    <el-container style="height: 100vh; box-sizing: border-box;">
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="aside-header">
          <el-avatar class="user-avatar">
            <el-image src="/src/assets/images/df_avatar.jpg"></el-image>
          </el-avatar>
          <div class="user-info" v-show="!isCollapse">
            <div class="username">用户名</div>
            <div class="email">电子邮件</div>
          </div>
        </div>
        <div class="user-func-buttons" v-show="!isCollapse">
          <el-button
            v-for="item in userFunc"
            :key="item.index"
            :index="item.index"
            class="circle-button"
            circle
          >
            <el-tooltip
              :content="item.name"
              placement="right"
              :show-after="200"
            >
              <el-icon><component :is="item.icon" /></el-icon>
            </el-tooltip>
          </el-button>
        </div>

        <el-menu
          :default-active="activeIndex"
          class="el-menu-vertical"
          :collapse="isCollapse"
          @open="handleOpen"
          @close="handleClose"
        >
          <el-menu-item 
            v-for="item in menuItems" 
            :key="item.index"
            :index="item.index"
          >
            <el-icon><component :is="item.icon" /></el-icon>
            <template #title>{{ item.name }}</template>
          </el-menu-item>

          <el-divider />
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div style="display: flex; align-items: center; gap: 8px;">
              <i class="fa-solid fa-server"></i>
              <div>管理主机列表 </div>
              <div style="color: gray; font-size: 14px">在这里可以远程管理已注册的主机实例，实现轻松运维</div>
            </div>

            <div>
              <el-button @click="register.show = true" type="primary" plain>
                添加新主机
                <el-icon><Plus/></el-icon>
              </el-button>
            </div>
          </div>
        </el-header>

        <el-main style="padding: 10px; max-width: 100%;">
          <el-form style="max-width: 800px; margin: 0 auto 20px;">
            <el-form-item label="Hostname">
              <el-input placeholder="请输入主机名称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-checkbox-group v-model="checkNodes">
                <el-checkbox v-for="node in locations" :key="node" :value="node.name">
                  <span :class="`flag-icon flag-icon-${node.name}`"></span>
                  <span style="font-size: 13px; margin-left: 10px">{{ node.desc }}</span>
                </el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-form>
          <div v-if="clientList.length" style="display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 10px;">
            <server-card v-for="item in clientList" :data="item" :update="updateList" @click="displayClientDetails(item?.id)"/>
          </div>
          <el-empty v-else description="还没有任何主机哦，点右上角添加一个吧 ~~"></el-empty>
        </el-main>
        <el-footer>Footer</el-footer>
      </el-container>

      <el-drawer size="540" v-model="detail.show" :show-close="false"
                  :with-header="false" v-if="list.length" @close="detail.id=-1">
        <ClientDetail :id="detail.id" :update="updateList" @delete="updateList"></ClientDetail>
      </el-drawer>

      <el-drawer v-model="register.show" direction="btt" :with-header="false"
                    style="width: 600px; margin: 10px auto;" size="320" @open="refreshToken">
        <RegisterCard :token="register.token"/>
      </el-drawer>
    </el-container>
</template>

<style scoped>
:deep(.el-drawer){
  margin: 10px;
  height: calc(100% - 20px);
  border-radius: 10px;
}

.el-menu-vertical:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

.el-menu-vertical {
  border-right: none;
}

.aside-header {
  padding: 20px 0;
  text-align: center;
}

.user-avatar {
  width: 40px;
  height: 40px;
  margin: 0 auto;
}

.user-info {
  margin-top: 8px;
}

.username {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.email {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.collapse-trigger {
  position: absolute;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: transform 0.3s;
}

.is-collapse {
  transform: rotate(180deg);
}

.user-func-buttons {
  position: relative;
  height: 40px;
  margin: 12px;
  margin-top: -15px;
  padding: 8px 0;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
}

.user-func-buttons .circle-button {
  position: relative;
  margin: 0 4px;
}

.user-func-buttons .circle-button:nth-child(1) {
  transform: translateY(4px) rotate(-8deg);
}

.user-func-buttons .circle-button:nth-child(2) {
  transform: translateY(-4px);
}

.user-func-buttons .circle-button:nth-child(3) {
  transform: translateY(4px) rotate(8deg);
}

.circle-button {
  width: 36px;
  height: 36px;
  padding: 8px;
  border: 1px solid var(--el-border-color);
  background-color: var(--el-bg-color);
  transition: all 0.3s;
}

.circle-button:hover {
  background-color: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
}

.dark .main-container .main-content {
  background-color: #232323;
}
</style>