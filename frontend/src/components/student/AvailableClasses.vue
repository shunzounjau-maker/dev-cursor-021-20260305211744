<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const classes = ref([])

const fetchClasses = async () => {
  try {
    const response = await axios.get('/api/student/classes', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    classes.value = response.data
  } catch (error) {
    ElMessage.error('Failed to fetch classes')
  }
}

const enroll = async (cls) => {
  try {
    await axios.post(`/api/student/enroll/${cls.id}`, {}, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    ElMessage.success('Enrolled successfully')
    fetchClasses()
  } catch (error) {
    ElMessage.error(error.response?.data || 'Failed to enroll')
  }
}

onMounted(fetchClasses)
</script>

<template>
  <div>
    <el-table :data="classes" style="width: 100%">
      <el-table-column prop="course.name" label="Course" />
      <el-table-column prop="teacher.user.name" label="Teacher" />
      <el-table-column prop="semester" label="Semester" />
      <el-table-column prop="schedule" label="Schedule" />
      <el-table-column prop="capacity" label="Capacity" />
      <el-table-column label="Actions">
        <template #default="scope">
          <el-button type="primary" @click="enroll(scope.row)">Enroll</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
