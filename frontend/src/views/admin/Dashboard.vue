<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import TeacherList from '../../components/admin/TeacherList.vue'
import StudentList from '../../components/admin/StudentList.vue'
import CourseList from '../../components/admin/CourseList.vue'
import ClassList from '../../components/admin/ClassList.vue'
import axios from 'axios'
import { onMounted } from 'vue'

const router = useRouter()
const authStore = useAuthStore()
const activeTab = ref('dashboard')
const stats = ref({
  studentCount: 0,
  teacherCount: 0,
  courseCount: 0,
  classCount: 0
})

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

const fetchStats = async () => {
  try {
    const response = await axios.get('/api/admin/dashboard', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    stats.value = response.data
  } catch (error) {
    console.error('Failed to fetch stats')
  }
}

onMounted(fetchStats)
</script>

<template>
  <div class="admin-dashboard">
    <div class="header">
      <h2>Admin Dashboard</h2>
      <el-button @click="handleLogout">Logout</el-button>
    </div>
    
    <el-tabs v-model="activeTab">
      <el-tab-pane label="Dashboard" name="dashboard">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card>
              <template #header>Students</template>
              <div class="stat-value">{{ stats.studentCount }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <template #header>Teachers</template>
              <div class="stat-value">{{ stats.teacherCount }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <template #header>Courses</template>
              <div class="stat-value">{{ stats.courseCount }}</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card>
              <template #header>Classes</template>
              <div class="stat-value">{{ stats.classCount }}</div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <el-tab-pane label="Teachers" name="teachers">
        <TeacherList />
      </el-tab-pane>
      <el-tab-pane label="Students" name="students">
        <StudentList />
      </el-tab-pane>
      <el-tab-pane label="Courses" name="courses">
        <CourseList />
      </el-tab-pane>
      <el-tab-pane label="Classes" name="classes">
        <ClassList />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<style scoped>
.admin-dashboard {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
}
</style>
