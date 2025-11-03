import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  server: {
    // 配置代理，将/api开头的请求转发到后端服务
    proxy: {
      '/api': {
        target: 'http://localhost:5026', // 后端服务运行在5026端口
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/api/puzzle/v2')
      }
    },
    port: 3000 // 前端开发服务器端口
  }
})