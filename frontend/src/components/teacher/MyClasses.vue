<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const classes = ref([])
const selectedClass = ref(null)
const students = ref([])
const dialogVisible = ref(false)

const fetchClasses = async () => {
  try {
    const response = await axios.get('/api/teacher/classes', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    classes.value = response.data
  } catch (error) {
    ElMessage.error('Failed to fetch classes')
  }
}

const viewStudents = async (cls) => {
  selectedClass.value = cls
  try {
    const response = await axios.get(`/api/teacher/classes/${cls.id}/students`, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    
    // Fetch grades for these students
    // Wait, the API returns Enrollments.
    // I need to fetch grades for each enrollment or if the enrollment includes grade.
    // My Enrollment entity doesn't have Grade directly linked in JSON (OneToOne mapped by Grade).
    // I should probably fetch grades separately or update Enrollment entity to include Grade.
    // Or TeacherController.getClassStudents could return a DTO with grade.
    
    // For now, I'll assume I can fetch grades or update the backend.
    // Let's update the backend to return DTO with grade.
    
    students.value = response.data
    dialogVisible.value = true
  } catch (error) {
    ElMessage.error('Failed to fetch students')
  }
}

const updateGrade = async (student, score) => {
  try {
    await axios.post(`/api/teacher/classes/${selectedClass.value.id}/grades`, {
      studentId: student.student.id,
      score: score
    }, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    ElMessage.success('Grade updated')
  } catch (error) {
    ElMessage.error('Failed to update grade')
  }
}

onMounted(fetchClasses)
</script>

<template>
  <div>
    <el-table :data="classes" style="width: 100%">
      <el-table-column prop="course.name" label="Course" />
      <el-table-column prop="semester" label="Semester" />
      <el-table-column prop="schedule" label="Schedule" />
      <el-table-column label="Actions">
        <template #default="scope">
          <el-button @click="viewStudents(scope.row)">View Students / Grade</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" title="Students & Grades" width="80%">
      <el-table :data="students" style="width: 100%">
        <el-table-column prop="student.studentNo" label="Student No" />
        <el-table-column prop="student.user.name" label="Name" />
        <el-table-column label="Grade">
          <template #default="scope">
            <!-- I need to show current grade. But I don't have it in Enrollment. -->
            <!-- I will add an input for entering grade. -->
            <el-input-number v-model="scope.row.score" @change="(val) => updateGrade(scope.row, val)" />
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>
