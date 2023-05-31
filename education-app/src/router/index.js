import { createRouter, createWebHistory } from 'vue-router';
import LoginForm from '../components/LoginForm.vue';
import RegisterForm from '../components/RegisterForm.vue';
import HomePage from '../components/HomePage.vue';
import TaskDetails from '../components/TaskDetails.vue';
import AdminPage from '../components/AdminPage.vue';
import AdminDetails from '../components/AdminDetails.vue';
import AdminCreate from '../components/AdminCreate.vue';

const routes = [
  {
    path: '/',
    redirect: '/login',
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginForm,
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterForm,
  },
  {
    path: '/home',
    name: 'Home',
    component: HomePage,
  },
  {
    path: '/admin',
    name: 'Admin',
    component: AdminPage,
  },
  {
    path: '/adminCreate',
    name: 'AdminCreate',
    component: AdminCreate,
  },
  {
    path: '/task/:id',
    name: 'TaskDetails',
    component: TaskDetails,
    props: true, // Включение передачи параметров в виде пропсов
  },
  {
    path: '/taskAdmin/:id',
    name: 'AdminDetails',
    component: AdminDetails,
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;