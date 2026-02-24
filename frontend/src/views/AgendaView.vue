<script setup>
import { ref, onMounted } from 'vue'
import { MapPin, Clock, Bell } from 'lucide-vue-next'

const charlas = ref([])
const isLoading = ref(true)

const fetchCharlas = async () => {
  try {
    const response = await fetch(`${import.meta.env.VITE_APP_API_URL || 'http://localhost:8081/api'}/charlas`)
    if (response.ok) {
      charlas.value = await response.json()
    }
  } catch (e) {
    console.error("Error fetching charlas", e)
  } finally {
    isLoading.value = false
  }
}

onMounted(fetchCharlas)

const formatTime = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const showInterest = (charla) => {
  alert(`Te has interesado en: ${charla.titulo}. Recibirás notificaciones.`)
}
</script>

<template>
  <div class="agenda">
    <h2>Agenda de Charlas</h2>
    
    <div v-if="isLoading" class="loading">Cargando agenda...</div>
    
    <div v-else-if="charlas.length === 0" class="empty-state">
      No hay charlas programadas aún.
    </div>

    <div v-for="charla in charlas" :key="charla.id" class="card charla-card">
      <div class="charla-header">
        <h3>{{ charla.titulo }}</h3>
        <span v-if="charla.cuposDisponibles > 0" class="badge">
          {{ charla.cuposDisponibles }} cupos
        </span>
        <span v-else class="badge-empty">Agotado</span>
      </div>
      
      <p class="description">{{ charla.descripcion }}</p>
      
      <div class="charla-footer">
        <div class="info">
          <div class="info-item">
            <Clock :size="16" />
            <span>{{ formatTime(charla.fechaHora) }}</span>
          </div>
          <div class="info-item">
            <MapPin :size="16" />
            <span>{{ charla.lugar }}</span>
          </div>
          <div v-if="charla.expositor" class="info-item speaker-name">
            <strong>Expositor:</strong> {{ charla.expositor.nombre }}
          </div>
        </div>
        
        <button @click="showInterest(charla)" class="btn btn-outline btn-sm">
          <Bell :size="16" />
          Me interesa
        </button>
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

.charla-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.description {
  font-size: 0.9rem;
  color: var(--text-light);
  margin-bottom: 1rem;
}

.charla-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.85rem;
  color: var(--text-dark);
}

.speaker-name {
  margin-top: 0.25rem;
  font-size: 0.8rem;
  color: var(--primary-red);
}

.info {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.badge-empty {
  background: #eee;
  color: #888;
  padding: 0.2rem 0.5rem;
  border-radius: 12px;
  font-size: 0.7rem;
}

.btn-sm {
  padding: 0.3rem 0.6rem;
  font-size: 0.8rem;
  display: flex;
  align-items: center;
  gap: 0.4rem;
}
</style>
