<script setup>
import { RouterView, RouterLink } from 'vue-router'
import { Calendar, Users, QrCode, Bookmark } from 'lucide-vue-next'
</script>

<template>
  <div class="app-container">
    <header class="header">
      <div class="header-content">
        <div class="header-brand">
          <span class="header-logo">ASME</span>
          <div class="header-text">
            <h1>EFx Conference</h1>
            <p class="header-subtitle">USFQ - Marzo 19-20, 2026</p>
          </div>
        </div>
      </div>
    </header>

    <main class="container">
      <RouterView />
    </main>

    <nav class="bottom-nav">
      <RouterLink to="/" class="nav-item">
        <Calendar :size="22" />
        <span>Agenda</span>
      </RouterLink>
      <RouterLink to="/expositores" class="nav-item">
        <Users :size="22" />
        <span>Expositores</span>
      </RouterLink>
      <RouterLink to="/checkin" class="nav-item">
        <QrCode :size="22" />
        <span>Check-In</span>
      </RouterLink>
      <RouterLink to="/reservas" class="nav-item">
        <Bookmark :size="22" />
        <span>Mi Perfil</span>
      </RouterLink>
    </nav>
  </div>
</template>

<style scoped>
.header {
  background: linear-gradient(135deg, #c62828 0%, #8e0000 100%);
  padding: 1rem 1.25rem;
  position: sticky;
  top: 0;
  z-index: 100;
  color: white;
}

.header-content {
  max-width: 900px;
  margin: 0 auto;
}

.header-brand {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.header-logo {
  background: white;
  color: #c62828;
  font-weight: 800;
  font-size: 0.85rem;
  padding: 0.3rem 0.5rem;
  border-radius: 6px;
  letter-spacing: 1px;
}

.header h1 {
  color: white;
  margin: 0;
  font-size: 1.2rem;
  font-weight: 700;
  line-height: 1.2;
}

.header-subtitle {
  color: rgba(255,255,255,0.8);
  font-size: 0.8rem;
  margin: 0;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>
