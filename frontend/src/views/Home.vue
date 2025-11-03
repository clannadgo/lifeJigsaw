<template>
  <div class="home-container">
    <!-- 头部区域 -->
    <header class="header">
      <div class="container">
        <div class="header-content">
          <h1 class="site-title">
            <span class="puzzle-icon">🧩</span>
            人生拼图
          </h1>
          <p class="site-subtitle">拼凑你的理想生活，记录生命的每一个精彩瞬间</p>
        </div>
      </div>
    </header>

    <!-- 主要内容区域 -->
    <main class="main-content">
      <!-- 介绍卡片 -->
      <section class="intro-section">
        <div class="container">
          <div class="card intro-card">
            <h2 class="section-title">欢迎来到人生拼图</h2>
            <p class="intro-text">
              人生就像一幅拼图，每一个经历、每一个选择都是其中的一块。在这里，你可以记录生活的点点滴滴，
              拼凑出属于自己的完美人生画卷。无论是家庭、事业、健康还是兴趣爱好，都能在这里找到合适的位置。
            </p>
            <div class="action-buttons">
              <router-link to="/login" class="btn btn-primary">登录</router-link>
              <router-link to="/register" class="btn btn-secondary">注册</router-link>
            </div>
          </div>
        </div>
      </section>

      <!-- 功能特性 -->
      <section class="features-section">
        <div class="container">
          <h2 class="section-title">功能特性</h2>
          <div class="features-grid">
            <div class="feature-card card">
              <div class="feature-icon">🏠</div>
              <h3>家庭管理</h3>
              <p>创建和管理你的家庭空间，与家人共享美好时光</p>
            </div>
            <div class="feature-card card">
              <div class="feature-icon">📝</div>
              <h3>生活记录</h3>
              <p>记录每一天的精彩瞬间，留下珍贵的回忆</p>
            </div>
            <div class="feature-card card">
              <div class="feature-icon">🎯</div>
              <h3>目标追踪</h3>
              <p>设定人生目标，追踪进度，实现自我突破</p>
            </div>
            <div class="feature-card card">
              <div class="feature-icon">📊</div>
              <h3>数据分析</h3>
              <p>通过数据了解你的生活习惯，优化生活质量</p>
            </div>
          </div>
        </div>
      </section>

      <!-- 人生拼图主题区域 -->
      <section class="puzzle-theme-section">
        <div class="container">
          <div class="puzzle-grid">
            <div class="puzzle-piece puzzle-piece-1">
              <div class="puzzle-content">
                <h3>家庭</h3>
                <p>温馨的港湾，爱的源泉</p>
              </div>
            </div>
            <div class="puzzle-piece puzzle-piece-2">
              <div class="puzzle-content">
                <h3>事业</h3>
                <p>实现自我价值的舞台</p>
              </div>
            </div>
            <div class="puzzle-piece puzzle-piece-3">
              <div class="puzzle-content">
                <h3>健康</h3>
                <p>生命的基础，幸福的保障</p>
              </div>
            </div>
            <div class="puzzle-piece puzzle-piece-4">
              <div class="puzzle-content">
                <h3>兴趣</h3>
                <p>生活的调味剂，心灵的寄托</p>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="container">
        <p class="footer-text">© 2025 人生拼图 - 拼凑你的理想生活</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data() {
    return {
      // 可以在这里添加组件数据
    }
  },
  mounted() {
    // 组件挂载后的逻辑
  },
  methods: {
    // 组件方法
  }
}
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 头部样式 */
.header {
  background: linear-gradient(135deg, var(--primary-color), #6B8CB5);
  color: var(--white);
  padding: 80px 0 60px;
  text-align: center;
}

.site-title {
  font-size: 3.5rem;
  margin-bottom: 16px;
  font-weight: 700;
  color: var(--white);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
}

.puzzle-icon {
  font-size: 4rem;
  animation: rotatePuzzle 3s ease-in-out infinite;
}

@keyframes rotatePuzzle {
  0%, 100% { transform: rotate(0deg) scale(1); }
  50% { transform: rotate(5deg) scale(1.05); }
}

.site-subtitle {
  font-size: 1.25rem;
  max-width: 600px;
  margin: 0 auto;
  opacity: 0.9;
}

/* 介绍区域 */
.intro-section {
  padding: 60px 0;
}

.intro-card {
  max-width: 800px;
  margin: 0 auto;
  text-align: center;
}

.section-title {
  text-align: center;
  margin-bottom: 32px;
  position: relative;
}

.section-title::after {
  content: '';
  display: block;
  width: 80px;
  height: 4px;
  background-color: var(--accent-color);
  margin: 16px auto 0;
  border-radius: 2px;
}

.intro-text {
  font-size: 1.1rem;
  line-height: 1.8;
  margin-bottom: 32px;
  color: var(--light-text);
}

.action-buttons {
  display: flex;
  gap: 16px;
  justify-content: center;
  flex-wrap: wrap;
}

/* 功能特性区域 */
.features-section {
  padding: 60px 0;
  background-color: rgba(74, 111, 165, 0.05);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 24px;
  margin-top: 40px;
}

.feature-card {
  text-align: center;
  padding: 32px 24px;
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 20px;
  transition: transform 0.3s ease;
}

.feature-card:hover .feature-icon {
  transform: scale(1.1) rotate(5deg);
}

/* 拼图主题区域 */
.puzzle-theme-section {
  padding: 60px 0;
}

.puzzle-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0;
  max-width: 800px;
  margin: 0 auto;
}

.puzzle-piece {
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.puzzle-piece:hover {
  transform: scale(1.02);
  z-index: 10;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
}

.puzzle-piece-1 {
  background: linear-gradient(135deg, #4A6FA5, #6B8CB5);
  clip-path: polygon(0 0, 100% 0, 85% 100%, 0 85%);
}

.puzzle-piece-2 {
  background: linear-gradient(135deg, #FF6B6B, #FF8E8E);
  clip-path: polygon(15% 0, 100% 0, 100% 85%, 0 100%);
}

.puzzle-piece-3 {
  background: linear-gradient(135deg, #FFD166, #FFE08E);
  clip-path: polygon(0 15%, 100% 0, 85% 100%, 0 100%);
}

.puzzle-piece-4 {
  background: linear-gradient(135deg, #2A9D8F, #4ECDC4);
  clip-path: polygon(15% 0, 100% 15%, 100% 100%, 0 100%);
}

.puzzle-content {
  text-align: center;
  color: var(--white);
  padding: 20px;
}

.puzzle-content h3 {
  font-size: 1.5rem;
  margin-bottom: 8px;
  color: var(--white);
}

.puzzle-content p {
  font-size: 1rem;
  opacity: 0.9;
}

/* 页脚样式 */
.footer {
  background-color: var(--primary-color);
  color: var(--white);
  padding: 30px 0;
  margin-top: auto;
}

.footer-text {
  text-align: center;
  opacity: 0.8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .site-title {
    font-size: 2.5rem;
  }
  
  .puzzle-icon {
    font-size: 3rem;
  }
  
  .puzzle-grid {
    grid-template-columns: 1fr;
    gap: 20px;
    max-width: 400px;
  }
  
  .puzzle-piece {
    clip-path: none;
    border-radius: 8px;
  }
}
</style>