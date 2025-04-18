<script setup lang="ts">
import {fitByUnit,percentageToStatus,osNameToIcon,copyIp,rename} from '@/tools';
const props:any = defineProps({
  data: Object,
  update: Function,
})
</script>

<template>
  <el-card class="server-card" shadow="hover">
    <template #header>
      <div class="card-header">
        <div class="server-info">
          <div class="hostname-container">
            <span :class="`flag-icon flag-icon-${data?.location}`"></span>
            <span class="hostname">{{ data?.name }}</span>
            <i class="fa-solid fa-pen-to-square edit-icon" @click.stop="rename(data?.id, data?.name, props?.update)"/>
          </div>
          <div class="os">
            操作系统:
            <i :style="{color: osNameToIcon(data?.osName).color}"
                :class="`fa-brands ${osNameToIcon(data?.osName).icon}`"></i>
            <span>{{`${data?.osName} ${data?.osVersion}`}}</span>
          </div>
        </div>
        <div class="status" v-if="data?.online">
          <i style="color:#67C23A" class="fa-solid fa-circle-play online-icon"></i>
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
        <span class="ip-address">{{data?.ip}}</span>
        <i class="fa-solid fa-copy copy-icon" @click.stop="copyIp(data?.ip)"/>
      </div>
      
      <div class="hardware-info">
        <div class="processor">处理器: {{data?.cpuName}}</div>
        <div class="specs">
          <span class="cpu-info">
            <i class="fa-solid fa-microchip"></i>
            <span> {{`${data?.cpuCore.toFixed(1)} CPU`}}</span>
          </span>
          <span class="memory-info">
            <i class="fa-solid fa-memory"></i>
            <span>{{`${data?.memory.toFixed(1)} GB`}}</span>
          </span>
        </div>
      </div>

      <div class="resource-usage">
          <div class="resource-label"> {{`CPU ${(data?.cpuUsage * 100).toFixed(1)} %`}}</div>
          <el-progress :percentage="data?.cpuUsage * 100" :stroke-width="5" :show-text="false" :status="percentageToStatus(data?.cpuUsage * 100)"/>
          <div class="resource-label">内存 {{`${data?.memoryUsage.toFixed(1)} GB`}}</div>
          <el-progress :percentage="data?.memoryUsage / data?.memory * 100" :stroke-width="5" :show-text="false" :status="percentageToStatus(data?.memoryUsage / data?.memory * 100)"/>
      </div>

      <div class="network-traffic">
        <span class="traffic-label">网络流量</span>
        <div class="traffic-data">
          <span class="upload"> 
            <i class="fa-solid fa-arrow-up"></i> 
            <span>{{` ${fitByUnit(data?.networkUpload, 'KB')}`}}</span>
          </span>
          <span class="divider">|</span>
          <span class="download"> 
            <i class="fa-solid fa-arrow-down"></i> 
            <span>{{`${fitByUnit(data?.networkDownload, 'KB')}`}}</span>
          </span>
        </div>
      </div>
    </div>
  </el-card>
</template>

<style scoped>
.server-card {
  width: 360px;
  height: 280px;
  border-radius: 8px;
  transition: all 0.3s;
  background: var(--el-card-bg-color);
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
  border-bottom: var(--el-border);
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
  color: var(--el-text-color-regular);
}

.edit-icon {
  cursor: pointer;
  font-size: 12px;
  color: var(--el-text-color-regular);
  transition: color 0.2s;
}

.os {
  font-size: 12px;
  color: var(--el-text-color-regular);
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
  background: var(--el-card-bg-color);
  border-radius: 4px;
}

.copy-icon {
  cursor: pointer;
  font-size: 13px;
  color: var(--el-text-color-regular);
  padding: 5px;
  transition: all 0.2s;

  &:hover{
    color: #409EFF;
    scale: 1.05;
  }
}

.hardware-info {
  padding: 3px;
  background: var(--el-card-bg-color);
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
  background: var(--el-card-bg-color);
  padding: 8px;
  border-radius: 4px;
}

.resource-label {
  font-size: 11px;
  color: var(--el-text-color-regular);
  font-weight: 500;
  min-width: 60px;
}

.network-traffic {
  display: flex;
  align-items: center;
  gap: 10px;
  background: var(--el-card-bg-color);
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
  color: var(--el-text-color-regular);
}

.upload,
.download {
  display: flex;
  align-items: center;
  gap: 5px;
}

.divider {
  color: var(--el-text-color);
}
</style>