<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import { Html5QrcodeScanner } from 'html5-qrcode'
import { CheckCircle } from 'lucide-vue-next'

const scanResult = ref(null)
const scanner = ref(null)

onMounted(() => {
  scanner.value = new Html5QrcodeScanner(
    "reader",
    { fps: 10, qrbox: { width: 250, height: 250 } },
    /* verbose= */ false
  )
  
  scanner.value.render(onScanSuccess, onScanFailure)
})

onUnmounted(() => {
  if (scanner.value) {
    scanner.value.clear()
  }
})

function onScanSuccess(decodedText, decodedResult) {
  scanResult.value = decodedText
  // Aquí se enviaría la petición al backend para registrar la asistencia
  alert(`Check-in exitoso para la charla ID: ${decodedText}`)
}

function onScanFailure(error) {
  // console.warn(`Code scan error = ${error}`);
}

const resetScanner = () => {
  scanResult.value = null
}
</script>

<template>
  <div class="check-in">
    <h2>Check-in de Asistencia</h2>
    <p class="instruction">Escanea el código QR ubicado en la entrada de la sala.</p>

    <div v-show="!scanResult" class="card scanner-container">
      <div id="reader"></div>
    </div>

    <div v-if="scanResult" class="card success-card">
      <CheckCircle :size="64" color="#4caf50" />
      <h3>¡Asistencia Registrada!</h3>
      <p>Has registrado tu entrada correctamente.</p>
      <button @click="resetScanner" class="btn">Escanear de nuevo</button>
    </div>
  </div>
</template>

<style scoped>
.instruction {
  margin-bottom: 1.5rem;
  color: var(--text-light);
  text-align: center;
}

.scanner-container {
  padding: 0;
  overflow: hidden;
}

#reader {
  width: 100%;
}

.success-card {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  padding: 2rem;
  border-left-color: #4caf50;
}
</style>
