<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const classes = ref([])
const dialogVisible = ref(false)
const form = ref({
  courseCode: '',
  teacherStaffNo: '',
  semester: '',
  schedule: '',
  capacity: 0
})

const fetchClasses = async () => {
  try {
    const response = await axios.get('/api/admin/classes', {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    classes.value = response.data
  } catch (error) {
    ElMessage.error('Failed to fetch classes')
  }
}

const createClass = async () => {
  try {
    await axios.post('/api/admin/classes', form.value, {
      headers: { Authorization: `Bearer ${authStore.token}` }
    })
    ElMessage.success('Class created')
    dialogVisible.value = false
    fetchClasses()
  } catch (error) {
    ElMessage.error('Failed to create class')
  }
}

onMounted(fetchClasses)
</script>

<template>
  <div>
    <el-button type="primary" @click="dialogVisible = true">Add Class</el-button>
    <el-table :data="classes" style="width: 100%">
      <el-table-column prop="course.name" label="Course" />
      <el-table-column prop="teacher.user.name" label="Teacher" />
      <el-table-column prop="semester" label="Semester" />
      <el-table-column prop="schedule" label="Schedule" />
      <el-table-column prop="capacity" label="Capacity" />
    </el-table>

    <el-dialog v-model="dialogVisible" title="Add Class">
      <el-form :model="form" label-width="120px">
        <el-form-item label="Course Code">
          <el-input v-model="form.courseCode" />
        </el-form-item>
        <el-form-item label="Teacher Staff No">
          <el-input v-model="form.teacherStaffNo" />
        </el-form-item>
        <el-form-item label="Semester">
          <el-input v-model="form.semester" />
        </el-form-item>
        <el-form-item label="Schedule">
          <el-input v-model="form.schedule" />
        </el-form-item>
        <el-form-item label="Capacity">
          <el-input-number v-model="form.capacity" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">Cancel</el-button>
          <el-button type="primary" @click="createClass">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
