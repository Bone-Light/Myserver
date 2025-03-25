<script setup lang="ts">

import ServerCard from "@/views/component/ServerCard.vue";
</script>

<template>
    <el-container style="width: 100vw; height: 100vh; box-sizing: border-box;">
      <el-aside width="80px">
        <el-avatar>
          <el-image src="\src\assets\images\df_avatar.jpg"></el-image>
        </el-avatar>
        <div>用户名</div>
        <div>电子邮件</div> 
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
              <el-button type="primary">
                添加新主机
                <el-icon><plus /></el-icon>
              </el-button>
            </div>
          </div>
        </el-header>

        <el-main>
          Main
          <server-card/>
        </el-main>
        <el-footer>Footer</el-footer>
      </el-container>

      <el-drawer>
        Drawer
      </el-drawer>
    </el-container>
</template>

<style scoped>

</style>