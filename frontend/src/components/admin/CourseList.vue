<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const courses = ref([])
const dialogVisible = ref(false)
const form = ref({
  code: '',
  name: '',
  credits: 0,
  description: ''
})

const fetchCourses = async () => {
  try {
    const response = await axios.get('/api/admin/courses', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    courses.value = response.data
  } catch (error) {
    ElMessage.error('Failed to fetch courses')
  }
}

const createCourse = async () => {
  try {
    await axios.post('/api/admin/courses', form.value, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    ElMessage.success('Course created')
    dialogVisible.value = false
    fetchCourses()
  } catch (error) {
    ElMessage.error('Failed to create course')
  }
}

onMounted(fetchCourses)
</script>

<template>
  <div>
    <el-button type="primary" @click="dialogVisible = true">Add Course</el-button>
    <el-table :data="courses" style="width: 100%">
      <el-table-column prop="code" label="Code" />
      <el-table-column prop="name" label="Name" />
      <el-table-column prop="credits" label="Credits" />
      <el-table-column prop="description" label="Description" />
    </el-table>

    <el-dialog v-model="dialogVisible" title="Add Course">
      <el-form :model="form" label-width="100px">
        <el-form-item label="Code">
          <el-input v-model="form.code" />
        </el-form-item>
        <el-form-item label="Name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="Credits">
          <el-input-number v-model="form.credits" />
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="createCourse">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
