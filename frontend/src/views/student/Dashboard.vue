<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import AvailableClasses from '../../components/student/AvailableClasses.vue'
import MySchedule from '../../components/student/MySchedule.vue'
import MyGrades from '../../components/student/MyGrades.vue'

const router = useRouter()
const authStore = useAuthStore()
const activeTab = ref('schedule')

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="student-dashboard">
    <div class="header">
      <h2>Student Dashboard</h2>
      <el-button @click="handleLogout">Logout</el-button>
    </div>
    
    <el-tabs v-model="activeTab">
      <el-tab-pane label="My Schedule" name="schedule">
        <MySchedule />
      </el-tab-pane>
      <el-tab-pane label="Available Classes" name="classes">
        <AvailableClasses />
      </el-tab-pane>
      <el-tab-pane label="My Grades" name="grades">
        <MyGrades />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped>
.student-dashboard {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
</style>
