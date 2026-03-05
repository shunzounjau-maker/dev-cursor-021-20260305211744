<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const students = ref([])
const dialogVisible = ref(false)
const form = ref({
  username: '',
  password: '',
  name: '',
  email: '',
  studentNo: '',
  grade: '',
  className: ''
})

const fetchStudents = async () => {
  try {
    const response = await axios.get('/api/admin/students', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    students.value = response.data
  } catch (error) {
    ElMessage.error('Failed to fetch students')
  }
}

const createStudent = async () => {
  try {
    await axios.post('/api/admin/students', form.value, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    ElMessage.success('Student created')
    dialogVisible.value = false
    fetchStudents()
  } catch (error) {
    ElMessage.error('Failed to create student')
  }
}

onMounted(fetchStudents)
</script>

<template>
  <div>
    <el-button type="primary" @click="dialogVisible = true">Add Student</el-button>
    <el-table :data="students" style="width: 100%">
      <el-table-column prop="studentNo" label="Student No" />
      <el-table-column prop="user.name" label="Name" />
      <el-table-column prop="grade" label="Grade" />
      <el-table-column prop="className" label="Class" />
      <el-table-column prop="user.email" label="Email" />
    </el-table>

    <el-dialog v-model="dialogVisible" title="Add Student">
      <el-form :model="form" label-width="100px">
        <el-form-item label="Username">
          <el-input v-model="form.username" />
        </el-form-item>
        <el-form-item label="Password">
          <el-input v-model="form.password" type="password" />
        </el-form-item>
        <el-form-item label="Name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-form-item label="Student No">
          <el-input v-model="form.studentNo" />
        </el-form-item>
        <el-form-item label="Grade">
          <el-input v-model="form.grade" />
        </el-form-item>
        <el-form-item label="Class">
          <el-input v-model="form.className" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="createStudent">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
