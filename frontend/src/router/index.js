import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import AdminDashboard from '../views/admin/Dashboard.vue'
import TeacherDashboard from '../views/teacher/Dashboard.vue'
import StudentDashboard from '../views/student/Dashboard.vue'
import { useAuthStore } from '../stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminDashboard,
      meta: { requiresAuth: true, role: 'ADMIN' }
    },
    {
      path: '/teacher',
      name: 'teacher',
      component: TeacherDashboard,
      meta: { requiresAuth: true, role: 'TEACHER' }
    },
    {
      path: '/student',
      name: 'student',
      component: StudentDashboard,
      meta: { requiresAuth: true, role: 'STUDENT' }
    }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  if (to.meta.requiresAuth) {
    if (!authStore.token) {
      next('/login')
    } else if (to.meta.role && authStore.userRole !== to.meta.role) {
      next('/login') // Or 403 page
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router
