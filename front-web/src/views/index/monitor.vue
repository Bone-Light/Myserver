<script setup lang="ts">

</script>

<template>
    <el-container style="width: 100vw; height: 100vh">
      <el-aside width="80px">
        <el-avatar>
          <el-image src="\src\assets\images\df_avatar.jpg"></el-image>
        </el-avatar>
        <div>用户名</div>
        <div>电子邮件</div> 
      </el-aside>
      <el-container>
        <el-header>
          <div>
            管理主机列表
          </div>
          <div style="color: gray; font-size: 12px;">在这里可以远程管理已注册的主机实例，实现轻松运维</div>
        </el-header>
        <el-main>Main</el-main>
        <el-footer>Footer</el-footer>
      </el-container>

      <el-drawer>
        Drawer
      </el-drawer>
    </el-container>
</template>

<style scoped>

</style>