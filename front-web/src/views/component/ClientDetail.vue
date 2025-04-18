<script setup lang="ts">
import {reactive, watch, computed} from "vue";
import {get,post} from '@/net'
import {copyIp, osNameToIcon, cpuNameToImage, rename, percentageToStatus, fitByUnit} from "@/tools";
import {ElMessage,ElMessageBox} from "element-plus";
import {Delete} from "@element-plus/icons-vue"
import RuntimeHistory from "@/views/component/RuntimeHistory.vue";

const locations = [
  {name: 'cn', desc: '中国大陆'},
  {name: 'hk', desc: '香港'},
  {name: 'jp', desc: '日本'},
  {name: 'us', desc: '美国'},
  {name: 'sg', desc: '新加坡'},
  {name: 'kr', desc: '韩国'},
  {name: 'de', desc: '德国'}
];

const props:any =  defineProps({
  id:Number,
  update: Function
});

const nodeEdit:any = reactive({
  name: '',
  location: ''
});

const enableNodeEdit = ()=>{
  details.editNode = true;
  nodeEdit.name = details.base.node;
  nodeEdit.location = details.base.location
}

const submitNodeEdit = () => {
  post('/api/monitor/node', {
    id: props.id,
    node: nodeEdit.name,
    location: nodeEdit.location
  }, () => {
    details.editNode = false
    updateDetail()
    ElMessage.success('节点信息已更新')
  })
}

const details:any = reactive({
  base:{},
  runtime: {
    list:[]
  },
  editNode: false
})

const emits = defineEmits(['delete', 'terminal']);

setInterval(() => {
  if(props.id !== -1 && details.runtime) {
    get(`/api/monitor/runtime-now?clientId=${props.id}`, (data:any) => {
      if(details.runtime.list.length >= 360)
        details.runtime.list.splice(0, 1);
      details.runtime.list.push(data)
    })
  }
}, 10000)
const now:any = computed(() => details.runtime.list[details.runtime.list.length - 1])

const init = (value:any) => {
  if(value !== -1){
    details.base = {}
    details.runtime = {list:[]}
    get(`/api/monitor/details?clientId=${value}`, (data:any) => Object.assign(details.base, data));
    get(`/api/monitor/runtime-history?clientId=${value}`, (data:any) => Object.assign(details.runtime, data));
  }
};

function deleteClient() {
  ElMessageBox.confirm('删除此主机后所有统计数据都将丢失，您确定要这样做吗？', '删除主机', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    get(`/api/monitor/delete?clientId=${props.id}`, () => {
      emits('delete')
      props.update()
      ElMessage.success('主机已成功移除')
    })
  }).catch(() => {})
}

function updateDetail(){
  props.update();
  init(props.id);
}
watch(()=>props.id, init, {immediate: true});
</script>

<template>
  <el-scrollbar>
    <div class="client-details" v-loading="Object.keys(details.base).length === 0">
      <div v-if="Object.keys(details.base).length">
        <div class="title" style="display: flex; justify-content: space-between">
          <div>
            <i class="fa-solid fa-server"/>
            服务器信息
          </div>
          <el-button :icon="Delete" @click="deleteClient" type="danger" plain text>删除此主机</el-button>
        </div>
        <el-divider style="margin:10px 0"/>
        <div class="detail-list">
          <div>
            <span>服务器ID</span>
            <span>{{details.base?.id}}</span>
          </div>
          <div>
            <span>服务器名称</span>
            <span>{{details.base?.name}}</span>
            <i class="fa-solid fa-pen-to-square edit-icon" @click.stop="rename(details.base?.id, details.base?.name, updateDetail)"/>
          </div>
          <div>
            <span>服务器状态</span>
            <span>
                <i style="color:#67C23A" class="fa-solid fa-circle-play online-icon" v-if="details.base?.online"></i>
                <i class="fa-solid fa-circle-stop offline-icon" v-else></i>
                <span>{{details.base?.online? "运行中" : "离线"}}</span>
            </span>
          </div>
          <div v-if="!details.editNode">
            <span>服务器节点</span>
            <span :class="`flag-icon flag-icon-${details.base?.location}`"></span>&nbsp;
            <span>{{details.base.node}}</span> &nbsp;
            <i class="fa-solid fa-pen-to-square edit-icon" @click.stop="enableNodeEdit"/>
          </div>
          <div v-else>
            <span>服务器节点</span>
            <div style="display: inline-block; height: 15px">
              <div style="display:flex">
                <el-select v-model="nodeEdit.location" style="width: 80px;" size="small">
                  <el-option v-for="item in locations" :value="item.name">
                    <span :class="`flag-icon flag-icon-${item.name}`"/>
                    {{item.desc}}
                  </el-option>
                </el-select>
                <el-input v-model="nodeEdit.name" style="margin-left: 10px" size="small" placeholder="请输入节点名称.."/>
                <div>
                  <i class="fa-solid fa-check interact-item" @click.stop="submitNodeEdit"/>
                </div>
              </div>
            </div>
          </div>

          <div>
            <span>公网ip:</span>
            <span class="ip-address">{{details.base?.ip}}</span> &nbsp;
            <i class="fa-solid fa-copy copy-icon" @click.stop="copyIp(details.base?.ip)"/>
          </div>

          <div style="display: flex">
              <span>处理器</span>
              <span>{{details.base?.cpuName}}</span>
              <el-image style="margin-left: 10px; height: 20px" :src="cpuNameToImage(details.base?.cpuName)"/>
          </div>

          <div>
            <span>硬件配置信息</span>
            <span class="cpu-info">
              <i class="fa-solid fa-microchip" style="margin-right: 10px"></i>
              <span> {{`${details.base?.cpuCore.toFixed(1)} CPU`}}</span>
            </span> &nbsp;
            <span class="memory-info">
              <i class="fa-solid fa-memory" style="margin-right: 10px"></i>
              <span>{{`${details.base?.memory.toFixed(1)} GB`}}</span>
            </span>
          </div>

          <div>
            <span>操作系统:</span>
            <i :style="{color: osNameToIcon(details.base?.osName).color}"
               style="margin-right: 10px"
               :class="`fa-brands ${osNameToIcon(details.base?.osName).icon}`"></i>
            <span>{{`${details.base?.osName} ${details.base?.osVersion}`}}</span>
          </div>

        </div>
        <div class="title">
          <i class="fa-solid fa-gauge-high"/>
          实时监控
        </div>
        <el-divider style="margin: 10px 0"/>
        <div v-if="details.base.online && now" v-loading="!details.runtime.list.length" style="display: flex;">
          <el-progress type="dashboard" :width="100" :percentage="now.cpuUsage*100" :status="percentageToStatus(now.cpuUsage*100)">
            <div style="font-size: 17px; font-weight: bold; color: initial">CPU</div>
            <div style="font-size: 14px; color: grey; margin-top: 5px">{{`${(now.cpuUsage*100).toFixed(1)} %`}}</div>
          </el-progress>
          <el-progress type="dashboard" :width="100" :percentage="now.memoryUsage / details.base.memory * 100" :status="percentageToStatus(now.memoryUsage / details.base.memory * 100)" style="margin-left: 20px">
            <div style="font-size: 17px; font-weight: bold; color: initial">内存</div>
            <div style="font-size: 14px; color: grey; margin-top: 5px">{{`${now.memoryUsage.toFixed(1)} GB`}}</div>
          </el-progress>

          <div style="flex: 1; margin-left: 30px; display: flex; flex-direction: column; height: 80px">
            <div style="flex: 1; font-size: 14px">
              <div>实时网络速度</div>
              <i style="color: orange; margin-right: 3px" class="fa-solid fa-arrow-up"></i>
              <span>{{`${fitByUnit(now.networkUpload, 'KB')}/s`}}</span>
              <el-divider direction="vertical"/>
              <i style="color: dodgerblue; margin-right: 3px" class="fa-solid fa-arrow-down"></i>
              <span>{{`${fitByUnit(now.networkDownload, 'KB')}/s`}}</span>
            </div>
            <div>
              <div style="font-size: 13px; display: flex; justify-content: space-between">
                <div>
                  <i class="fa-solid fa-hard-drive"/>
                  <span>磁盘总容量</span>
                </div>
                <div>{{now.diskUsage.toFixed(1)}} GB / {{details.runtime.disk.toFixed(1)}} GB</div>
              </div>
              <el-progress type="line" :status="percentageToStatus(now.diskUsage / details.runtime.disk * 100)" :percentage="now.diskUsage / details.runtime.disk * 100" :show-text="false"/>
            </div>
          </div>
        </div>
        <runtime-history style="margin-top: 20px" :data="details.runtime.list"/>
      </div>
      <el-empty description="服务器处于离线状态，请检查服务器是否正常运行" v-else/>
    </div>
  </el-scrollbar>
</template>

<style scoped>
.client-details{
  height: 100%;
  padding: 20px;

  .title{
    color: dodgerblue;
    font-size: 18px;
    font-weight: bold;
  }

  .edit-icon{
    margin-left: 8px;
    &:hover{
      color: #409EFF;
      cursor: pointer;
    }
  }

  .interact-item{
    margin-left: 10px;
    &:hover{
      color: #409EFF;
      cursor: pointer;
    }

  }

  .copy-icon{
    &:hover{
      color: #409EFF;
      cursor: pointer;
    }
  }

  .detail-list{
    font-size: 14px;

    & div{
      margin-bottom: 10px;

      & span:first-child{
        color:grey;
        font-size: 13px;
        font-weight: normal;
        width: 120px;
        display: inline-block;
      }

      &span{
        font-weight: bold;
      }
    }
  }
}
</style>