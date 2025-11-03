<template>
  <div class="main-layout">
    <!-- 顶部导航栏 -->
    <header class="navbar">
      <div class="navbar-container">
        <!-- 左侧Logo -->
        <div class="logo">
          <router-link to="/">
            <h1>人生拼图</h1>
          </router-link>
        </div>
        
        <!-- 右侧用户信息 -->
        <div class="user-info" v-if="userInfo">
          <span class="family-name">{{ userInfo.familyName }}</span>
          <button class="logout-btn" @click="handleLogout">退出登录</button>
        </div>
      </div>
    </header>
    
    <!-- 主内容区 -->
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  name: 'MainLayout',
  data() {
    return {
      userInfo: null
    }
  },
  mounted() {
    // 初始化时从localStorage获取用户信息
    this.loadUserInfo()
    
    // 监听storage事件，当localStorage中的用户信息发生变化时更新
    window.addEventListener('storage', this.handleStorageChange)
  },
  beforeUnmount() {
    // 移除事件监听
    window.removeEventListener('storage', this.handleStorageChange)
  },
  methods: {
    loadUserInfo() {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        try {
          this.userInfo = JSON.parse(userStr)
        } catch (e) {
          console.error('解析用户信息失败:', e)
          this.userInfo = null
        }
      }
    },
    handleStorageChange(event) {
      if (event.key === 'user') {
        this.loadUserInfo()
      }
    },
    handleLogout() {
      // 清除localStorage中的token和用户信息
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      this.userInfo = null
      
      // 跳转到登录页
      this.$router.push('/login')
      
      // 显示退出成功消息
      this.$message.success('退出登录成功')
    }
  }
}
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.navbar {
  background-color: #4a6fa5;
  color: white;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.navbar-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo h1 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
}

.logo a {
  color: white;
  text-decoration: none;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.family-name {
  font-size: 16px;
  font-weight: 500;
  padding: 8px 16px;
  background-color: rgba(255,255,255,0.2);
  border-radius: 20px;
}

.logout-btn {
  padding: 8px 16px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: #d32f2f;
}

.content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
}
</style>