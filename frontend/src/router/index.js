import { createRouter, createWebHistory } from 'vue-router'
import AgendaView from '../views/AgendaView.vue'
import ExpositoresView from '../views/ExpositoresView.vue'
import CheckInView from '../views/CheckInView.vue'
import ReservasView from '../views/ReservasView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'agenda',
      component: AgendaView
    },
    {
      path: '/expositores',
      name: 'expositores',
      component: ExpositoresView
    },
    {
      path: '/checkin',
      name: 'checkin',
      component: CheckInView
    },
    {
      path: '/reservas',
      name: 'reservas',
      component: ReservasView
    }
  ]
})

export default router
