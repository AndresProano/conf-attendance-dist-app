<script setup>
import { ref, onMounted } from 'vue'
import { Twitter, Linkedin, Github } from 'lucide-vue-next'

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
        <img :src="expositor.fotoUrl || 'https://i.pravatar.cc/150?u=default'" :alt="expositor.nombre" class="avatar" />
        <div class="profile-info">
          <h3>{{ expositor.nombre }}</h3>
          <p class="bio">{{ expositor.bio }}</p>
        </div>
      </div>
      
      <div class="social-links">
        <a v-if="expositor.twitter" :href="expositor.twitter" target="_blank" class="social-icon">
          <Twitter :size="20" />
        </a>
        <a v-if="expositor.linkedin" :href="expositor.linkedin" target="_blank" class="social-icon">
          <Linkedin :size="20" />
        </a>
        <a v-if="expositor.github" :href="expositor.github" target="_blank" class="social-icon">
          <Github :size="20" />
        </a>
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
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid var(--light-red);
  object-fit: cover;
}

.profile-info h3 {
  margin-bottom: 0.25rem;
}

.bio {
  font-size: 0.9rem;
  color: var(--text-light);
}

.social-links {
  display: flex;
  gap: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.social-icon {
  color: var(--text-light);
  transition: color 0.2s;
}

.social-icon:hover {
  color: var(--primary-red);
}

@media (max-width: 480px) {
  .profile-main {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}
</style>
