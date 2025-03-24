import { createApp } from 'vue'
import App from './App.vue'
import router from '@/router'
import axios from "axios";

import {createPinia} from "pinia";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
axios.defaults.baseURL = 'http://localhost:8081'
import ElementPlus from 'element-plus' // 新增
import 'element-plus/dist/index.css'    // 引入样式

const app = createApp(App)
const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)
app.use(router)
app.use(pinia)
app.use(ElementPlus)
app.mount('#app')
