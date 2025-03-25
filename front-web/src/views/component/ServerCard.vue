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
            <span> 92 cpu</span>
          </span>
          <span class="memory-info">
            <i class="fa-solid fa-memory"></i>
            <span> 64 GB</span>
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
  height: 257px;
  border-radius: 8px;
  transition: all 0.3s;
  background: #ffffff;
}

.server-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  border-bottom: 1.3px solid #f0f0f0;
}

.server-info {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.hostname-container {
  display: flex;
  align-items: center;
  gap: 4px;
}

.hostname {
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}

.edit-icon {
  cursor: pointer;
  font-size: 12px;
  color: #909399;
  transition: color 0.2s;
}

.os {
  font-size: 12px;
  color: #606266;
  display: flex;
  align-items: center;
  gap: 3px;
  padding: 1.3px 4px;
  background: rgba(0, 0, 0, 0.03);
  border-radius: 3px;
  width: fit-content;
}

.status {
  display: flex;
  align-items: center;
  font-size: 12px;
}

:deep(.el-card__body) {
  padding: 1.3px 5px;
}

:deep(.el-card__header) {
  padding: 4px 5px;
}

.card-content {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 12px;
  padding: 3px;
}

.ip-info {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 5px;
  background: #f8f9fa;
  border-radius: 4px;
}

.copy-icon {
  cursor: pointer;
  font-size: 13px;
  color: #909399;
  padding: 5px;
  transition: all 0.2s;
}

.hardware-info {
  padding: 3px;
  background: #f8f9fa;
  border-radius: 4px;
}

.specs {
  display: flex;
  gap: 10px;
  margin-top: 3px;
}

.resource-usage {
  display: flex;
  flex-direction: column;
  gap: 3px;
  background: #f8f9fa;
  padding: 8px;
  border-radius: 4px;
}

.resource-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.resource-label {
  font-size: 12px;
  color: #606266;
  font-weight: 500;
  min-width: 39px;
}

:deep(div.el-progress__text) {
  font-size: 14px!important;
}

:deep(.el-progress) {
  width: 100%;
}

:deep(.el-progress-bar__outer) {
  height: 5px !important;
  border-radius: 3px;
  background-color: rgba(0, 0, 0, 0.04);
}

.network-traffic {
  display: flex;
  align-items: center;
  gap: 10px;
  background: #f8f9fa;
  padding: 8px;
  border-radius: 4px;
}

.traffic-label {
  color: #606266;
  font-weight: 500;
  min-width: 64px;
}

.traffic-data {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #606266;
}

.upload,
.download {
  display: flex;
  align-items: center;
  gap: 5px;
}

.divider {
  color: #dcdfe6;
}
</style>