<script setup>
import { onMounted, ref } from 'vue'
import { Bookmark, Clock, User, Award } from 'lucide-vue-next'

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
  <div class="reservas">
    <h2>Mi Perfil y Asistencias</h2>
    
    <div v-if="isLoading" class="loading">Cargando...</div>
    
    <div v-else-if="!user" class="card empty-state">
      <User :size="48" />
      <p>Debes identificarte en la sección de <strong>Check-In</strong> para ver tu historial.</p>
    </div>

    <template v-else-if="perfil">
      <div class="card profile-summary">
        <div class="summary-item">
          <Award :size="32" color="#d32f2f" />
          <div class="summary-text">
            <span class="count">{{ perfil.totalCharlas }}</span>
            <span class="label">Charlas asistidas</span>
          </div>
        </div>
      </div>

      <h3>Charlas registradas</h3>
      <div v-if="perfil.charlas.length === 0" class="empty-state-text">
        Aún no has registrado asistencia a ninguna charla.
      </div>
      
      <div v-for="(titulo, index) in perfil.charlas" :key="index" class="card">
        <div class="reserva-item">
          <Bookmark :size="24" fill="currentColor" class="icon-active" />
          <div class="reserva-details">
            <h3>{{ titulo }}</h3>
            <div class="info-row">
              <Clock :size="14" />
              <span>Asistencia confirmada</span>
            </div>
          </div>
        </div>
      </div>
    </template>

    <div v-else class="card empty-state">
      <p>No se encontraron datos para <strong>{{ user.email }}</strong>. <br>¡Empieza a escanear QRs!</p>
    </div>
  </div>
</template>

<style scoped>
.loading {
  text-align: center;
  padding: 2rem;
}

.empty-state {
  text-align: center;
  padding: 3rem;
  color: var(--text-light);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

.profile-summary {
  background: var(--primary-red);
  color: white;
  border: none;
}

.summary-item {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.summary-item :deep(svg) {
  color: white !important;
}

.summary-text {
  display: flex;
  flex-direction: column;
}

.count {
  font-size: 2rem;
  font-weight: bold;
  line-height: 1;
}

.label {
  font-size: 0.9rem;
  opacity: 0.9;
}

.empty-state-text {
  text-align: center;
  padding: 2rem;
  color: var(--text-light);
}

.reserva-item {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.icon-active {
  color: var(--primary-red);
}

.reserva-details h3 {
  margin-bottom: 0.2rem;
  font-size: 1.1rem;
}

.info-row {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.85rem;
  color: var(--text-light);
}
</style>
