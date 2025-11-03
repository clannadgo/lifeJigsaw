<template>
  <div class="register-container">
    <div class="register-form-wrapper">
      <div class="register-form">
        <h2 class="form-title">
          <span class="puzzle-icon">🧩</span>
          用户注册
        </h2>
        <form @submit.prevent="handleRegister">
          <div class="form-group">
            <label for="username">用户名</label>
            <input
              type="text"
              id="username"
              v-model="formData.username"
              placeholder="请输入用户名"
              required
            />
          </div>
          <div class="form-group">
            <label for="email">邮箱</label>
            <input
              type="email"
              id="email"
              v-model="formData.email"
              placeholder="请输入邮箱"
              required
            />
          </div>
          <div class="form-group">
            <label for="password">密码</label>
            <input
              type="password"
              id="password"
              v-model="formData.password"
              placeholder="请输入密码"
              required
            />
          </div>
          <div class="form-group">
            <label for="confirmPassword">确认密码</label>
            <input
              type="password"
              id="confirmPassword"
              v-model="confirmPassword"
              placeholder="请再次输入密码"
              required
            />
          </div>
          <div class="form-group">
            <label for="familyName">家庭名称</label>
            <input
              type="text"
              id="familyName"
              v-model="formData.familyName"
              placeholder="请输入家庭名称"
              required
            />
          </div>
          <button type="submit" class="btn btn-primary" :disabled="isLoading">
            {{ isLoading ? '注册中...' : '注册' }}
          </button>
        </form>
        <div class="form-footer">
          <p>已有账号？<router-link to="/login">立即登录</router-link></p>
        </div>
        <div v-if="errorMessage" class="error-message">
          {{ errorMessage }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { addUser } from '../api/user.js'

export default {
  name: 'Register',
  data() {
    return {
      formData: {
        username: '',
        email: '',
        password: '',
        familyName: ''
      },
      confirmPassword: '',
      isLoading: false,
      errorMessage: ''
    }
  },
  methods: {
    async handleRegister() {
      // 表单验证
      if (this.formData.password !== this.confirmPassword) {
        this.errorMessage = '两次输入的密码不一致'
        return
      }
      
      if (this.formData.username.length < 2) {
        this.errorMessage = '用户名至少需要2个字符'
        return
      }
      
      // 邮箱格式验证
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(this.formData.email)) {
        this.errorMessage = '请输入有效的邮箱地址'
        return
      }
      
      if (this.formData.password.length < 6) {
        this.errorMessage = '密码至少需要6个字符'
        return
      }
      
      if (!this.formData.familyName) {
        this.errorMessage = '请输入家庭名称'
        return
      }
      
      // 清除之前的错误信息
      this.errorMessage = ''
      this.isLoading = true
      
      try {
        const response = await addUser(this.formData)
        if (response && response.code === 200) {
          this.$message.success('注册成功！')
          // 注册成功后跳转到登录页面
          this.$router.push('/login')
        } else {
          this.errorMessage = response?.message || '注册失败，请重试'
        }
      } catch (error) {
        console.error('注册失败:', error)
        this.errorMessage = error.response?.data?.message || '用户名或家庭名称已存在'
      } finally {
        this.isLoading = false
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: var(--background-color);
  padding: 20px;
}

.register-form-wrapper {
  width: 100%;
  max-width: 480px;
}

.register-form {
  background-color: var(--white);
  border-radius: 12px;
  padding: 40px;
  box-shadow: var(--shadow);
}

.form-title {
  text-align: center;
  margin-bottom: 32px;
  font-size: 2rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  color: var(--primary-color);
}

.puzzle-icon {
  font-size: 2.2rem;
}

.form-group {
  margin-bottom: 24px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-color);
}

.form-group input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.form-group input:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

.btn-primary {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  font-weight: 500;
  background-color: var(--primary-color);
  color: var(--white);
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  margin-bottom: 20px;
}

.btn-primary:hover:not(:disabled) {
  background-color: var(--primary-dark);
}

.btn-primary:disabled {
  background-color: var(--disabled-color);
  cursor: not-allowed;
}

.form-footer {
  text-align: center;
  color: var(--light-text);
}

.form-footer a {
  color: var(--primary-color);
  text-decoration: none;
  font-weight: 500;
}

.form-footer a:hover {
  text-decoration: underline;
}

.error-message {
  margin-top: 16px;
  padding: 12px;
  background-color: rgba(244, 67, 54, 0.1);
  color: var(--error-color);
  border-radius: 6px;
  font-size: 14px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-form {
    padding: 24px;
  }
  
  .form-title {
    font-size: 1.5rem;
  }
  
  .puzzle-icon {
    font-size: 1.8rem;
  }
}
</style>