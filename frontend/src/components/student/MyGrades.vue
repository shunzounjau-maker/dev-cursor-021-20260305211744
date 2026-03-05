<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const grades = ref([])

const fetchGrades = async () => {
  try {
    const response = await axios.get('/api/student/grades', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    grades.value = response.data
  } catch (error) {
    ElMessage.error('Failed to fetch grades')
  }
}

onMounted(fetchGrades)
</script>

<template>
  <div>
    <el-table :data="grades" style="width: 100%">
      <el-table-column prop="enrollment.courseClass.course.name" label="Course" />
      <el-table-column prop="enrollment.courseClass.semester" label="Semester" />
      <el-table-column prop="score" label="Score" />
    </el-table>
  </div>
</template>
