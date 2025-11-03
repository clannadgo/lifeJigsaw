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
            <label for="verificationCode">验证码</label>
            <div class="verification-code-group">
              <input
                type="text"
                id="verificationCode"
                v-model="formData.verificationCode"
                placeholder="请输入6位验证码"
                maxlength="6"
                pattern="[0-9]{6}"
                required
              />
              <button
                type="button"
                class="btn btn-secondary"
                :class="{ 'countdown-active': countDown > 0 }"
                :disabled="isSendingCode || countDown > 0"
                @click="sendVerificationCode"
              >
                {{ countDown > 0 ? `${countDown}秒后重试` : '获取验证码' }}
              </button>
            </div>
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
import { addUser, sendEmailCode } from '../api/user.js'

export default {
  name: 'Register',
  data() {
    return {
      formData: {
        username: '',
        email: '',
        verificationCode: '',
        password: '',
        familyName: ''
      },
      confirmPassword: '',
      isLoading: false,
      isSendingCode: false,
      countDown: 0,
      errorMessage: '',
      // 存储定时器ID，用于组件销毁时清除
      countDownTimer: null
    }
  },
  
  beforeDestroy() {
    // 组件销毁前清除定时器，避免内存泄漏
    if (this.countDownTimer) {
      clearInterval(this.countDownTimer)
    }
  },
  methods: {
    async sendVerificationCode() {
      // 邮箱格式验证
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(this.formData.email)) {
        this.errorMessage = '请输入有效的邮箱地址'
        return
      }
      
      this.errorMessage = ''
      this.isSendingCode = true
      
      try {
        await sendEmailCode(this.formData.email)
        // 由于响应拦截器已经统一处理了错误情况，这里只需要处理成功的情况
        this.$message.success('验证码已发送，请查收邮箱')
        this.startCountDown()
      } catch (error) {
        console.error('发送验证码失败:', error)
        this.errorMessage = error.response?.data?.message || '发送验证码失败'
      } finally {
        this.isSendingCode = false
      }
    },
    
    startCountDown() {
      // 清除可能存在的旧定时器
      if (this.countDownTimer) {
        clearInterval(this.countDownTimer)
      }
      
      this.countDown = 60
      this.countDownTimer = setInterval(() => {
        this.countDown--
        if (this.countDown <= 0) {
          clearInterval(this.countDownTimer)
          this.countDownTimer = null
        }
      }, 1000)
    },
    
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
      
      // 验证码格式验证
      if (!/^\d{6}$/.test(this.formData.verificationCode)) {
        this.errorMessage = '请输入6位数字验证码'
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
          // 由于响应拦截器已经统一处理了错误情况，这里只需要处理成功的情况
          const response = await addUser(this.formData)
          // 检查响应中是否包含token和用户信息
          if (response.data && response.data.token) {
            this.$message.success(response.message || '注册成功，正在跳转到控制台！')
            // 有token时直接跳转到控制台
            this.$router.push('/dashboard')
          } else {
            // 没有token时，提示用户注册成功并跳转到控制台
            this.$message.success(response.message || '注册成功，正在跳转到控制台！')
            setTimeout(() => {
              this.$router.push('/dashboard')
            }, 1500)
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
}

.verification-code-group {
  display: flex;
  gap: 12px;
}

.verification-code-group input {
  flex: 1;
}

.btn-secondary {
  padding: 12px 20px;
  font-size: 14px;
  font-weight: 500;
  background-color: var(--secondary-color);
  color: var(--white);
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  white-space: nowrap;
}

.btn-secondary:hover:not(:disabled) {
  background-color: #E55A5A;
}

.btn-secondary:disabled {
  background-color: #BDC3C7;
  cursor: not-allowed;
  opacity: 0.7;
}

/* 倒计时状态的按钮样式，使其更明显 */
.btn-secondary:disabled.countdown-active {
  background-color: #95A5A6;
  font-size: 13px;
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