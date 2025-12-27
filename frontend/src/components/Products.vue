<template>
  <div class="products-container">
    <h2>Products Management (JPA)</h2>
    
    <div class="form-section">
      <h3>{{ editingProduct ? 'Edit Product' : 'Add New Product' }}</h3>
      <form @submit.prevent="saveProduct" class="product-form">
        <input v-model.number="productForm.name" placeholder="Product Name" required />
        <textarea v-model="productForm.description" placeholder="Description" rows="3"></textarea>
        <input v-model.number="productForm.price" type="number" step="0.01" placeholder="Price" required />
        <input v-model.number="productForm.stock" type="number" placeholder="Stock" required />
        <button type="submit">{{ editingProduct ? 'Update' : 'Create' }} Product</button>
        <button v-if="editingProduct" type="button" @click="cancelEdit">Cancel</button>
      </form>
    </div>

    <div class="search-section">
      <h3>Search Products</h3>
      <input v-model="searchKeyword" @input="searchProducts" placeholder="Search by name or description" />
    </div>

    <div class="products-list">
      <h3>Products List</h3>
      <div v-if="loading" class="loading">Loading...</div>
      <div v-else-if="products.length === 0" class="empty">No products found</div>
      <table v-else class="products-table">
        <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>{{ product.name }}</td>
            <td>{{ product.description }}</td>
            <td>${{ product.price }}</td>
            <td>{{ product.stock }}</td>
            <td>
              <button @click="editProduct(product)" class="btn-edit">Edit</button>
              <button @click="deleteProduct(product.id)" class="btn-delete">Delete</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import axios from 'axios'

const API_URL = 'http://localhost:8080/api/products'

export default {
  name: 'Products',
  data() {
    return {
      products: [],
      loading: false,
      editingProduct: null,
      searchKeyword: '',
      productForm: {
        name: '',
        description: '',
        price: 0,
        stock: 0
      }
    }
  },
  mounted() {
    this.fetchProducts()
  },
  methods: {
    async fetchProducts() {
      this.loading = true
      try {
        const response = await axios.get(API_URL)
        this.products = response.data
      } catch (error) {
        console.error('Error fetching products:', error)
        alert('Failed to fetch products')
      } finally {
        this.loading = false
      }
    },
    async searchProducts() {
      if (!this.searchKeyword.trim()) {
        this.fetchProducts()
        return
      }
      this.loading = true
      try {
        const response = await axios.get(`${API_URL}/search`, {
          params: { keyword: this.searchKeyword }
        })
        this.products = response.data
      } catch (error) {
        console.error('Error searching products:', error)
      } finally {
        this.loading = false
      }
    },
    async saveProduct() {
      try {
        if (this.editingProduct) {
          await axios.put(`${API_URL}/${this.editingProduct.id}`, this.productForm)
        } else {
          await axios.post(API_URL, this.productForm)
        }
        this.resetForm()
        this.fetchProducts()
      } catch (error) {
        console.error('Error saving product:', error)
        alert('Failed to save product')
      }
    },
    editProduct(product) {
      this.editingProduct = product
      this.productForm = {
        name: product.name,
        description: product.description,
        price: product.price,
        stock: product.stock
      }
    },
    cancelEdit() {
      this.editingProduct = null
      this.resetForm()
    },
    async deleteProduct(id) {
      if (!confirm('Are you sure you want to delete this product?')) return
      try {
        await axios.delete(`${API_URL}/${id}`)
        this.fetchProducts()
      } catch (error) {
        console.error('Error deleting product:', error)
        alert('Failed to delete product')
      }
    },
    resetForm() {
      this.productForm = {
        name: '',
        description: '',
        price: 0,
        stock: 0
      }
      this.editingProduct = null
    }
  }
}
</script>

<style scoped>
.products-container {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

h2 {
  margin-bottom: 2rem;
  color: #333;
}

.form-section,
.search-section {
  margin-bottom: 2rem;
  padding: 1.5rem;
  background: #f9f9f9;
  border-radius: 6px;
}

.product-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-top: 1rem;
}

.product-form input,
.product-form textarea {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.product-form button {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  font-weight: 500;
}

.product-form button[type="submit"] {
  background: #667eea;
  color: white;
}

.product-form button[type="button"] {
  background: #6c757d;
  color: white;
}

.search-section input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.products-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

.products-table th,
.products-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.products-table th {
  background: #f8f9fa;
  font-weight: 600;
}

.products-table td {
  max-width: 300px;
  word-wrap: break-word;
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

