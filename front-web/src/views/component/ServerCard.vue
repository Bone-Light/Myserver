<script setup lang="ts">
const online:boolean = false;
</script>

<template>
  <el-card class="server-card" shadow="hover">
    <template #header>
      <div class="card-header">
        <div class="server-info">
          <div class="hostname-container">
            <span class="flag-icon flag-icon-cn"></span>
            <span class="hostname">主机名字</span>
            <i class="fa-solid fa-pen-to-square edit-icon"/>
          </div>
          <div class="os">
            <i class="fa-brands fa-apple"></i>
            <span>macOS 14.2</span>
          </div>
        </div>
        <div class="status" v-if="online">
          <i class="fa-solid fa-circle-play online-icon"></i>
          <span>运行中</span>
        </div>
        <div class="status" v-else>
          <i class="fa-solid fa-circle-stop offline-icon"></i>
          <span>离线</span>
        </div>
      </div>
    </template>
    
    <div class="card-content">
      <div class="ip-info">
        <span>公网ip:</span>
        <span class="ip-address">192.162.0.10</span>
        <i class="fa-solid fa-copy copy-icon"/>
      </div>
      
      <div class="hardware-info">
        <div class="processor">处理器: Apple M4 Pro</div>
        <div class="specs">
          <span class="cpu-info">
            <i class="fa-solid fa-microchip"></i>
            <span>92 cpu</span>
          </span>
          <span class="memory-info">
            <i class="fa-solid fa-memory"></i>
            <span>64 GB</span>
          </span>
        </div>
      </div>

      <div class="resource-usage">
        <div class="resource-item">
          <span class="resource-label">CPU</span>
          <el-progress :percentage="50" :stroke-width="10" color="#67C23A"/>
        </div>
        <div class="resource-item">
          <span class="resource-label">内存</span>
          <el-progress :percentage="20" :stroke-width="10" color="#E6A23C"/>
        </div>
      </div>

      <div class="network-traffic">
        <span class="traffic-label">网络流量</span>
        <div class="traffic-data">
          <span class="upload"> 
            <i class="fa-solid fa-arrow-up"></i> 
            <span>10kb</span> 
          </span>
          <span class="divider">|</span>
          <span class="download"> 
            <i class="fa-solid fa-arrow-down"></i> 
            <span>10mb</span> 
          </span>
        </div>
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.server-card {
  width: 360px;
  height: 260px;
  border-radius: 6px;
  transition: all 0.3s;
  background: linear-gradient(to bottom, #ffffff, #f8f9fa);
}

.server-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 3px 6px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px;
  border-bottom: 1px solid #f0f0f0;
}

.server-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.hostname-container {
  display: flex;
  align-items: center;
  gap: 3px;
}

.hostname {
  font-weight: 600;
  font-size: 11px;
  color: #303133;
}

.edit-icon {
  cursor: pointer;
  font-size: 9px;
  color: #909399;
  transition: color 0.2s;
}

.edit-icon:hover {
  color: #409EFF;
}

.os {
  font-size: 9px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 2px;
  padding: 1px 3px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 2px;
  width: fit-content;
}

.os i {
  color: #4a5557;
}

.status {
  display: flex;
  align-items: center;
  font-size: 9px;
}

.online-icon {
  color: #67C23A;
}

.offline-icon {
  color: #8a8a8a;
}

.status span {
  margin-left: 2px;
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 2px;
  font-size: 9px;
  padding: 4px;
}

.ip-info {
  display: flex;
  align-items: center;
  gap: 2px;
}

.ip-address {
  color: #606266;
}

.copy-icon {
  color: #409EFF;
  cursor: pointer;
  transition: transform 0.2s;
}

.copy-icon:hover {
  transform: scale(1.1);
}

.hardware-info {
  margin-top: 2px;
}

.processor {
  font-weight: 500;
  margin-bottom: 2px;
}

.specs {
  display: flex;
  gap: 8px;
}

.cpu-info, .memory-info {
  display: flex;
  align-items: center;
  gap: 2px;
}

.resource-usage {
  margin: 2px 0;
  background: #f8f9fa;
  padding: 4px;
  border-radius: 3px;
}

.resource-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 2px;
  border-radius: 2px;
  background: rgba(255, 255, 255, 0.6);
}

.resource-label {
  font-size: 9px;
  color: #606266;
  font-weight: 500;
  min-width: 30px;
}

:deep(.el-progress) {
  width: 100%;
  margin-right: 8px;
}

:deep(.el-progress-bar__outer) {
  height: 4px !important;
  border-radius: 2px;
}

:deep(.el-progress-bar__inner) {
  border-radius: 2px;
}

.network-traffic {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #f8f9fa;
  padding: 4px;
  border-radius: 3px;
}

.traffic-label {
  font-size: 9px;
  color: #606266;
  font-weight: 500;
  min-width: 50px;
}

.traffic-data {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 9px;
}

.upload, .download {
  display: flex;
  align-items: center;
  gap: 3px;
  padding: 2px 4px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 2px;
}

.upload i {
  color: #F56C6C;
}

.download i {
  color: #409EFF;
}

.divider {
  display: none;
}
</style>