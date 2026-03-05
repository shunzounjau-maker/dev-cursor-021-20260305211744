<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import MyClasses from '../../components/teacher/MyClasses.vue'

const router = useRouter()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="teacher-dashboard">
    <div class="header">
      <h2>Teacher Dashboard</h2>
      <el-button @click="handleLogout">Logout</el-button>
    </div>
    
    <MyClasses />
  </div>
</template>

<style scoped>
.teacher-dashboard {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
