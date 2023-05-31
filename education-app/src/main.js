import { createApp } from 'vue';
import App from './App.vue';
import router from './router/index.js';

const app = createApp(App);

app.config.globalProperties.userData = null; // Инициализация userData в корневом экземпляре Vue

app.use(router);

app.mount('#app');