<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import { ElCard } from 'element-plus'

interface SystemInfo {
  hostname: string
  ip: string
  version: string
  cpuCores: number
  memory: string
}

const systemInfo = ref<SystemInfo>({
  hostname: 'Apple M3 Max',
  ip: '192.168.0.10',
  version: 'macOS 14.2',
  cpuCores: 14,
  memory: '36.0 GB'
})

let cpuChart: echarts.ECharts | null = null
let memoryChart: echarts.ECharts | null = null
let networkChart: echarts.ECharts | null = null
let diskChart: echarts.ECharts | null = null

const initCharts = () => {
  cpuChart = echarts.init(document.getElementById('cpuChart') as HTMLElement)
  memoryChart = echarts.init(document.getElementById('memoryChart') as HTMLElement)
  networkChart = echarts.init(document.getElementById('networkChart') as HTMLElement)
  diskChart = echarts.init(document.getElementById('diskChart') as HTMLElement)
  
  const cpuOption = {
    title: { text: 'CPU使用率', left: 'center', top: 10, textStyle: { color: '#333', fontSize: 16, fontWeight: 500 } },
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255, 255, 255, 0.9)', textStyle: { color: '#333' }, axisPointer: { type: 'line' } },
    grid: { left: '5%', right: '5%', bottom: '8%', top: '15%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['23:00', '23:01', '23:02', '23:03', '23:04'],
      axisLabel: { color: '#666' },
      axisLine: { lineStyle: { color: '#ddd' } }
    },
    yAxis: {
      type: 'value',
      axisLabel: { color: '#666', formatter: '{value}%' },
      splitLine: { lineStyle: { color: '#eee', type: 'dashed' } }
    },
    series: [{
      data: [8.7, 12.3, 15.1, 10.5, 9.8],
      type: 'line',
      smooth: true,
      symbolSize: 6,
      lineStyle: { width: 3, color: '#409EFF' },
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(64, 158, 255, 0.3)' }, { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }]) }
    }]
  }

  const memoryOption = {
    title: { text: '内存使用', left: 'center', top: 10, textStyle: { color: '#333', fontSize: 16, fontWeight: 500 } },
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255, 255, 255, 0.9)', textStyle: { color: '#333' }, axisPointer: { type: 'line' } },
    grid: { left: '5%', right: '5%', bottom: '8%', top: '15%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['23:00', '23:01', '23:02', '23:03', '23:04'],
      axisLabel: { color: '#666' },
      axisLine: { lineStyle: { color: '#ddd' } }
    },
    yAxis: {
      type: 'value',
      axisLabel: { color: '#666', formatter: '{value}%' },
      splitLine: { lineStyle: { color: '#eee', type: 'dashed' } }
    },
    series: [{
      data: [65.2, 71.4, 68.5, 73.2, 70.8],
      type: 'line',
      smooth: true,
      symbolSize: 6,
      lineStyle: { width: 3, color: '#67C23A' },
      areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(103, 194, 58, 0.3)' }, { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }]) }
    }]
  }

  const networkOption = {
    title: { text: '网络流量', left: 'center', top: 10, textStyle: { color: '#333', fontSize: 16, fontWeight: 500 } },
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255, 255, 255, 0.9)', textStyle: { color: '#333' }, axisPointer: { type: 'line' } },
    legend: { data: ['上传', '下载'], bottom: 0, textStyle: { color: '#666' }, icon: 'circle' },
    grid: { left: '5%', right: '5%', bottom: '15%', top: '15%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['23:00', '23:01', '23:02', '23:03', '23:04'],
      axisLabel: { color: '#666' },
      axisLine: { lineStyle: { color: '#ddd' } }
    },
    yAxis: {
      type: 'value',
      axisLabel: { color: '#666' },
      splitLine: { lineStyle: { color: '#eee', type: 'dashed' } }
    },
    series: [
      {
        name: '上传',
        type: 'line',
        smooth: true,
        symbolSize: 6,
        lineStyle: { width: 3 },
        itemStyle: { color: '#67C23A' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(103, 194, 58, 0.3)' }, { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }]) },
        data: [2, 1.5, 3, 2.5, 2.8]
      },
      {
        name: '下载',
        type: 'line',
        smooth: true,
        symbolSize: 6,
        lineStyle: { width: 3 },
        itemStyle: { color: '#E6A23C' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(230, 162, 60, 0.3)' }, { offset: 1, color: 'rgba(230, 162, 60, 0.1)' }]) },
        data: [3.5, 4, 3.8, 4.2, 3.9]
      }
    ]
  }

  const diskOption = {
    title: { text: '磁盘IO', left: 'center', top: 10, textStyle: { color: '#333', fontSize: 16, fontWeight: 500 } },
    tooltip: { trigger: 'axis', backgroundColor: 'rgba(255, 255, 255, 0.9)', textStyle: { color: '#333' }, axisPointer: { type: 'line' } },
    legend: { data: ['读取', '写入'], bottom: 0, textStyle: { color: '#666' }, icon: 'circle' },
    grid: { left: '5%', right: '5%', bottom: '15%', top: '15%', containLabel: true },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: ['23:00', '23:01', '23:02', '23:03', '23:04'],
      axisLabel: { color: '#666' },
      axisLine: { lineStyle: { color: '#ddd' } }
    },
    yAxis: {
      type: 'value',
      name: 'MB/s',
      nameTextStyle: { color: '#666' },
      axisLabel: { color: '#666' },
      splitLine: { lineStyle: { color: '#eee', type: 'dashed' } }
    },
    series: [
      {
        name: '读取',
        type: 'line',
        smooth: true,
        symbolSize: 6,
        lineStyle: { width: 3 },
        itemStyle: { color: '#409EFF' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(64, 158, 255, 0.3)' }, { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }]) },
        data: [25, 35, 28, 32, 30]
      },
      {
        name: '写入',
        type: 'line',
        smooth: true,
        symbolSize: 6,
        lineStyle: { width: 3 },
        itemStyle: { color: '#F56C6C' },
        areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(245, 108, 108, 0.3)' }, { offset: 1, color: 'rgba(245, 108, 108, 0.1)' }]) },
        data: [18, 22, 20, 25, 23]
      }
    ]
  }

  cpuChart.setOption(cpuOption)
  memoryChart.setOption(memoryOption)
  networkChart.setOption(networkOption)
  diskChart.setOption(diskOption)
}

onMounted(() => {
  initCharts()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
  cpuChart?.dispose()
  memoryChart?.dispose()
  networkChart?.dispose()
  diskChart?.dispose()
})

const handleResize = () => {
  cpuChart?.resize()
  memoryChart?.resize()
  networkChart?.resize()
  diskChart?.resize()
}
</script>

<template>
  <div class="client-details">
    <el-card class="system-info" :body-style="{ padding: '20px' }">
      <div class="info-grid">
        <div class="info-item">
          <span class="label">主机名：</span>
          <span class="value">{{ systemInfo.hostname }}</span>
        </div>
        <div class="info-item">
          <span class="label">IP地址：</span>
          <span class="value">{{ systemInfo.ip }}</span>
        </div>
        <div class="info-item">
          <span class="label">系统版本：</span>
          <span class="value">{{ systemInfo.version }}</span>
        </div>
        <div class="info-item">
          <span class="label">CPU核心：</span>
          <span class="value">{{ systemInfo.cpuCores }} CPU核心</span>
        </div>
        <div class="info-item">
          <span class="label">内存大小：</span>
          <span class="value">{{ systemInfo.memory }}</span>
        </div>
      </div>
    </el-card>

    <div class="charts-container">
      <el-card class="chart-card">
        <div id="cpuChart" class="chart"></div>
      </el-card>
      <el-card class="chart-card">
        <div id="memoryChart" class="chart"></div>
      </el-card>
      <el-card class="chart-card">
        <div id="networkChart" class="chart"></div>
      </el-card>
      <el-card class="chart-card">
        <div id="diskChart" class="chart"></div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.client-details {
  padding: 24px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.system-info {
  margin-bottom: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 24px;
  padding: 8px;
}

.info-item {
  color: #333;
  font-size: 14px;
  padding: 8px;
  border-radius: 8px;
  background-color: #fff;
  transition: all 0.3s ease;
}

.info-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.label {
  color: #666;
  margin-right: 12px;
  font-weight: 500;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.chart-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.chart-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.chart {
  height: 280px;
  width: 100%;
  padding: 12px;
}

:deep(.el-card) {
  --el-card-bg-color: #fff;
  border: none;
}
</style>