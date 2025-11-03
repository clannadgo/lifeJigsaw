import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import message from './utils/message.js'

// 导入主题样式
import './assets/styles/theme.css'

// 创建Vue应用实例
const app = createApp(App)

// 使用路由
app.use(router)

// 使用消息提示插件
app.use(message)

// 挂载应用
app.mount('#app')
