<script setup>
import { ref, onMounted } from 'vue'
import { User } from 'lucide-vue-next'

const expositores = ref([])
const isLoading = ref(true)

const fetchExpositores = async () => {
  try {
    const response = await fetch(`${import.meta.env.VITE_APP_API_URL || '/api'}/expositores`)
    if (response.ok) {
      expositores.value = await response.json()
    }
  } catch (e) {
    console.error("Error fetching expositores", e)
  } finally {
    isLoading.value = false
  }
}

onMounted(fetchExpositores)

const getInitials = (nombre) => {
  return nombre.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase()
}
</script>

<template>
  <div class="expositores">
    <h2>Expositores</h2>

    <div v-if="isLoading" class="loading">
      <div class="spinner"></div>
      <span>Cargando expositores...</span>
    </div>

    <div v-else-if="expositores.length === 0" class="empty-state">
      No hay expositores registrados.
    </div>

    <div class="expositor-grid">
      <div v-for="expositor in expositores" :key="expositor.id" class="expositor-card">
        <div class="avatar">
          <span>{{ getInitials(expositor.nombre) }}</span>
        </div>
        <div class="expositor-info">
          <h3>{{ expositor.nombre }}</h3>
          <p class="bio">{{ expositor.bio }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 4rem 0;
  color: var(--text-light);
}

.spinner {
  width: 32px;
  height: 32px;
  border: 3px solid var(--gray-200);
  border-top-color: var(--primary-red);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 4rem 0;
  color: var(--text-light);
}

.expositor-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 0.75rem;
}

.expositor-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  padding: 1rem;
  transition: box-shadow 0.2s;
}

.expositor-card:hover {
  box-shadow: var(--shadow-md);
}

.avatar {
  width: 48px;
  height: 48px;
  min-width: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, #c62828, #e53935);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 0.85rem;
}

.expositor-info h3 {
  font-size: 0.95rem;
  margin-bottom: 0.15rem;
  color: var(--text-dark);
}

.bio {
  font-size: 0.8rem;
  color: var(--text-light);
  line-height: 1.4;
}

@media (max-width: 640px) {
  .expositor-grid {
    grid-template-columns: 1fr;
  }
}
</style>
