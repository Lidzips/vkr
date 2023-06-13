<template>
  <div id="app">
    <header>
      <div class="header-left">
        <i class="fas fa-home" @click="goToHome"></i>
      </div>
      <div id="greetings" v-if="userData" class="header-center">
        Привет, {{ userData.login }}!
      </div>
      <div class="header-right">
        <p v-if="isLoginPage" class="auth-link" @click="goTo">Регистрация</p>
        <p v-else-if="isRegisterPage" class="auth-link" @click="goTo">Войти</p>
        <p v-else-if="isHomePage" class="auth-link" @click="goTo">{{ userData ? 'Выйти' : 'Войти' }}</p>
        <p v-else class="auth-link" @click="goTo">Выйти</p>
      </div>
      
    </header>
    <router-view @userLoggedIn="handleUserLoggedIn" @clearUserData="handleClearUserData"> </router-view>
    
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'App',
  data() {
    return {
      userData: null, // Переменная для хранения данных пользователя
    };
  },
  computed: {
    isLoginPage() {
      return this.$route.name === 'Login';
    },
    isRegisterPage() {
      return this.$route.name === 'Register';
    },
    isHomePage() {
      return this.$route.name === 'Home';
    },
  },

  created() {
    
    this.$router.beforeEach((to, from, next) => {
      const sessionData = localStorage.getItem('session');
      if (sessionData && to.name !== 'Login' && to.name !== 'Register') {
        const { accessToken } = JSON.parse(sessionData);

        // Восстановление сессии
        this.restoreSession(accessToken);
      }
      // Вызов метода logout() при каждом переходе на маршрут '/login'
      if (to.name === 'Login') {
        this.logout();
      }

      next();
    });
  },

  beforeMount() {
    
  },
  
  methods: {
    async restoreSession(token) {
      try {
        console.log(token);
        // Отправка запроса на восстановление сессии
        const response = await axios.post('http://localhost:8081/user/restore-session', null, {
          headers: { Authorization: `Bearer ${token}` },
        });

        // Проверка статуса ответа
        if (response.status === 200) {
          // Восстановление сессии
          const { id, login, isAdmin, points } = response.data;
          const userData = {
            id,
            login,
            isAdmin,
            points
          };
          this.handleUserLoggedIn(userData);
        }
      } catch (error) {
        // Обработка ошибки при восстановлении сессии
        console.error('Ошибка восстановления сессии:', error);
      }
    },

    handleUserLoggedIn(userData) {
      this.userData = userData; // Присваиваем данные пользователя свойству корневого компонента
    },

    handleClearUserData() {
      this.userData = null;
      const element = document.getElementById('greetings');
      if (element) {
        element.textContent = '';
      }
    },

    logout() {
      if (this.userData) {
        axios.post('http://localhost:8081/user/logout', { login: this.userData.login, accessToken: this.userData.accessToken })
          .then(response => {
            console.log(response.data);
            // Успешный выход пользователя
            // Очистить данные о пользователе и токене в клиентском приложении
            this.userData = null;
            localStorage.removeItem('session');
          })
          .catch(error => {
            // Обработка ошибки при выходе пользователя
            console.error(error);
          });
      }
    },

    goToHome() {
      this.$router.push('/home');
    },

    goTo() {
      switch (this.$route.name) {
        case 'Login':
          this.$router.push('/register');
          break;
        case 'Register':
          this.$router.push('/login');
          break;
        case 'Home':
          this.$router.push('/login');
          break;
        default:
          this.$router.push('/login');
      }
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0;
}

header {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  padding: 2px;
  border-bottom: #b4b8b9fb;
  border-bottom-style: groove;
  border-width: 2px;
}

.header-left {
  font-size: 24px;
  flex: 1;
  display: flex;
  justify-content: flex-start;
}

.header-center {
  font-weight: bold;
  margin-right: 2%;
  flex: 1;
  display: flex;
  justify-content: center;
}

.header-right {
  flex: 1;
  display: flex;
  justify-content: flex-end;
}

.fa-home{
  cursor: pointer;
}

.auth-link {
  margin-left: 10px;
  text-decoration: none;
  color: #5e9bd8;
  cursor: pointer;
}
</style>