<script setup>
import { ref } from 'vue'
import { MapPin, Clock, Bell } from 'lucide-vue-next'

const charlas = ref([
  {
    id: 1,
    titulo: 'Arquitecturas Distribuidas',
    descripcion: 'Una inmersión profunda en sistemas distribuidos modernos y su escalabilidad.',
    lugar: 'Auditorio A',
    fechaHora: '2026-03-10T09:00:00',
    cuposDisponibles: 45
  },
  {
    id: 2,
    titulo: 'Vue.js en el Mundo Real',
    descripcion: 'Patrones avanzados y mejores prácticas para aplicaciones a gran escala.',
    lugar: 'Sala 204',
    fechaHora: '2026-03-10T11:30:00',
    cuposDisponibles: 12
  },
  {
    id: 3,
    titulo: 'Seguridad en Microservicios',
    descripcion: 'Estrategias de autenticación y autorización para ecosistemas complejos.',
    lugar: 'Auditorio B',
    fechaHora: '2026-03-10T14:00:00',
    cuposDisponibles: 0
  }
])

const formatTime = (dateStr) => {
  return new Date(dateStr).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const showInterest = (charla) => {
  alert(`Te has interesado en: ${charla.titulo}. Recibirás notificaciones.`)
}
</script>

<template>
  <div class="agenda">
    <h2>Agenda de Charlas</h2>
    
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
