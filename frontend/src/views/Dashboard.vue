<template>
  <div class="dashboard-container">
    <!-- 欢迎区域 -->
    <section class="welcome-section">
      <div class="container">
        <div class="welcome-content">
          <h1 class="welcome-title">欢迎回来，{{ userInfo?.username }}</h1>
          <p class="welcome-subtitle">{{ familyName }}</p>
          <div class="welcome-stats">
            <div class="stat-item">
              <span class="stat-value">{{ currentDate }}</span>
              <span class="stat-label">今日日期</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ daysSinceJoin }}</span>
              <span class="stat-label">加入天数</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 内容网格 -->
    <div class="container dashboard-grid">
      <!-- 左侧区域 -->
      <div class="left-section">
        <!-- 本月积分模块 -->
        <section class="points-section card">
          <div class="section-header">
            <h2 class="section-title">本月积分</h2>
            <span class="points-total">{{ monthlyPoints }}</span>
          </div>
          <div class="points-progress">
            <div class="progress-bar">
              <div class="progress-fill" :style="{ width: pointsProgress + '%' }"></div>
            </div>
            <div class="progress-info">
              <span>进度：{{ pointsProgress }}%</span>
              <span>目标：{{ pointsGoal }}</span>
            </div>
          </div>
          <div class="points-history">
            <h3>积分记录</h3>
            <div class="history-list">
              <div v-for="record in pointsRecords" :key="record.id" class="history-item">
                <span class="history-reason">{{ record.reason }}</span>
                <span class="history-points" :class="{ positive: record.points > 0, negative: record.points < 0 }">
                  {{ record.points > 0 ? '+' : '' }}{{ record.points }}
                </span>
              </div>
            </div>
          </div>
        </section>

        <!-- 许愿池模块 -->
        <section class="wishing-well-section card">
          <div class="section-header">
            <h2 class="section-title">许愿池</h2>
            <button class="btn btn-primary btn-sm" @click="openWishModal">许下心愿</button>
          </div>
          <div class="wishes-container">
            <div v-for="wish in wishes" :key="wish.id" class="wish-card" :class="{ 'fulfilled': wish.fulfilled }">
              <div class="wish-content">
                <h3 class="wish-title">{{ wish.title }}</h3>
                <p class="wish-description">{{ wish.description }}</p>
                <div class="wish-footer">
                  <span class="wish-date">{{ formatDate(wish.createdAt) }}</span>
                  <span v-if="wish.fulfilled" class="wish-status fulfilled">已实现</span>
                  <span v-else class="wish-status pending">进行中</span>
                </div>
              </div>
              <div class="wish-actions">
                <button v-if="!wish.fulfilled" class="btn btn-success btn-xs" @click="fulfillWish(wish.id)">
                  标记实现
                </button>
                <button class="btn btn-danger btn-xs" @click="deleteWish(wish.id)">删除</button>
              </div>
            </div>
          </div>
        </section>
      </div>

      <!-- 右侧区域 -->
      <div class="right-section">
        <!-- 人生拼图模块 -->
        <section class="puzzle-section card">
          <div class="section-header">
            <h2 class="section-title">人生拼图</h2>
            <button class="btn btn-primary btn-sm" @click="addPuzzlePiece">添加拼图</button>
          </div>
          <div class="puzzle-grid">
            <div v-for="piece in puzzlePieces" :key="piece.id" class="puzzle-piece">
              <div class="piece-content">
                <div class="piece-icon">{{ getPieceIcon(piece.category) }}</div>
                <h3 class="piece-title">{{ piece.title }}</h3>
                <div class="piece-progress">
                  <div class="progress-bar small">
                    <div class="progress-fill" :style="{ width: piece.progress + '%' }"></div>
                  </div>
                  <span class="progress-text">{{ piece.progress }}%</span>
                </div>
              </div>
            </div>
            <div class="puzzle-piece add-piece" @click="addPuzzlePiece">
              <div class="add-piece-content">
                <div class="plus-icon">+</div>
                <span>添加拼图</span>
              </div>
            </div>
          </div>
          
          <!-- 拼图统计 -->
          <div class="puzzle-stats">
            <div class="stat-item">
              <span class="stat-value">{{ puzzleStats.total }}</span>
              <span class="stat-label">总拼图数</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ puzzleStats.completed }}</span>
              <span class="stat-label">已完成</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ puzzleStats.inProgress }}</span>
              <span class="stat-label">进行中</span>
            </div>
          </div>
        </section>
      </div>
    </div>

    <!-- 许愿弹窗 -->
    <div v-if="showWishModal" class="modal-overlay" @click="closeWishModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>许下心愿</h3>
          <button class="close-btn" @click="closeWishModal">×</button>
        </div>
        <div class="modal-body">
          <div class="form-group">
            <label for="wishTitle">心愿标题</label>
            <input type="text" id="wishTitle" v-model="newWish.title" placeholder="简短描述你的心愿">
          </div>
          <div class="form-group">
            <label for="wishDescription">心愿描述</label>
            <textarea id="wishDescription" v-model="newWish.description" rows="4" placeholder="详细描述你的心愿..."></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn btn-secondary" @click="closeWishModal">取消</button>
          <button class="btn btn-primary" @click="submitWish" :disabled="!newWish.title">许愿</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Dashboard',
  data() {
    return {
      userInfo: null,
      monthlyPoints: 0,
      pointsGoal: 1000,
      pointsProgress: 0,
      pointsRecords: [],
      wishes: [],
      puzzlePieces: [],
      puzzleStats: {
        total: 0,
        completed: 0,
        inProgress: 0
      },
      showWishModal: false,
      newWish: {
        title: '',
        description: ''
      }
    }
  },
  computed: {
    familyName() {
      return this.userInfo?.familyName || '家庭'
    },
    currentDate() {
      const now = new Date()
      return now.toLocaleDateString('zh-CN', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        weekday: 'long'
      })
    },
    daysSinceJoin() {
      // 模拟数据，实际应该从用户注册日期计算
      return 15
    }
  },
  mounted() {
    this.loadUserData()
    this.loadDashboardData()
  },
  methods: {
    loadUserData() {
      // 从localStorage加载用户信息
      const userStr = localStorage.getItem('user')
      if (userStr) {
        try {
          this.userInfo = JSON.parse(userStr)
        } catch (e) {
          console.error('解析用户信息失败:', e)
        }
      }
    },
    loadDashboardData() {
      // 模拟加载仪表盘数据
      this.loadPointsData()
      this.loadWishesData()
      this.loadPuzzleData()
    },
    loadPointsData() {
      // 模拟本月积分数据
      this.monthlyPoints = 350
      this.pointsProgress = Math.min((this.monthlyPoints / this.pointsGoal) * 100, 100)
      this.pointsRecords = [
        { id: 1, reason: '完成每日任务', points: 50, date: '今天 10:30' },
        { id: 2, reason: '连续登录奖励', points: 20, date: '昨天 09:15' },
        { id: 3, reason: '分享成就', points: 30, date: '昨天 14:45' },
        { id: 4, reason: '完成拼图', points: 100, date: '3天前 16:20' },
        { id: 5, reason: '心愿实现', points: 150, date: '5天前 08:30' }
      ]
    },
    loadWishesData() {
      // 模拟许愿池数据
      this.wishes = [
        {
          id: 1,
          title: '学会弹吉他',
          description: '希望在三个月内学会弹奏简单的吉他曲目',
          createdAt: '2024-01-15',
          fulfilled: false
        },
        {
          id: 2,
          title: '读完10本书',
          description: '今年的阅读计划，每月至少读完一本书',
          createdAt: '2024-01-05',
          fulfilled: true
        },
        {
          id: 3,
          title: '家庭旅行',
          description: '计划一次全家人的旅行，增进感情',
          createdAt: '2024-01-20',
          fulfilled: false
        }
      ]
    },
    loadPuzzleData() {
      // 模拟人生拼图数据
      this.puzzlePieces = [
        {
          id: 1,
          title: '家庭和谐',
          category: 'family',
          progress: 85
        },
        {
          id: 2,
          title: '事业发展',
          category: 'career',
          progress: 60
        },
        {
          id: 3,
          title: '健康生活',
          category: 'health',
          progress: 45
        },
        {
          id: 4,
          title: '学习成长',
          category: 'learning',
          progress: 70
        },
        {
          id: 5,
          title: '兴趣爱好',
          category: 'hobby',
          progress: 30
        },
        {
          id: 6,
          title: '社交网络',
          category: 'social',
          progress: 55
        }
      ]
      
      // 计算拼图统计
      this.puzzleStats.total = this.puzzlePieces.length
      this.puzzleStats.completed = this.puzzlePieces.filter(p => p.progress >= 100).length
      this.puzzleStats.inProgress = this.puzzlePieces.filter(p => p.progress > 0 && p.progress < 100).length
    },
    getPieceIcon(category) {
      const icons = {
        family: '🏠',
        career: '💼',
        health: '🏃',
        learning: '📚',
        hobby: '🎨',
        social: '👥'
      }
      return icons[category] || '🧩'
    },
    formatDate(dateStr) {
      const date = new Date(dateStr)
      return date.toLocaleDateString('zh-CN')
    },
    openWishModal() {
      this.showWishModal = true
      this.newWish = { title: '', description: '' }
    },
    closeWishModal() {
      this.showWishModal = false
    },
    submitWish() {
      if (!this.newWish.title) return
      
      // 模拟添加心愿
      const newId = this.wishes.length + 1
      this.wishes.push({
        id: newId,
        title: this.newWish.title,
        description: this.newWish.description,
        createdAt: new Date().toISOString().split('T')[0],
        fulfilled: false
      })
      
      this.$message.success('心愿已添加！')
      this.closeWishModal()
    },
    fulfillWish(wishId) {
      const wish = this.wishes.find(w => w.id === wishId)
      if (wish) {
        wish.fulfilled = true
        this.$message.success('恭喜实现心愿！')
        // 可以在这里添加积分奖励逻辑
      }
    },
    deleteWish(wishId) {
      if (confirm('确定要删除这个心愿吗？')) {
        this.wishes = this.wishes.filter(w => w.id !== wishId)
        this.$message.success('心愿已删除')
      }
    },
    addPuzzlePiece() {
      this.$message.info('拼图添加功能开发中...')
      // 实际项目中应该打开添加拼图的弹窗
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  min-height: calc(100vh - 60px);
  background-color: var(--background-color);
}

/* 欢迎区域 */
.welcome-section {
  background: linear-gradient(135deg, var(--primary-color), #6B8CB5);
  color: white;
  padding: 40px 0;
}

.welcome-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.welcome-title {
  font-size: 2.5rem;
  margin-bottom: 8px;
  font-weight: 700;
}

.welcome-subtitle {
  font-size: 1.2rem;
  margin-bottom: 30px;
  opacity: 0.9;
}

.welcome-stats {
  display: flex;
  gap: 40px;
  margin-top: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 0.9rem;
  opacity: 0.8;
}

/* 主内容网格 */
.dashboard-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 30px;
  padding: 30px 0;
}

.left-section, .right-section {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

/* 通用卡片样式 */
.card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 24px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid var(--border-color);
}

.section-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin: 0;
  color: var(--text-color);
}

/* 积分模块 */
.points-total {
  font-size: 1.8rem;
  font-weight: 700;
  color: var(--accent-color);
}

.points-progress {
  margin-bottom: 24px;
}

.progress-bar {
  height: 20px;
  background-color: #f0f0f0;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 8px;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4CAF50, #8BC34A);
  border-radius: 10px;
  transition: width 0.3s ease;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  font-size: 0.9rem;
  color: var(--light-text);
}

.points-history h3 {
  font-size: 1.1rem;
  margin-bottom: 16px;
  color: var(--text-color);
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px;
  background-color: var(--background-color);
  border-radius: 8px;
}

.history-reason {
  font-size: 0.95rem;
  color: var(--text-color);
}

.history-points {
  font-weight: 600;
  font-size: 1rem;
}

.history-points.positive {
  color: #4CAF50;
}

.history-points.negative {
  color: #f44336;
}

/* 许愿池模块 */
.wishes-container {
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-height: 500px;
  overflow-y: auto;
}

.wish-card {
  border: 2px solid #e0e0e0;
  border-radius: 10px;
  padding: 16px;
  position: relative;
  transition: all 0.3s ease;
}

.wish-card.fulfilled {
  border-color: #4CAF50;
  background-color: #f1f8e9;
}

.wish-title {
  font-size: 1.1rem;
  font-weight: 600;
  margin: 0 0 8px 0;
  color: var(--text-color);
}

.wish-description {
  font-size: 0.95rem;
  color: var(--light-text);
  margin: 0 0 12px 0;
  line-height: 1.5;
}

.wish-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.85rem;
  color: var(--light-text);
}

.wish-status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 500;
}

.wish-status.fulfilled {
  background-color: #4CAF50;
  color: white;
}

.wish-status.pending {
  background-color: #ff9800;
  color: white;
}

.wish-actions {
  display: flex;
  gap: 8px;
  margin-top: 12px;
}

/* 人生拼图模块 */
.puzzle-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 24px;
}

.puzzle-piece {
  border: 2px solid var(--border-color);
  border-radius: 10px;
  padding: 20px;
  text-align: center;
  transition: all 0.3s ease;
  cursor: pointer;
}

.puzzle-piece:hover {
  border-color: var(--primary-color);
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.piece-icon {
  font-size: 2.5rem;
  margin-bottom: 12px;
}

.piece-title {
  font-size: 1rem;
  font-weight: 600;
  margin: 0 0 12px 0;
  color: var(--text-color);
}

.piece-progress {
  margin-top: 12px;
}

.progress-bar.small {
  height: 8px;
  margin-bottom: 4px;
}

.progress-text {
  font-size: 0.85rem;
  color: var(--light-text);
  font-weight: 500;
}

.add-piece {
  border: 2px dashed var(--border-color);
  background-color: #f9f9f9;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-piece-content {
  text-align: center;
  color: var(--light-text);
}

.plus-icon {
  font-size: 2rem;
  margin-bottom: 8px;
  color: var(--primary-color);
}

.puzzle-stats {
  display: flex;
  justify-content: space-around;
  padding-top: 20px;
  border-top: 2px solid var(--border-color);
}

/* 弹窗样式 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid var(--border-color);
}

.modal-header h3 {
  margin: 0;
  font-size: 1.2rem;
  color: var(--text-color);
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  cursor: pointer;
  color: var(--light-text);
}

.modal-body {
  padding: 24px;
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 24px;
  border-top: 1px solid var(--border-color);
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-color);
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px;
  border: 2px solid var(--border-color);
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: var(--primary-color);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .welcome-stats {
    flex-direction: column;
    gap: 20px;
  }
  
  .puzzle-grid {
    grid-template-columns: 1fr;
  }
  
  .puzzle-stats {
    flex-direction: column;
    gap: 16px;
    text-align: center;
  }
  
  .welcome-title {
    font-size: 2rem;
  }
}

/* 按钮样式 */
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  font-size: 0.95rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
}

.btn-primary {
  background-color: var(--primary-color);
  color: white;
}

.btn-primary:hover {
  background-color: var(--primary-dark);
}

.btn-secondary {
  background-color: #e0e0e0;
  color: var(--text-color);
}

.btn-secondary:hover {
  background-color: #d0d0d0;
}

.btn-success {
  background-color: #4CAF50;
  color: white;
}

.btn-success:hover {
  background-color: #45a049;
}

.btn-danger {
  background-color: #f44336;
  color: white;
}

.btn-danger:hover {
  background-color: #d32f2f;
}

.btn-sm {
  padding: 8px 16px;
  font-size: 0.85rem;
}

.btn-xs {
  padding: 4px 12px;
  font-size: 0.75rem;
}
</style>