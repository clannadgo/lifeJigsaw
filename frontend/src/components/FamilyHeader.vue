<template>
  <header class="family-header">
    <div class="header-content">
      <div class="header-left">
        <div class="family-info">
          <div class="family-icon">🏠</div>
          <div class="family-details">
            <h1 class="family-name">{{ familyName }}</h1>
            <p class="family-motto">{{ familyMotto }}</p>
          </div>
        </div>
      </div>
      <div class="header-right">
        <div class="user-info" v-if="userInfo">
          <span class="username">{{ userInfo.username }}</span>
        </div>
        <button class="logout-btn" @click="handleLogout">
          <span class="logout-icon">👋</span>
          <span>退出登录</span>
        </button>
      </div>
    </div>
  </header>
</template>

<script>
export default {
  name: 'FamilyHeader',
  props: {
    familyName: {
      type: String,
      default: '温馨家庭'
    },
    userInfo: {
      type: Object,
      default: () => null
    }
  },
  computed: {
    familyMotto() {
      // 根据familyName生成不同的家庭格言，增加个性化体验
      const mottos = {
        '温馨家庭': '相亲相爱，幸福美满',
        '快乐家庭': '笑口常开，幸福常在',
        '和谐家庭': '家和万事兴',
        '美满家庭': '家庭是心灵的港湾'
      }
      return mottos[this.familyName] || '共创美好未来'
    }
  },
  methods: {
    handleLogout() {
      this.$emit('logout')
    }
  }
}
</script>

<style scoped>
.family-header {
  background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
  color: #fff;
  padding: 1rem 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  position: relative;
  overflow: hidden;
}

.family-header::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: 
    radial-gradient(circle at 10% 20%, rgba(255, 255, 255, 0.1) 20%, transparent 20%),
    radial-gradient(circle at 80% 30%, rgba(255, 255, 255, 0.15) 15%, transparent 15%),
    radial-gradient(circle at 40% 70%, rgba(255, 255, 255, 0.1) 18%, transparent 18%),
    radial-gradient(circle at 70% 80%, rgba(255, 255, 255, 0.12) 12%, transparent 12%);
  background-size: 100% 100%;
  pointer-events: none;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: relative;
  z-index: 1;
}

.header-left {
  display: flex;
  align-items: center;
}

.family-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.family-icon {
  font-size: 2.5rem;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { transform: scale(1); }
  50% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

.family-details h1 {
  margin: 0;
  font-size: 1.8rem;
  font-weight: 700;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.family-motto {
  margin: 0.25rem 0 0 0;
  font-size: 0.9rem;
  opacity: 0.9;
  font-style: italic;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.user-info {
  display: flex;
  align-items: center;
  background: rgba(255, 255, 255, 0.1);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  backdrop-filter: blur(10px);
}

.username {
  font-weight: 500;
  font-size: 0.95rem;
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 0.6rem 1.2rem;
  border-radius: 25px;
  cursor: pointer;
  font-size: 0.95rem;
  font-weight: 500;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
}

.logout-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.logout-icon {
  font-size: 1.1rem;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
  
  .family-info {
    justify-content: center;
  }
  
  .family-details h1 {
    font-size: 1.5rem;
  }
  
  .header-right {
    flex-direction: column;
    gap: 0.75rem;
  }
}
</style>