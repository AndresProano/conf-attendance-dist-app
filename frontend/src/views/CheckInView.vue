<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import { Html5QrcodeScanner } from 'html5-qrcode'
import { CheckCircle, UserCircle } from 'lucide-vue-next'

const scanResult = ref(null)
const scanner = ref(null)
const user = ref({ nombre: '', email: '' })
const isRegistered = ref(false)
const isLoading = ref(false)

onMounted(() => {
  const savedUser = localStorage.getItem('conf_user')
  if (savedUser) {
    user.value = JSON.parse(savedUser)
    isRegistered.value = true
    initScanner()
  }
})

const saveUser = () => {
  if (user.value.nombre && user.value.email) {
    localStorage.setItem('conf_user', JSON.stringify(user.value))
    isRegistered.value = true
    initScanner()
  }
}

const initScanner = () => {
  setTimeout(() => {
    scanner.value = new Html5QrcodeScanner(
      "reader",
      { fps: 10, qrbox: { width: 250, height: 250 } },
      false
    )
    scanner.value.render(onScanSuccess, onScanFailure)
  }, 100)
}

onUnmounted(() => {
  if (scanner.value) {
    scanner.value.clear()
  }
})

async function onScanSuccess(decodedText) {
  if (isLoading.value) return
  
  isLoading.value = true
  try {
    const response = await fetch(`${import.meta.env.VITE_APP_API_URL || '/api'}/check-in`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        charlaId: decodedText,
        email: user.value.email,
        nombre: user.value.nombre,
        banner: user.value.banner || ''
      })
    })
    
    const result = await response.text()
    if (response.ok) {
      scanResult.value = result
    } else {
      alert(result)
    }
  } catch (e) {
    alert("Error conectando con el servidor")
  } finally {
    isLoading.value = false
  }
}

function onScanFailure(error) {}

const resetScanner = () => {
  scanResult.value = null
}
</script>

<template>
  <div class="check-in">
    <h2>Check-in de Asistencia</h2>
    
    <div v-if="!isRegistered" class="card user-form">
      <div class="form-header">
        <UserCircle :size="48" color="#d32f2f" />
        <h3>Identifícate</h3>
        <p>Necesitamos saber quién eres para registrar tu asistencia.</p>
      </div>
      
      <div class="input-group">
        <label>Nombre Completo</label>
        <input v-model="user.nombre" type="text" placeholder="Ej: Juan Pérez" />
      </div>
      
      <div class="input-group">
        <label>Correo Electrónico</label>
        <input v-model="user.email" type="email" placeholder="juan@ejemplo.com" />
      </div>

      <div class="input-group">
        <label>Banner ID (6 dígitos)</label>
        <input v-model="user.banner" type="text" maxlength="6" placeholder="001234" />
      </div>
      
      <button @click="saveUser" class="btn" :disabled="!user.nombre || !user.email || user.banner?.length !== 6">
        Comenzar a Escanear
      </button>
    </div>

    <template v-else>
      <p class="instruction" v-if="!scanResult">Hola <strong>{{ user.nombre }}</strong>, escanea el código QR de la charla.</p>

      <div v-show="!scanResult" class="card scanner-container">
        <div id="reader"></div>
      </div>

      <div v-if="scanResult" class="card success-card">
        <CheckCircle :size="64" color="#4caf50" />
        <h3>¡Registro Exitoso!</h3>
        <p>{{ scanResult }}</p>
        <button @click="resetScanner" class="btn">Escanear otra charla</button>
      </div>
    </template>
  </div>
</template>

<style scoped>
.user-form {
  padding: 2rem;
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-header {
  text-align: center;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.input-group input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

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
