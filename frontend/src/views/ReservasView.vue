<script setup>
import { ref } from 'vue'
import { Bookmark, Clock } from 'lucide-vue-next'

const reservas = ref([
  {
    id: 1,
    titulo: 'Arquitecturas Distribuidas',
    fechaHora: '2026-03-10T09:00:00',
    lugar: 'Auditorio A'
  }
])

const formatTime = (dateStr) => {
  return new Date(dateStr).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}
</script>

<template>
  <div class="reservas">
    <h2>Mis Intereses</h2>
    <p v-if="reservas.length === 0" class="empty-state">No tienes charlas marcadas como interesantes aún.</p>
    
    <div v-for="reserva in reservas" :key="reserva.id" class="card">
      <div class="reserva-item">
        <Bookmark :size="24" fill="currentColor" class="icon-active" />
        <div class="reserva-details">
          <h3>{{ reserva.titulo }}</h3>
          <div class="info-row">
            <Clock :size="14" />
            <span>{{ formatTime(reserva.fechaHora) }} - {{ reserva.lugar }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.empty-state {
  text-align: center;
  margin-top: 3rem;
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
