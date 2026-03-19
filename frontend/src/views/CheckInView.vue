<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import { Html5QrcodeScanner } from 'html5-qrcode'
import { CheckCircle, UserCircle, ScanLine } from 'lucide-vue-next'

const scanResult = ref(null)
const scanner = ref(null)
const user = ref({ nombre: '', email: '', banner: '' })
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

function onScanFailure() {}

const resetScanner = () => {
  scanResult.value = null
}
</script>

<template>
  <div class="check-in">
    <h2>Check-In</h2>

    <div v-if="!isRegistered" class="register-card">
      <div class="form-header">
        <div class="form-icon">
          <UserCircle :size="40" />
        </div>
        <h3>Identifícate</h3>
        <p>Ingresa tus datos para registrar tu asistencia.</p>
      </div>

      <div class="form-fields">
        <div class="input-group">
          <label>Nombre Completo</label>
          <input v-model="user.nombre" type="text" placeholder="Juan Pérez" />
        </div>

        <div class="input-group">
          <label>Correo Electrónico</label>
          <input v-model="user.email" type="email" placeholder="juan@ejemplo.com" />
        </div>

        <div class="input-group">
          <label>Banner ID (6 dígitos)</label>
          <input v-model="user.banner" type="text" maxlength="6" placeholder="001234" />
        </div>

        <button
          @click="saveUser"
          class="btn btn-full"
          :disabled="!user.nombre || !user.email || user.banner?.length !== 6"
        >
          <ScanLine :size="18" />
          Comenzar a Escanear
        </button>
      </div>
    </div>

    <template v-else>
      <p class="greeting" v-if="!scanResult">
        Hola <strong>{{ user.nombre }}</strong>, escanea el código QR de la charla.
      </p>

      <div v-show="!scanResult" class="scanner-card">
        <div id="reader"></div>
      </div>

      <div v-if="scanResult" class="success-card">
        <div class="success-icon">
          <CheckCircle :size="56" color="#16a34a" />
        </div>
        <h3>Registro Exitoso</h3>
        <p class="success-message">{{ scanResult }}</p>
        <button @click="resetScanner" class="btn btn-full">Escanear otra charla</button>
      </div>
    </template>
  </div>
</template>

<style scoped>
.register-card {
  background: var(--white);
  border-radius: var(--radius-lg);
  padding: 2rem 1.5rem;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--gray-200);
}

.form-header {
  text-align: center;
  margin-bottom: 1.5rem;
}

.form-icon {
  color: var(--primary-red);
  margin-bottom: 0.5rem;
}

.form-header h3 {
  font-size: 1.2rem;
  margin-bottom: 0.25rem;
}

.form-header p {
  color: var(--text-light);
  font-size: 0.9rem;
}

.form-fields {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.input-group {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.input-group label {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--text-dark);
}

.input-group input {
  padding: 0.7rem 0.85rem;
  border: 1.5px solid var(--gray-300);
  border-radius: var(--radius-sm);
  font-size: 1rem;
  transition: border-color 0.2s;
  outline: none;
}

.input-group input:focus {
  border-color: var(--primary-red);
}

.btn-full {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.greeting {
  text-align: center;
  color: var(--text-light);
  margin-bottom: 1rem;
  font-size: 0.95rem;
}

.scanner-card {
  border-radius: var(--radius-md);
  overflow: hidden;
  box-shadow: var(--shadow-md);
}

#reader {
  width: 100%;
}

.success-card {
  background: var(--white);
  border-radius: var(--radius-lg);
  padding: 2.5rem 1.5rem;
  text-align: center;
  box-shadow: var(--shadow-md);
  border: 1px solid #bbf7d0;
}

.success-icon {
  margin-bottom: 1rem;
}

.success-card h3 {
  color: #16a34a;
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
}

.success-message {
  color: var(--text-light);
  margin-bottom: 1.5rem;
}
</style>
