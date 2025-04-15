<script setup lang="ts">
import {ref, onMounted, watch, onUnmounted} from "vue";
import {useRouter} from 'vue-router';
import { User, SwitchButton, Document, HomeFilled, Star, Monitor,Moon,Sunny } from '@element-plus/icons-vue';
import {useDark} from "@vueuse/core";
const router = useRouter();
const activeIndex = ref('首页');
const scrolled = ref(false);

const menuItems = [
  { name: '首页', index: '首页', path: '/', icon: HomeFilled },
  { name: '推荐', index: '推荐', path: '/recommend', icon: Star },
  { name: '文档', index: '文档', path: '/document', icon: Document },
  { name: '运维管理', index: '运维管理', path: '/monitor', icon: Monitor }
];

const handleSelect = (index: string) => {
  activeIndex.value = index;
  const selectedItem:any = menuItems.find(item => item.index === index);
  if (selectedItem) {
    router.push(selectedItem.path);
  }
};

// 监听滚动事件，改变header样式
const handleScroll = () => {
  scrolled.value = window.scrollY > 20;
};

const dark = useDark();

// 监听路由变化，更新活跃菜单项
watch(() => router.currentRoute.value.path, (newPath) => {
  const item = menuItems.find(item => item.path === newPath || (newPath === '/' && item.path === '/'));
  if (item) {
    activeIndex.value = item.index;
  }
}, { immediate: true });

// 初始化活跃菜单项和滚动监听
onMounted(() => {
  const path = router.currentRoute.value.path;
  const item = menuItems.find(item => item.path === path || (path === '/' && item.path === '/'));
  if (item) {
    activeIndex.value = item.index;
  }
  
  window.addEventListener('scroll', handleScroll);
  handleScroll(); // 初始检查
});

// 组件卸载时移除事件监听
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>

<template>
  <el-container class="app-container">
    <el-header :class="{ 'scrolled': scrolled }">
      <div class="header-content">
        <div class="logo-area" @click="router.push('/')">
          <el-image class="logo" src="/src/assets/images/WK_Icon.png"></el-image>
          <span class="title">封灯云运维</span>
        </div>
        <el-menu
          :default-active="activeIndex"
          class="nav-menu"
          mode="horizontal"
          @select="handleSelect"
          :ellipsis="false"
          background-color="transparent"
        >
          <el-menu-item v-for="item in menuItems" :key="item.index" :index="item.index">
            <el-icon><component :is="item.icon" /></el-icon>
            <span class="menu-text">{{ item.name }}</span>
          </el-menu-item>
        </el-menu>
        <el-switch class="switch"
                   v-model="dark"
                   active-color="#424242"
                   :active-action-icon="Moon"
                   :inactive-action-icon="Sunny"
        />
        <div class="right-area">
          <el-tooltip content="个人中心" placement="bottom" :enterable="false">
            <el-button class="user-btn" type="primary" link>
              <el-icon><User /></el-icon>
              <span class="Hidspan">个人中心</span>
            </el-button>
          </el-tooltip>
          <el-tooltip content="退出登录" placement="bottom" :enterable="false">
            <el-button class="logout-btn" type="danger" link>
              <el-icon><SwitchButton /></el-icon>
              <span class="Hidspan">退出登录</span>
            </el-button>
          </el-tooltip>
        </div>
      </div>
    </el-header>
    <el-main>
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <div class="fade-container"> <!-- 添加容器 -->
            <component :is="Component" />
          </div>
        </transition>
      </router-view>
    </el-main>
  </el-container>
</template>

<style scoped>
.el-header {
  background-color: rgba(255, 255, 255, 0.95);
  border-bottom: 1px solid rgba(230, 230, 230, 0.5);
  padding: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  height: 60px;
  position: sticky;
  top: 0;
  z-index: 100;
  transition: all 0.3s ease;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.header-content {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  max-width: 1400px;
  margin: 0 auto;
  transition: all 0.3s ease;
}

.scrolled {
  background-color: rgba(255, 255, 255, 0.98);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  height: 56px;
  border-bottom: 1px solid rgba(230, 230, 230, 0.8);
}

.logo-area {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 6px 10px;
  border-radius: 8px;
}

.logo-area:hover {
  transform: scale(1.02);
  background-color: rgba(68, 104, 235, 0.05);
}

.logo {
  width: 42px;
  height: 42px;
  object-fit: contain;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.1));
  transition: transform 0.3s ease;
}

.logo-area:hover .logo {
  transform: rotate(5deg);
}

.title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  background: linear-gradient(45deg, #4468eb, #36cfc9);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
  letter-spacing: 0.5px;
}

.nav-menu {
  border-bottom: none;
  margin-left: 20px;
  flex: 1;
  display: flex;
  justify-content: center;
}

:deep(.el-menu--horizontal) {
  border-bottom: none;
}

:deep(.el-menu-item) {
  font-size: 15px;
  font-weight: 500;
  height: 60px;
  line-height: 60px;
  color: #606266;
  transition: all 0.3s;
  padding: 0 24px;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  gap: 6px;
}

.scrolled :deep(.el-menu-item) {
  height: 56px;
  line-height: 56px;
}

:deep(.el-menu-item)::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 0;
  background: linear-gradient(180deg, rgba(68, 104, 235, 0.05), transparent);
  transition: height 0.3s ease;
  z-index: -1;
}

:deep(.el-menu-item:hover)::before {
  height: 100%;
}

.menu-text {
  position: relative;
  transition: all 0.3s;
}

:deep(.el-menu-item.is-active) {
  color: #4468eb;
  font-weight: bold;
  position: relative;
  background: linear-gradient(180deg, rgba(68, 104, 235, 0.08), transparent);
}

:deep(ul.el-menu.el-menu--horizontal.nav-menu){
  --el-menu-hover-bg-color: rgba(217, 227, 236, 0.212) !important;
}

:deep(.el-menu-item.is-active::after) {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: linear-gradient(90deg, #4468eb, #36cfc9);
  border-radius: 2px;
  transition: all 0.3s ease;
  box-shadow: 0 1px 4px rgba(68, 104, 235, 0.3);
}
:deep(.el-menu-item:hover:not(.is-active)) {
  color: #4468eb;
  background-color: rgba(68, 104, 235, 0.05) !important;
  box-shadow: 0 2px 8px rgba(68, 104, 235, 0.08);
}

:deep(.el-menu-item:hover:not(.is-active) .menu-text) {
  transform: translateY(-2px);
}

.right-area {
  display: flex;
  align-items: center;
  gap: 15px;
}

:deep(.el-button.is-link) {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
  padding: 8px 16px;
  border-radius: 20px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

:deep(.el-button.is-link)::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(45deg, rgba(255, 255, 255, 0.1), transparent);
  transform: translateY(100%);
  transition: transform 0.3s ease;
}

:deep(.el-button.is-link:hover)::before {
  transform: translateY(0);
}

:deep(.el-button.is-link .el-icon) {
  font-size: 16px;
  transition: transform 0.3s ease;
}

:deep(.el-button.is-link:hover .el-icon) {
  transform: scale(1.1);
}

:deep(.user-btn.is-link) {
  color: #4468eb;
  background-color: rgba(68, 104, 235, 0.05);
}

:deep(.user-btn.is-link:hover) {
  background-color: rgba(68, 104, 235, 0.1);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(68, 104, 235, 0.15);
}

:deep(.logout-btn.is-link) {
  color: #f56c6c;
  background-color: rgba(245, 108, 108, 0.05);
}

:deep(.logout-btn.is-link:hover) {
  background-color: rgba(245, 108, 108, 0.1);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(245, 108, 108, 0.15);
}

@media (max-width: 992px) {
  .header-content {
    padding: 0 16px;
  }

  .nav-menu {
    justify-content: flex-start;
    margin-left: 10px;
  }
  
  :deep(.el-menu-item) {
    padding: 0 16px;
  }

  .logo {
    width: 38px;
    height: 38px;
  }

  .title {
    font-size: 16px;
  }
}

@media (max-width: 768px) {
  .header-content {
    padding: 0 12px;
  }

  .logo-area {
    gap: 8px;
    padding: 4px 8px;
  }
  
  .title {
    font-size: 15px;
  }
  
  .nav-menu {
    margin-left: 8px;
    justify-content: center;
  }
  
  :deep(.el-menu-item) {
    padding: 0 10px;
    font-size: 14px;
    gap: 4px;
  }

  :deep(.el-menu-item .menu-text) {
    display: none;
  }

  :deep(.el-menu-item .el-icon) {
    font-size: 20px;
    margin: 0;
  }
  
  :deep(.el-button.is-link) {
    padding: 6px;
    border-radius: 50%;
    width: 36px;
    height: 36px;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  :deep(.el-button.is-link .Hidspan){
    display: none;
  }
  
  :deep(.el-button.is-link .el-icon) {
    margin: 0;
    font-size: 18px;
  }

  .right-area {
    gap: 8px;
  }
}

@media (max-width: 480px) {
  .header-content {
    padding: 0 8px;
  }

  .logo {
    width: 32px;
    height: 32px;
  }
  
  .title {
    font-size: 14px;
    display: none;
  }
  
  :deep(.el-menu-item) {
    padding: 0 8px;
  }

  :deep(.el-menu-item .el-icon) {
    font-size: 18px;
  }

  :deep(.el-button.is-link) {
    width: 32px;
    height: 32px;
  }

  :deep(.el-button.is-link .el-icon) {
    font-size: 16px;
  }

  .right-area {
    gap: 6px;
  }
}

/* 页面过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 主内容区域样式 */
.el-main {
  padding: 20px;
  max-width: 1400px;
  margin: 0 10px;
  min-height: calc(100vh - 60px);
}

.switch {
  margin: 0 20px;
}
</style>