// 简单的消息提示工具

const message = {
  install(app) {
    // 消息提示方法
    const showMessage = (text, type = 'info', duration = 3000) => {
      // 创建消息元素
      const messageEl = document.createElement('div')
      messageEl.className = `message-toast message-${type}`
      messageEl.textContent = text
      
      // 添加样式
      messageEl.style.cssText = `
        position: fixed;
        top: 20px;
        left: 50%;
        transform: translateX(-50%);
        padding: 12px 24px;
        border-radius: 6px;
        font-size: 14px;
        color: white;
        z-index: 9999;
        transition: opacity 0.3s, transform 0.3s;
        opacity: 0;
        transform: translate(-50%, -20px);
      `
      
      // 根据类型设置背景色
      const colors = {
        success: '#4CAF50',
        error: '#F44336',
        warning: '#FF9800',
        info: '#2196F3'
      }
      
      messageEl.style.backgroundColor = colors[type] || colors.info
      
      // 添加到页面
      document.body.appendChild(messageEl)
      
      // 显示消息
      setTimeout(() => {
        messageEl.style.opacity = '1'
        messageEl.style.transform = 'translate(-50%, 0)'
      }, 10)
      
      // 自动关闭
      setTimeout(() => {
        messageEl.style.opacity = '0'
        messageEl.style.transform = 'translate(-50%, -20px)'
        
        // 移除元素
        setTimeout(() => {
          if (messageEl.parentNode) {
            document.body.removeChild(messageEl)
          }
        }, 300)
      }, duration)
    }
    
    // 定义各种类型的消息方法
    const messageMethods = {
      success: (text, duration) => showMessage(text, 'success', duration),
      error: (text, duration) => showMessage(text, 'error', duration),
      warning: (text, duration) => showMessage(text, 'warning', duration),
      info: (text, duration) => showMessage(text, 'info', duration)
    }
    
    // 添加到全局属性
    app.config.globalProperties.$message = messageMethods
    
    // 也可以通过 provide/inject 提供
    app.provide('message', messageMethods)
  }
}

export default message