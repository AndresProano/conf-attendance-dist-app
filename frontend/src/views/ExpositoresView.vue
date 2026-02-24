<script setup>
import { ref } from 'vue'
import { Twitter, Linkedin, Github } from 'lucide-vue-next'

const expositores = ref([
  {
    id: 1,
    nombre: 'Dra. Elena Ruiz',
    bio: 'Experta en sistemas distribuidos y computación en la nube con más de 15 años de experiencia.',
    foto: 'https://i.pravatar.cc/150?u=elena',
    redes: { twitter: '#', linkedin: '#', github: '#' }
  },
  {
    id: 2,
    nombre: 'Marco Polo',
    bio: 'Ingeniero de Software Senior especializado en frameworks frontend modernos y UX.',
    foto: 'https://i.pravatar.cc/150?u=marco',
    redes: { twitter: '#', linkedin: '#', github: '#' }
  }
])
</script>

<template>
  <div class="expositores">
    <h2>Nuestros Expositores</h2>
    
    <div v-for="expositor in expositores" :key="expositor.id" class="card expositor-card">
      <div class="profile-main">
        <img :src="expositor.foto" :alt="expositor.nombre" class="avatar" />
        <div class="profile-info">
          <h3>{{ expositor.nombre }}</h3>
          <p class="bio">{{ expositor.bio }}</p>
        </div>
      </div>
      
      <div class="social-links">
        <a :href="expositor.redes.twitter" target="_blank" class="social-icon">
          <Twitter :size="20" />
        </a>
        <a :href="expositor.redes.linkedin" target="_blank" class="social-icon">
          <Linkedin :size="20" />
        </a>
        <a :href="expositor.redes.github" target="_blank" class="social-icon">
          <Github :size="20" />
        </a>
      </div>
    </div>
  </div>
</template>

<style scoped>
.profile-main {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  border: 3px solid var(--light-red);
  object-fit: cover;
}

.profile-info h3 {
  margin-bottom: 0.25rem;
}

.bio {
  font-size: 0.9rem;
  color: var(--text-light);
}

.social-links {
  display: flex;
  gap: 1rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.social-icon {
  color: var(--text-light);
  transition: color 0.2s;
}

.social-icon:hover {
  color: var(--primary-red);
}

@media (max-width: 480px) {
  .profile-main {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
}
</style>
