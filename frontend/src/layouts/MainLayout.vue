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
        
        <!-- 右侧用户信息或登录按钮 -->
        <div class="user-info">
          <div v-if="userInfo">
            <router-link to="/dashboard" class="family-name-link">
              <span class="family-name">{{ userInfo.familyName }}</span>
            </router-link>
            <router-link to="/admin" v-if="userInfo.isAdmin" class="admin-btn">管理后台</router-link>
            <button class="logout-btn" @click="handleLogout">退出登录</button>
          </div>
          <div v-else>
            <router-link to="/login" class="login-btn">登录</router-link>
            <router-link to="/register" class="register-btn">注册</router-link>
          </div>
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
    console.log('data初始化 - 开始初始化数据...')
    const initializedData = {
      userInfo: this.initializeUserInfo()
    }
    console.log('data初始化 - 完成，userInfo值:', initializedData.userInfo)
    return initializedData
  },
  created() {
    console.log('created钩子执行 - 开始，userInfo值:', this.userInfo)
    console.log('created钩子 - localStorage当前user值:', localStorage.getItem('user'))
    console.log('created钩子 - localStorage当前token值:', localStorage.getItem('token'))
    // 监听storage事件，当localStorage中的用户信息发生变化时更新
    window.addEventListener('storage', this.handleStorageChange)
    console.log('created钩子执行 - 结束，userInfo值:', this.userInfo)
  },
  mounted() {
    console.log('mounted钩子执行 - 开始，userInfo值:', this.userInfo)
    // 组件挂载后再次确认用户信息是否正确
    this.loadUserInfo()
    console.log('mounted钩子执行 - 结束，userInfo值:', this.userInfo)
    console.log('mounted钩子 - 最终userInfo.isAdmin值:', this.userInfo.isAdmin)
  },
  beforeUnmount() {
    console.log('beforeUnmount钩子执行 - 移除事件监听')
    // 移除事件监听
    window.removeEventListener('storage', this.handleStorageChange)
  },
  watch: {
    userInfo: {
      handler(newVal, oldVal) {
        console.log('watch - userInfo变化:', {oldVal, newVal})
        console.log('watch - 新userInfo.isAdmin值:', newVal?.isAdmin)
      },
      deep: true
    }
  },
  methods: {
    initializeUserInfo() {
      console.log('===== initializeUserInfo 开始 =====')
      // 在data初始化时就尝试从localStorage获取用户信息
      const userStr = localStorage.getItem('user')
      console.log('localStorage.user原始值:', userStr)
      if (userStr) {
        try {
          const userInfo = JSON.parse(userStr)
          console.log('解析后的userInfo完整对象:', JSON.stringify(userInfo))
          console.log('userInfo.isAdmin存在性检查:', userInfo.hasOwnProperty('isAdmin'))
          console.log('userInfo.isAdmin原始值:', userInfo.isAdmin)
          // 确保isAdmin字段存在
          if (userInfo.isAdmin === undefined) {
            console.log('isAdmin为undefined，设置为false')
            userInfo.isAdmin = false
          }
          console.log('初始化处理后userInfo.isAdmin值:', userInfo.isAdmin)
          console.log('===== initializeUserInfo 返回值 =====', JSON.stringify(userInfo))
          return userInfo
        } catch (e) {
          console.error('初始化用户信息解析失败:', e)
        }
      } else {
        console.log('localStorage.user不存在')
      }
      console.log('===== initializeUserInfo 返回空对象 =====')
      return {}
    },
    loadUserInfo() {
      console.log('===== loadUserInfo 开始 =====');
      const userStr = localStorage.getItem('user');
      console.log('loadUserInfo - localStorage.user原始值:', userStr);
      
      // 额外检查token
      const token = localStorage.getItem('token');
      console.log('loadUserInfo - localStorage.token存在:', !!token);
      
      if (userStr) {
        try {
          const userInfo = JSON.parse(userStr);
          console.log('loadUserInfo - 解析后的完整userInfo:', JSON.stringify(userInfo));
          console.log('loadUserInfo - userInfo.isAdmin类型:', typeof userInfo.isAdmin);
          console.log('loadUserInfo - userInfo.isAdmin值:', userInfo.isAdmin);
          
          // 确保isAdmin字段存在且为布尔值
          if (userInfo.isAdmin === undefined) {
            console.log('loadUserInfo - isAdmin为undefined，设置为false');
            userInfo.isAdmin = false;
          }
          if (typeof userInfo.isAdmin !== 'boolean') {
            console.log('loadUserInfo - isAdmin类型错误，转换为布尔值:', userInfo.isAdmin);
            userInfo.isAdmin = Boolean(userInfo.isAdmin);
          }
          
          console.log('loadUserInfo - 处理后isAdmin值:', userInfo.isAdmin);
          
          // 保存前最后检查
          console.log('loadUserInfo - 设置this.userInfo前值:', this.userInfo);
          this.userInfo = userInfo;
          console.log('loadUserInfo - 设置this.userInfo后值:', this.userInfo);
          console.log('loadUserInfo - 设置后this.userInfo.isAdmin:', this.userInfo.isAdmin);
          
        } catch (e) {
          console.error('loadUserInfo - 解析失败:', e);
          console.log('loadUserInfo - 解析失败，设置空对象');
          this.userInfo = {};
        }
      } else {
        console.log('loadUserInfo - localStorage.user不存在');
        this.userInfo = {};
      }
      console.log('===== loadUserInfo 结束 ===== 最终userInfo:', JSON.stringify(this.userInfo));
    },
    handleStorageChange(event) {
      console.log('===== handleStorageChange 开始 =====')
      console.log('storage事件key:', event.key)
      console.log('storage事件newValue:', event.newValue)
      console.log('storage事件oldValue:', event.oldValue)
      
      if (event.key === 'user') {
        console.log('storage事件 - 用户信息变化，重新加载')
        this.loadUserInfo()
      } else if (event.key === 'token') {
        console.log('storage事件 - token变化，token新值存在:', !!event.newValue)
      }
      console.log('===== handleStorageChange 结束 =====')
    },
    handleLogout() {
      console.log('===== handleLogout 开始 =====')
      // 清除localStorage中的token和用户信息
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      this.userInfo = {}
      console.log('handleLogout - 清除后userInfo:', this.userInfo)
      
      // 跳转到登录页
      this.$router.push('/login')
      
      // 显示退出成功消息
      this.$message.success('退出登录成功')
      console.log('===== handleLogout 结束 =====')
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

.family-name-link {
  text-decoration: none;
}

.family-name {
  font-size: 16px;
  font-weight: 500;
  padding: 8px 16px;
  background-color: rgba(255,255,255,0.2);
  border-radius: 20px;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
}

.family-name:hover {
  color: #e8f4fd;
  text-decoration: underline;
  background-color: rgba(255,255,255,0.3);
}

.admin-btn {
  background-color: #4ecdc4;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  text-decoration: none;
  font-size: 14px;
  margin-left: 10px;
  cursor: pointer;
}

.admin-btn:hover {
  background-color: #45b7aa;
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

.login-btn, .register-btn {
  padding: 8px 16px;
  margin-left: 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  text-decoration: none;
  transition: background-color 0.3s;
}

.login-btn {
  background-color: rgba(255,255,255,0.2);
  color: white;
}

.login-btn:hover {
  background-color: rgba(255,255,255,0.3);
}

.register-btn {
  background-color: #4caf50;
  color: white;
}

.register-btn:hover {
  background-color: #45a049;
}

.content {
  flex: 1;
  max-width: 1200px;
  width: 100%;
  margin: 0 auto;
  padding: 20px;
}
</style>