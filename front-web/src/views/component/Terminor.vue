<script setup lang="ts">
import axios from "axios";
import {onBeforeUnmount, onMounted, ref} from "vue";
import {ElMessage} from "element-plus";
import {AttachAddon} from "xterm-addon-attach/src/AttachAddon";
import {Terminal} from "xterm";

const props = defineProps({
  id: Number
});
const emits:any = defineEmits(['dispose']);
const terminalRef = ref();
const linkUrl:string = axios.defaults.baseURL as string;
const socket = new WebSocket(`${linkUrl.replace('http', 'ws')}/terminal/${props.id}`)

socket.onclose = event => {
  if(event.code !== 1000) ElMessage.warning(`连接失败: ${event.reason}`);
  else ElMessage.success('远程SSH连接已断开');
  emits('dispose');
}

const attachAddon = new AttachAddon(socket);
const term = new Terminal({
  lineHeight: 1.2,
  rows: 20,
  fontSize: 13,
  fontFamily: "Monaco, Menlo, Consolas, '', monospace",
  fontWeight: "bold",
  theme: {
    background: '#000000'
  },
  // 光标闪烁
  cursorBlink: true,
  cursorStyle: 'underline',
  scrollback: 100,
  tabStopWidth: 4,
});

term.loadAddon(attachAddon);
onMounted(() => {
  term.open(terminalRef.value);
  term.focus();
});

onBeforeUnmount( () => {
  socket.close();
  term.dispose();
});
</script>

<template>
  <div ref="terminalRef" class="xterm"/>
</template>

<style scoped>

</style>