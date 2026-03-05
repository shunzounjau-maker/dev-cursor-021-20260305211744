import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    userRole: localStorage.getItem('userRole') || null,
    username: localStorage.getItem('username') || null
  }),
  actions: {
    async login(username, password) {
      try {
        const response = await axios.post('/api/auth/login', { username, password })
        const token = response.data.accessToken
        this.token = token
        localStorage.setItem('token', token)
        
        // Fetch user details
        const meResponse = await axios.get('/api/auth/me', {
          headers: { Authorization: `Bearer ${token}` }
        })
        
        const user = meResponse.data
        // user.authorities is an array of { authority: "ROLE_ADMIN" }
        const role = user.authorities[0].authority.replace('ROLE_', '')
        
        this.userRole = role
        localStorage.setItem('userRole', role)
        
        this.username = user.username
        localStorage.setItem('username', user.username)
        
        return true
      } catch (error) {
        console.error('Login failed', error)
        return false
      }
    },
    logout() {
      this.token = null
      this.userRole = null
      this.username = null
      localStorage.removeItem('token')
      localStorage.removeItem('userRole')
      localStorage.removeItem('username')
    }
  }
})
