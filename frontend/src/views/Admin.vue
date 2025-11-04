<template>
  <div class="admin-container">
    <!-- 管理后台头部 -->
    <div class="admin-header">
      <h1>管理后台</h1>
      <router-link to="/dashboard" class="back-btn">返回首页</router-link>
    </div>

    <!-- 侧边导航 -->
    <div class="admin-layout">
      <aside class="sidebar">
        <nav class="admin-nav">
          <ul>
            <li class="nav-item" :class="{ active: activeModule === 'scores' }" @click="activeModule = 'scores'">
              <span class="nav-icon">📊</span>
              <span class="nav-text">积分管理</span>
            </li>
          </ul>
        </nav>
      </aside>

      <!-- 主内容区域 -->
      <main class="admin-content">
        <!-- 积分管理模块 -->
        <div v-if="activeModule === 'scores'" class="scores-management">
          <div class="module-header">
            <h2>积分管理</h2>
          </div>

          <!-- 积分管理标签页 -->
          <div class="tabs">
            <div class="tab-buttons">
              <button 
                v-for="tab in scoreTabs" 
                :key="tab.key"
                class="tab-btn"
                :class="{ active: activeScoreTab === tab.key }"
                @click="activeScoreTab = tab.key"
              >
                {{ tab.label }}
              </button>
            </div>

            <!-- 每日任务积分管理 -->
            <div v-if="activeScoreTab === 'dailyTasks'" class="tab-content">
              <div class="tab-header">
                <h3>每日任务积分设置</h3>
                <button class="btn btn-primary btn-sm" @click="openDailyTaskModal">添加任务</button>
              </div>
              <div class="table-container">
                <table class="data-table">
                  <thead>
                    <tr>
                      <th>任务名称</th>
                      <th>任务描述</th>
                      <th>积分奖励</th>
                      <th>任务类型</th>
                      <th>状态</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="task in dailyTasks" :key="task.id">
                      <td>{{ task.taskName }}</td>
                      <td>{{ task.description }}</td>
                      <td>{{ task.scoreValue }}</td>
                      <td>{{ task.taskType }}</td>
                      <td>{{ task.status === 1 ? '启用' : '禁用' }}</td>
                      <td>
                        <button class="btn btn-edit" @click="editDailyTask(task)">编辑</button>
                        <button class="btn btn-delete" @click="deleteDailyTask(task.id)">删除</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- 学业追踪积分管理 -->
            <div v-if="activeScoreTab === 'academicTracking'" class="tab-content">
              <div class="tab-header">
                <h3>学业追踪积分设置</h3>
                <button class="btn btn-primary btn-sm" @click="openAcademicTrackingModal">添加项目</button>
              </div>
              <div class="table-container">
                <table class="data-table">
                  <thead>
                    <tr>
                      <th>项目名称</th>
                      <th>项目描述</th>
                      <th>积分奖励</th>
                      <th>追踪类型</th>
                      <th>状态</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="tracking in academicTrackings" :key="tracking.id">
                      <td>{{ tracking.projectName }}</td>
                      <td>{{ tracking.description }}</td>
                      <td>{{ tracking.scoreValue }}</td>
                      <td>{{ tracking.trackingType }}</td>
                      <td>{{ tracking.status === 1 ? '启用' : '禁用' }}</td>
                      <td>
                        <button class="btn btn-edit" @click="editAcademicTracking(tracking)">编辑</button>
                        <button class="btn btn-delete" @click="deleteAcademicTracking(tracking.id)">删除</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <!-- 扩展活动积分管理 -->
            <div v-if="activeScoreTab === 'extendedActivities'" class="tab-content">
              <div class="tab-header">
                <h3>扩展活动积分设置</h3>
                <button class="btn btn-primary btn-sm" @click="openExtendedActivityModal">添加活动</button>
              </div>
              <div class="table-container">
                <table class="data-table">
                  <thead>
                    <tr>
                      <th>活动名称</th>
                      <th>活动描述</th>
                      <th>积分奖励</th>
                      <th>活动类型</th>
                      <th>状态</th>
                      <th>操作</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="activity in extendedActivities" :key="activity.id">
                      <td>{{ activity.activityName }}</td>
                      <td>{{ activity.description }}</td>
                      <td>{{ activity.scoreValue }}</td>
                      <td>{{ activity.activityType }}</td>
                      <td>{{ activity.status === 1 ? '启用' : '禁用' }}</td>
                      <td>
                        <button class="btn btn-edit" @click="editExtendedActivity(activity)">编辑</button>
                        <button class="btn btn-delete" @click="deleteExtendedActivity(activity.id)">删除</button>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Admin',
  data() {
    return {
      activeModule: 'scores',
      scoreTabs: [
        { key: 'dailyTasks', label: '每日任务' },
        { key: 'academicTracking', label: '学业追踪' },
        { key: 'extendedActivities', label: '扩展活动' }
      ],
      activeScoreTab: 'dailyTasks',
      // 模拟数据
      dailyTasks: [
        { id: 1, taskName: '晨读英语', description: '每天早晨阅读英语15分钟', scoreValue: 5, taskType: '学习', status: 1 },
        { id: 2, taskName: '体育锻炼', description: '每天进行30分钟体育锻炼', scoreValue: 8, taskType: '健康', status: 1 },
        { id: 3, taskName: '做家务', description: '帮助家人完成家务', scoreValue: 3, taskType: '生活', status: 1 }
      ],
      academicTrackings: [
        { id: 1, projectName: '数学作业', description: '完成每日数学作业', scoreValue: 10, trackingType: '作业', status: 1 },
        { id: 2, projectName: '阅读计划', description: '每周阅读一本课外书', scoreValue: 15, trackingType: '阅读', status: 1 }
      ],
      extendedActivities: [
        { id: 1, activityName: '志愿者活动', description: '参与社区志愿者活动', scoreValue: 20, activityType: '公益', status: 1 },
        { id: 2, activityName: '兴趣小组', description: '参加兴趣小组活动', scoreValue: 12, activityType: '兴趣', status: 1 }
      ]
    }
  },
  mounted() {
    // 检查用户权限
    this.checkAdminPermission()
    // 后续可以从API加载数据
  },
  methods: {
    checkAdminPermission() {
      const userStr = localStorage.getItem('user')
      if (userStr) {
        try {
          const user = JSON.parse(userStr)
          if (!user.isAdmin) {
            this.$message.error('您没有管理员权限')
            this.$router.push('/dashboard')
          }
        } catch (e) {
          console.error('解析用户信息失败:', e)
          this.$router.push('/dashboard')
        }
      } else {
        this.$router.push('/login')
      }
    },
    // 每日任务相关方法
    openDailyTaskModal() {
      this.$message.info('打开添加每日任务弹窗')
      // 后续实现弹窗功能
    },
    editDailyTask(task) {
      this.$message.info(`编辑每日任务: ${task.taskName}`)
      // 后续实现编辑功能
    },
    deleteDailyTask(id) {
      if (confirm('确定要删除这个每日任务吗？')) {
        this.$message.success('每日任务删除成功')
        // 后续实现删除功能
      }
    },
    // 学业追踪相关方法
    openAcademicTrackingModal() {
      this.$message.info('打开添加学业追踪弹窗')
      // 后续实现弹窗功能
    },
    editAcademicTracking(tracking) {
      this.$message.info(`编辑学业追踪: ${tracking.projectName}`)
      // 后续实现编辑功能
    },
    deleteAcademicTracking(id) {
      if (confirm('确定要删除这个学业追踪项目吗？')) {
        this.$message.success('学业追踪项目删除成功')
        // 后续实现删除功能
      }
    },
    // 扩展活动相关方法
    openExtendedActivityModal() {
      this.$message.info('打开添加扩展活动弹窗')
      // 后续实现弹窗功能
    },
    editExtendedActivity(activity) {
      this.$message.info(`编辑扩展活动: ${activity.activityName}`)
      // 后续实现编辑功能
    },
    deleteExtendedActivity(id) {
      if (confirm('确定要删除这个扩展活动吗？')) {
        this.$message.success('扩展活动删除成功')
        // 后续实现删除功能
      }
    }
  }
}
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background-color: #f5f7fa;
}

.admin-header {
  background-color: #4a6fa5;
  color: white;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.admin-header h1 {
  margin: 0;
  font-size: 24px;
}

.back-btn {
  background-color: #ffffff;
  color: #4a6fa5;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  text-decoration: none;
  font-size: 14px;
  cursor: pointer;
}

.back-btn:hover {
  background-color: #f0f0f0;
}

.admin-layout {
  display: flex;
  min-height: calc(100vh - 80px);
}

.sidebar {
  width: 200px;
  background-color: #ffffff;
  box-shadow: 2px 0 4px rgba(0,0,0,0.1);
  padding: 20px 0;
}

.admin-nav ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #333;
}

.nav-item:hover {
  background-color: #f5f7fa;
}

.nav-item.active {
  background-color: #e8f4fd;
  color: #4a6fa5;
  border-left: 4px solid #4a6fa5;
}

.nav-icon {
  font-size: 18px;
  margin-right: 10px;
}

.nav-text {
  font-size: 14px;
}

.admin-content {
  flex: 1;
  padding: 20px;
}

.scores-management {
  background-color: #ffffff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.module-header h2 {
  margin: 0 0 20px 0;
  color: #333;
  font-size: 20px;
}

.tabs {
  border-top: 1px solid #e0e0e0;
  padding-top: 20px;
}

.tab-buttons {
  display: flex;
  border-bottom: 1px solid #e0e0e0;
  margin-bottom: 20px;
}

.tab-btn {
  padding: 10px 20px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
  color: #666;
  border-bottom: 2px solid transparent;
  transition: all 0.3s ease;
}

.tab-btn:hover {
  color: #4a6fa5;
}

.tab-btn.active {
  color: #4a6fa5;
  border-bottom-color: #4a6fa5;
}

.tab-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.tab-header h3 {
  margin: 0;
  color: #333;
  font-size: 18px;
}

.table-container {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.data-table th {
  background-color: #f5f7fa;
  font-weight: 600;
  color: #333;
}

.data-table tbody tr:hover {
  background-color: #f9f9f9;
}

.btn {
  padding: 6px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s ease;
}

.btn-primary {
  background-color: #4a6fa5;
  color: white;
}

.btn-primary:hover {
  background-color: #3a5a8a;
}

.btn-sm {
  padding: 4px 8px;
  font-size: 12px;
}

.btn-edit {
  background-color: #4ecdc4;
  color: white;
  margin-right: 5px;
}

.btn-edit:hover {
  background-color: #45b7aa;
}

.btn-delete {
  background-color: #ff6b6b;
  color: white;
}

.btn-delete:hover {
  background-color: #ee5253;
}
</style>