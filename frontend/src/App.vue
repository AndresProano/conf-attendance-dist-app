<script setup>
import { RouterView, RouterLink } from 'vue-router'
import { Calendar, Users, QrCode, Bookmark } from 'lucide-vue-next'
</script>

<template>
  <div class="app-container">
    <header class="header">
      <h1>Conf Attendance</h1>
    </header>

    <main class="container">
      <RouterView />
    </main>

    <nav class="bottom-nav">
      <RouterLink to="/" class="nav-item">
        <Calendar :size="24" />
        <span>Agenda</span>
      </RouterLink>
      <RouterLink to="/expositores" class="nav-item">
        <Users :size="24" />
        <span>Expositores</span>
      </RouterLink>
      <RouterLink to="/checkin" class="nav-item">
        <QrCode :size="24" />
        <span>Check-In</span>
      </RouterLink>
      <RouterLink to="/reservas" class="nav-item">
        <Bookmark :size="24" />
        <span>Reservas</span>
      </RouterLink>
    </nav>
  </div>
</template>

<style scoped>
.header {
  background-color: var(--white);
  padding: 1rem;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header h1 {
  margin-bottom: 0;
  font-size: 1.5rem;
}

.app-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
</style>
