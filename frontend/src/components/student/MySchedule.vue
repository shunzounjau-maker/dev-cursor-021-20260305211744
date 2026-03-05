<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const enrollments = ref([])

const fetchSchedule = async () => {
  try {
    const response = await axios.get('/api/student/schedule', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    enrollments.value = response.data
  } catch (error) {
    ElMessage.error('Failed to fetch schedule')
  }
}

const drop = async (enrollment) => {
  try {
    await axios.delete(`/api/student/drop/${enrollment.courseClass.id}`, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    ElMessage.success('Dropped successfully')
    fetchSchedule()
  } catch (error) {
    ElMessage.error(error.response?.data || 'Failed to drop')
  }
}

onMounted(fetchSchedule)
</script>

<template>
  <div>
    <el-table :data="enrollments" style="width: 100%">
      <el-table-column prop="courseClass.course.name" label="Course" />
      <el-table-column prop="courseClass.teacher.user.name" label="Teacher" />
      <el-table-column prop="courseClass.semester" label="Semester" />
      <el-table-column prop="courseClass.schedule" label="Schedule" />
      <el-table-column label="Actions">
        <template #default="scope">
          <el-button type="danger" @click="drop(scope.row)">Drop</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
