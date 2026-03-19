<script setup>
import { ref, computed, onMounted } from 'vue'
import { MapPin, Clock, User, Coffee, UtensilsCrossed } from 'lucide-vue-next'

const charlas = ref([])
const isLoading = ref(true)
const activeDay = ref(null)

const fetchCharlas = async () => {
  try {
    const response = await fetch(`${import.meta.env.VITE_APP_API_URL || '/api'}/charlas`)
    if (response.ok) {
      charlas.value = await response.json()
      const dates = [...new Set(charlas.value.map(c => c.fecha))].sort()
      if (dates.length > 0) activeDay.value = dates[0]
    }
  } catch (e) {
    console.error("Error fetching charlas", e)
  } finally {
    isLoading.value = false
  }
}

onMounted(fetchCharlas)

const days = computed(() => {
  const dates = [...new Set(charlas.value.map(c => c.fecha))].sort()
  return dates.map((fecha, i) => ({ fecha, label: `Dia ${i + 1}`, dateLabel: formatDateLong(fecha) }))
})

const timeSlots = computed(() => {
  if (!activeDay.value) return []
  const dayCharlas = charlas.value.filter(c => c.fecha === activeDay.value)
  const slotMap = {}
  dayCharlas.forEach(c => {
    const key = c.horaDesde
    if (!slotMap[key]) slotMap[key] = { horaDesde: c.horaDesde, charlas: [] }
    slotMap[key].charlas.push(c)
  })
  return Object.values(slotMap).sort((a, b) => a.horaDesde.localeCompare(b.horaDesde))
})

const formatDateLong = (fechaStr) => {
  if (!fechaStr) return ''
  const date = new Date(`${fechaStr}T00:00:00`)
  return date.toLocaleDateString('es-EC', { weekday: 'long', day: 'numeric', month: 'long' })
}

const formatHour = (timeStr) => {
  if (!timeStr) return ''
  const [h, m] = timeStr.split(':').map(Number)
  const suffix = h >= 12 ? 'PM' : 'AM'
  const hour12 = h > 12 ? h - 12 : h === 0 ? 12 : h
  return m === 0 ? `${hour12} ${suffix}` : `${hour12}:${String(m).padStart(2, '0')} ${suffix}`
}

const speakerNames = (charla) => {
  if (Array.isArray(charla?.expositores) && charla.expositores.length > 0) {
    return charla.expositores.map(e => e.nombre).join(', ')
  }
  if (charla?.expositor?.nombre) return charla.expositor.nombre
  return ''
}

const isBreak = (charla) => {
  const t = charla.titulo.toLowerCase()
  return t.includes('intermedio') || t.includes('refrigerio') || t.includes('receso') || t.includes('almuerzo') || t.includes('coffee') || t.includes('networking') || t.includes('red de contactos')
}

const isEventStarted = (charla) => {
  if (charla?.fecha && charla?.horaDesde) {
    return new Date() >= new Date(`${charla.fecha}T${charla.horaDesde}`)
  }
  return false
}

const getCardClass = (charla) => {
  if (isBreak(charla)) return 'charla-break'
  const t = charla.titulo?.toLowerCase() || ''
  const lugar = charla.lugar?.toLowerCase() || ''
  if (t.includes('iam3d') || t.includes('elevator pitch') || t.includes('oral competition') || lugar.includes('epicuro') || lugar.includes('plaza')) return 'charla-competition'
  return 'charla-talk'
}
</script>

<template>
  <div class="agenda">
    <div class="page-header">
      <h2>Agenda</h2>
    </div>

    <div v-if="isLoading" class="loading">
      <div class="spinner"></div>
      <span>Cargando agenda...</span>
    </div>

    <template v-else-if="charlas.length > 0">
      <div class="day-tabs">
        <button
          v-for="day in days"
          :key="day.fecha"
          class="day-tab"
          :class="{ active: activeDay === day.fecha }"
          @click="activeDay = day.fecha"
        >
          <span class="day-tab-label">{{ day.label }}</span>
          <span class="day-tab-date">{{ day.dateLabel }}</span>
        </button>
      </div>

      <div class="legend">
        <span class="legend-item"><span class="legend-dot talk"></span> Charlas</span>
        <span class="legend-item"><span class="legend-dot competition"></span> Competencias</span>
        <span class="legend-item"><span class="legend-dot break-dot"></span> Recesos</span>
      </div>

      <div class="timeline">
        <div v-for="slot in timeSlots" :key="`${slot.horaDesde}-${slot.horaHasta}`" class="time-slot">
          <div class="time-label">
            <span class="time-from">{{ formatHour(slot.horaDesde) }}</span>
          </div>
          <div class="slot-cards" :class="{ 'multi': slot.charlas.length > 1 }">
            <div
              v-for="charla in slot.charlas"
              :key="charla.id"
              class="charla-card"
              :class="[getCardClass(charla), { 'event-started': isEventStarted(charla) }]"
            >
              <template v-if="isBreak(charla)">
                <div class="break-content">
                  <Coffee v-if="charla.titulo.toLowerCase().includes('refrigerio') || charla.titulo.toLowerCase().includes('coffee') || charla.titulo.toLowerCase().includes('intermedio')" :size="18" />
                  <UtensilsCrossed v-else-if="charla.titulo.toLowerCase().includes('almuerzo')" :size="18" />
                  <User v-else :size="18" />
                  <span>{{ charla.titulo }}</span>
                  <span class="break-time">{{ formatHour(charla.horaDesde) }} - {{ formatHour(charla.horaHasta) }}</span>
                </div>
              </template>
              <template v-else>
                <h3 class="charla-title">{{ charla.titulo }}</h3>
                <div class="charla-meta">
                  <div class="meta-item">
                    <Clock :size="14" />
                    <span>{{ formatHour(charla.horaDesde) }} - {{ formatHour(charla.horaHasta) }}</span>
                  </div>
                  <div class="meta-item">
                    <MapPin :size="14" />
                    <span>{{ charla.lugar }}</span>
                  </div>
                  <div v-if="speakerNames(charla)" class="meta-item speaker">
                    <User :size="14" />
                    <span>{{ speakerNames(charla) }}</span>
                  </div>
                </div>
              </template>
            </div>
          </div>
        </div>
      </div>
    </template>

    <div v-else class="empty-state">No hay charlas programadas.</div>
  </div>
</template>

<style scoped>
.page-header {
  margin-bottom: 0.5rem;
}

.page-header h2 {
  font-size: 1.5rem;
  font-weight: 700;
  margin: 0;
}

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

/* Day tabs */
.day-tabs {
  display: flex;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.day-tab {
  flex: 1;
  padding: 0.75rem 0.5rem;
  border: 2px solid var(--gray-200);
  border-radius: var(--radius-md);
  background: var(--white);
  cursor: pointer;
  text-align: center;
  transition: all 0.2s;
}

.day-tab.active {
  border-color: var(--primary-red);
  background: #fef2f2;
}

.day-tab-label {
  display: block;
  font-weight: 700;
  font-size: 0.95rem;
  color: var(--text-dark);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.day-tab.active .day-tab-label {
  color: var(--primary-red);
}

.day-tab-date {
  display: block;
  font-size: 0.75rem;
  color: var(--text-light);
  margin-top: 0.15rem;
  text-transform: capitalize;
}

/* Legend */
.legend {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  padding: 0.5rem 0;
  font-size: 0.75rem;
  color: var(--text-light);
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 0.35rem;
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 3px;
}

.legend-dot.talk { background: var(--primary-red); }
.legend-dot.competition { background: #1565c0; }
.legend-dot.break-dot { background: var(--gray-300); }

/* Timeline */
.timeline {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.time-slot {
  display: flex;
  gap: 0.75rem;
  padding: 0.5rem 0;
  border-top: 1px solid var(--gray-200);
}

.time-slot:first-child {
  border-top: none;
}

.time-label {
  width: 70px;
  min-width: 70px;
  display: flex;
  flex-direction: column;
  padding-top: 0.75rem;
}

.time-from {
  font-size: 0.8rem;
  font-weight: 700;
  color: var(--text-dark);
  line-height: 1.2;
}

.time-to {
  font-size: 0.7rem;
  color: var(--text-light);
}

.slot-cards {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.slot-cards.multi {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 0.5rem;
}

/* Charla cards */
.charla-card {
  border-radius: var(--radius-sm);
  padding: 0.85rem;
  transition: box-shadow 0.2s;
}

.charla-card:hover {
  box-shadow: var(--shadow-md);
}

.charla-talk {
  background: var(--white);
  border: 1px solid var(--gray-200);
  border-left: 4px solid var(--primary-red);
}

.charla-competition {
  background: #e3f2fd;
  border: 1px solid #bbdefb;
  border-left: 4px solid #1565c0;
}

.charla-break {
  background: var(--gray-100);
  border: 1px solid var(--gray-200);
  padding: 0.6rem 0.85rem;
}

.event-started {
  opacity: 0.5;
}

.charla-title {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--text-dark);
  margin-bottom: 0.5rem;
  line-height: 1.3;
}

.charla-meta {
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  font-size: 0.78rem;
  color: var(--text-light);
}

.meta-item.speaker {
  color: var(--primary-red);
  font-weight: 500;
}

.break-content {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--text-light);
  font-size: 0.85rem;
  font-weight: 500;
}

.break-time {
  margin-left: auto;
  font-size: 0.75rem;
  color: var(--gray-600);
}

@media (max-width: 500px) {
  .slot-cards.multi {
    grid-template-columns: 1fr;
  }

  .time-label {
    width: 58px;
    min-width: 58px;
  }

  .time-from {
    font-size: 0.72rem;
  }

  .charla-title {
    font-size: 0.85rem;
  }
}
</style>
