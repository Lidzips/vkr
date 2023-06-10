<template>
  <div id="app">
    <header>
      <div v-if="userData" class="user-info">
        Привет, {{ userData.login }}!
      </div>
      <router-link v-if="isLoginPage" to="/register">Регистрация</router-link>
      <router-link v-else-if="isRegisterPage" to="/login">Войти</router-link>
      <router-link v-else to="/login">Выйти</router-link>
    </header>
    <router-view @userLoggedIn="handleUserLoggedIn"> </router-view>
    
  </div>
</template>

<script>
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
  },
  methods: {
    handleUserLoggedIn(userData) {
      this.userData = userData; // Присваиваем данные пользователя свойству корневого компонента
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
  justify-content: flex-end;
  padding: 10px;
  border-bottom: #6ec9f7fb;
  border-bottom-style: groove;
  border-width: 2px;
}

header a {
  margin-left: 10px;
  text-decoration: none;
  color: #0b7beb;
}

.user-info {
  font-weight: bold;
  margin-right: 2%;
}
</style>
