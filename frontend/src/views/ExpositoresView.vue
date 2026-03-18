<script setup>
import { ref, onMounted } from 'vue'

const expositores = ref([])
const isLoading = ref(true)

const fetchExpositores = async () => {
  try {
    const response = await fetch(`${import.meta.env.VITE_APP_API_URL || 'http://localhost:8081/api'}/expositores`)
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
</script>

<template>
  <div class="expositores">
    <h2>Nuestros Expositores</h2>
    
    <div v-if="isLoading" class="loading">Cargando expositores...</div>
    
    <div v-else-if="expositores.length === 0" class="empty-state">
      No hay expositores registrados aún.
    </div>

    <div v-for="expositor in expositores" :key="expositor.id" class="card expositor-card">
      <div class="profile-main">
        <div class="profile-info">
          <h3>{{ expositor.nombre }}</h3>
          <p class="bio">{{ expositor.bio }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.loading, .empty-state {
  text-align: center;
  padding: 3rem;
  color: var(--text-light);
}

.profile-main {
  margin-bottom: 0.5rem;
}

.profile-info h3 {
  margin-bottom: 0.25rem;
}

.bio {
  font-size: 0.9rem;
  color: var(--text-light);
}

</style>
