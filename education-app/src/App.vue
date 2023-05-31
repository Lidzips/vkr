<template>
  <div id="app">
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
  margin-top: 20px;
}
</style>
