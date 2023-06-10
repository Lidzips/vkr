import { createApp } from 'vue';
import App from './App.vue';
import router from './router/index.js';
import '@fortawesome/fontawesome-free/css/all.css';

const app = createApp(App);

app.config.globalProperties.userData = null; // Инициализация userData в корневом экземпляре Vue

app.use(router);

app.mount('#app');