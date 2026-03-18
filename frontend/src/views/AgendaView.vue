<script setup>
import { ref, onMounted } from 'vue'
import { MapPin, Clock } from 'lucide-vue-next'

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

const formatDate = (fechaStr) => {
  if (!fechaStr) return ''
  const date = new Date(`${fechaStr}T00:00:00`)
  return date.toLocaleDateString('es-EC', { day: 'numeric', month: 'long' })
}

const formatHour = (timeStr) => {
  if (!timeStr) return ''
  const [rawHour, rawMinute = '00'] = timeStr.split(':')
  const hour = Number(rawHour)
  const minute = Number(rawMinute)
  if (Number.isNaN(hour) || Number.isNaN(minute)) return timeStr
  return minute === 0 ? `${hour}` : `${hour}:${String(minute).padStart(2, '0')}`
}

const formatRange = (charla) => {
  if (!charla?.horaDesde || !charla?.horaHasta) return ''
  return `${formatHour(charla.horaDesde)} a ${formatHour(charla.horaHasta)}`
}

const getStartDateTime = (charla) => {
  if (charla?.fecha && charla?.horaDesde) {
    return new Date(`${charla.fecha}T${charla.horaDesde}`)
  }
  return null
}

const isEventStarted = (charla) => {
  const startDateTime = getStartDateTime(charla)
  if (!startDateTime) return false
  return new Date() >= startDateTime
}

const speakerNames = (charla) => {
  if (Array.isArray(charla?.expositores) && charla.expositores.length > 0) {
    return charla.expositores.map((exp) => exp.nombre).join(', ')
  }
  if (charla?.expositor?.nombre) {
    return charla.expositor.nombre
  }
  return ''
}


</script>

<template>
  <div class="agenda">
    <h2>Agenda de Charlas</h2>
    
    <div v-if="isLoading" class="loading">Cargando agenda...</div>
    
    <div v-else-if="charlas.length === 0" class="empty-state">
      No hay charlas programadas aún.
    </div>

    <div
      v-for="charla in charlas"
      :key="charla.id"
      class="card charla-card"
      :class="{ 'event-started': isEventStarted(charla) }"
    >
      <div class="charla-header">
        <h3>{{ charla.titulo }}</h3>
      </div>
      
      <div class="charla-footer">
        <div class="info">
          <div class="info-item">
            <Clock :size="16" />
            <span>{{ formatDate(charla.fecha) }} - {{ formatRange(charla) }}</span>
          </div>
          <div class="info-item">
            <MapPin :size="16" />
            <span>{{ charla.lugar }}</span>
          </div>
          <div v-if="speakerNames(charla)" class="info-item speaker-name">
            <strong>Expositor(es):</strong> {{ speakerNames(charla) }}
          </div>
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

.charla-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}


.charla-footer {
  display: block;
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


.event-started {
  opacity: 0.55;
}
</style>
