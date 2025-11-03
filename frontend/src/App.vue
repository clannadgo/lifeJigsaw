<template>
  <div class="app-container">
    <!-- 路由视图，显示当前路由对应的组件 -->
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App'
}
</script>

<style>
/* 全局样式已在theme.css中定义 */
.app-container {
  min-height: 100vh;
}
</style>
