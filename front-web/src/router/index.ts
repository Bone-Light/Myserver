// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

// 定义路由配置
const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'indexView',
        component: () => import('@/views/indexView.vue'),
        children: [
            {
                path: '',
                name: 'index',
                component: () => import('@/views/index/index.vue'),
            },{
                path: 'recommend',
                name: 'recommend',
                component: () => import('@/views/index/recommend.vue'),
            },{
                path: 'document',
                name: 'document',
                component: () => import('@/views/index/document.vue'),
            },{
                path: 'monitor',
                name: 'monitor',
                component: () => import('@/views/index/monitor.vue'),
            }
        ]
    },{
        path: '/login',
        name: 'loginView',
        component: () => import('@/views/loginView.vue'),
        children: [
            {
              path: '',
              name: 'login',
              component: () => import('@/views/login/login.vue'),
            },{
                path: 'forget',
                name: 'forget',
                component: () => import('@/views/login/forget.vue'),
            }
        ],
    }
]

// 创建并导出路由实例
const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),  // 使用 Vue 3 中的 history 模式
    routes
})

export default router