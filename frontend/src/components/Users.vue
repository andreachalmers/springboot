<template>
  <div class="users-container">
    <h2>Users Management (MongoDB)</h2>
    
    <div class="form-section">
      <h3>{{ editingUser ? 'Edit User' : 'Add New User' }}</h3>
      <form @submit.prevent="saveUser" class="user-form">
        <input v-model="userForm.username" placeholder="Username" required />
        <input v-model="userForm.email" type="email" placeholder="Email" required />
        <input v-model="userForm.firstName" placeholder="First Name" required />
        <input v-model="userForm.lastName" placeholder="Last Name" required />
        <button type="submit">{{ editingUser ? 'Update' : 'Create' }} User</button>
        <button v-if="editingUser" type="button" @click="cancelEdit">Cancel</button>
      </form>
    </div>

    <div class="users-list">
      <h3>Users List</h3>
      <div v-if="loading" class="loading">Loading...</div>
      <div v-else-if="users.length === 0" class="empty">No users found</div>
      <table v-else class="users-table">
        <thead>
          <tr>
            <th>Username</th>
            <th>Email</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.username }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.firstName }}</td>
            <td>{{ user.lastName }}</td>
            <td>
              <button @click="editUser(user)" class="btn-edit">Edit</button>
              <button @click="deleteUser(user.id)" class="btn-delete">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

const API_URL = 'http://localhost:8080/api/users'

export default {
  name: 'Users',
  data() {
    return {
      users: [],
      loading: false,
      editingUser: null,
      userForm: {
        username: '',
        email: '',
        firstName: '',
        lastName: ''
      }
    }
  },
  mounted() {
    this.fetchUsers()
  },
  methods: {
    async fetchUsers() {
      this.loading = true
      try {
        const response = await axios.get(API_URL)
        this.users = response.data
      } catch (error) {
        console.error('Error fetching users:', error)
        alert('Failed to fetch users')
      } finally {
        this.loading = false
      }
    },
    async saveUser() {
      try {
        if (this.editingUser) {
          await axios.put(`${API_URL}/${this.editingUser.id}`, this.userForm)
        } else {
          await axios.post(API_URL, this.userForm)
        }
        this.resetForm()
        this.fetchUsers()
      } catch (error) {
        console.error('Error saving user:', error)
        alert('Failed to save user')
      }
    },
    editUser(user) {
      this.editingUser = user
      this.userForm = {
        username: user.username,
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName
      }
    },
    cancelEdit() {
      this.editingUser = null
      this.resetForm()
    },
    async deleteUser(id) {
      if (!confirm('Are you sure you want to delete this user?')) return
      try {
        await axios.delete(`${API_URL}/${id}`)
        this.fetchUsers()
      } catch (error) {
        console.error('Error deleting user:', error)
        alert('Failed to delete user')
      }
    },
    resetForm() {
      this.userForm = {
        username: '',
        email: '',
        firstName: '',
        lastName: ''
      }
      this.editingUser = null
    }
  }
}
</script>

<style scoped>
.users-container {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

h2 {
  margin-bottom: 2rem;
  color: #333;
}

.form-section {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: #f9f9f9;
  border-radius: 6px;
}

.user-form {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
  margin-top: 1rem;
}

.user-form input {
  flex: 1;
  min-width: 150px;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.user-form button {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
}

.user-form button[type="submit"] {
  background: #667eea;
  color: white;
}

.user-form button[type="button"] {
  background: #6c757d;
  color: white;
}

.users-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.users-table th,
.users-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.users-table th {
  background: #f8f9fa;
  font-weight: 600;
}

.btn-edit,
.btn-delete {
  padding: 0.5rem 1rem;
  margin-right: 0.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.btn-edit {
  background: #28a745;
  color: white;
}

.btn-delete {
  background: #dc3545;
  color: white;
}

.loading, .empty {
  text-align: center;
  padding: 2rem;
  color: #666;
}
</style>

