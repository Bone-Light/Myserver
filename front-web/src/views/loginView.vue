<script setup lang="ts">

const url: string = '/src/assets/images/bkLogin.jpg'
</script>

<template>
  <div class="out-box">
    <div class="background-image">
      <el-image :src="url" fit="cover" />
    </div>
    <router-view/>
  </div>
</template>

<style scoped>
.out-box {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.background-image {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.background-image :deep(.el-image) {
  width: 100%;
  height: 100%;
  filter: brightness(0.7);
}


</style>