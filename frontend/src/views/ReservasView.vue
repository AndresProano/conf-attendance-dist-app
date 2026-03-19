<script setup>
import { onMounted, ref } from 'vue'
import { Bookmark, CheckCircle, User, Award } from 'lucide-vue-next'

const perfil = ref(null)
const isLoading = ref(true)
const user = ref(null)

onMounted(async () => {
  const savedUser = localStorage.getItem('conf_user')
  if (savedUser) {
    user.value = JSON.parse(savedUser)
    await fetchPerfil()
  } else {
    isLoading.value = false
  }
})

const fetchPerfil = async () => {
  try {
    const response = await fetch(`${import.meta.env.VITE_APP_API_URL || '/api'}/usuarios/${user.value.email}/perfil`)
    if (response.ok) {
      perfil.value = await response.json()
    }
  } catch (e) {
    console.error("Error cargando perfil", e)
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="perfil">
    <h2>Mi Perfil</h2>

    <div v-if="isLoading" class="loading">
      <div class="spinner"></div>
      <span>Cargando...</span>
    </div>

    <div v-else-if="!user" class="empty-card">
      <User :size="40" />
      <p>Identifícate en <strong>Check-In</strong> para ver tu historial de asistencia.</p>
    </div>

    <template v-else-if="perfil">
      <div class="stats-card">
        <Award :size="28" />
        <div class="stats-text">
          <span class="stats-count">{{ perfil.totalCharlas }}</span>
          <span class="stats-label">charlas asistidas</span>
        </div>
      </div>

      <h3 class="section-title">Charlas registradas</h3>

      <div v-if="perfil.charlas.length === 0" class="empty-text">
        Aún no has registrado asistencia a ninguna charla.
      </div>

      <div v-for="(titulo, index) in perfil.charlas" :key="index" class="attendance-card">
        <div class="attendance-icon">
          <CheckCircle :size="20" color="#16a34a" />
        </div>
        <div class="attendance-info">
          <span class="attendance-title">{{ titulo }}</span>
          <span class="attendance-status">Asistencia confirmada</span>
        </div>
      </div>
    </template>

    <div v-else class="empty-card">
      <p>No se encontraron datos para <strong>{{ user.email }}</strong>.</p>
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

.empty-card {
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-md);
  padding: 3rem 1.5rem;
  text-align: center;
  color: var(--text-light);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
}

.stats-card {
  background: linear-gradient(135deg, #c62828 0%, #e53935 100%);
  color: white;
  border-radius: var(--radius-md);
  padding: 1.25rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.stats-text {
  display: flex;
  flex-direction: column;
}

.stats-count {
  font-size: 2rem;
  font-weight: 800;
  line-height: 1;
}

.stats-label {
  font-size: 0.85rem;
  opacity: 0.9;
}

.section-title {
  font-size: 1rem;
  color: var(--text-dark);
  margin-bottom: 0.75rem;
}

.empty-text {
  text-align: center;
  padding: 2rem;
  color: var(--text-light);
  font-size: 0.9rem;
}

.attendance-card {
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-radius: var(--radius-sm);
  padding: 0.85rem 1rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 0.5rem;
}

.attendance-info {
  display: flex;
  flex-direction: column;
}

.attendance-title {
  font-weight: 600;
  font-size: 0.9rem;
  color: var(--text-dark);
}

.attendance-status {
  font-size: 0.75rem;
  color: #16a34a;
  font-weight: 500;
}
</style>
