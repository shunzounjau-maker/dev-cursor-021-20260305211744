<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const teachers = ref([])
const dialogVisible = ref(false)
const form = ref({
  username: '',
  password: '',
  name: '',
  email: '',
  staffNo: '',
  department: ''
})

const fetchTeachers = async () => {
  try {
    const response = await axios.get('/api/admin/teachers', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    teachers.value = response.data
  } catch (error) {
    ElMessage.error('Failed to fetch teachers')
  }
}

const createTeacher = async () => {
  try {
    await axios.post('/api/admin/teachers', form.value, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    ElMessage.success('Teacher created')
    dialogVisible.value = false
    fetchTeachers()
  } catch (error) {
    ElMessage.error('Failed to create teacher')
  }
}

onMounted(fetchTeachers)
</script>

<template>
  <div>
    <el-button type="primary" @click="dialogVisible = true">Add Teacher</el-button>
    <el-table :data="teachers" style="width: 100%">
      <el-table-column prop="staffNo" label="Staff No" />
      <el-table-column prop="user.name" label="Name" />
      <el-table-column prop="department" label="Department" />
      <el-table-column prop="user.email" label="Email" />
    </el-table>

    <el-dialog v-model="dialogVisible" title="Add Teacher">
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
        <el-form-item label="Staff No">
          <el-input v-model="form.staffNo" />
        </el-form-item>
        <el-form-item label="Department">
          <el-input v-model="form.department" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="createTeacher">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
