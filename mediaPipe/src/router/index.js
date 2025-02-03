import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import PushUpView from "../views/PushUpView.vue";
import LungeView from "../views/LungeView.vue";
import PlankView from "../views/PlankView.vue";

const routes = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/pushup",
    name: "pushup",
    component: PushUpView,
  },
  {
    path: "/lunge",
    name: "lunge",
    component: LungeView,
  },
  {
    path: "/plank",
    name: "plank",
    component: PlankView,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
