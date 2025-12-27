import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import Users from './components/Users.vue'
import Products from './components/Products.vue'

const routes = [
  { path: '/', redirect: '/users' },
  { path: '/users', component: Users },
  { path: '/products', component: Products }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

const app = createApp(App)
app.use(router)
app.mount('#app')

