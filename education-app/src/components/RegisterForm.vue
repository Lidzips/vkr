<template>
  <div class="register">
    <h2>Регистрация</h2>
    <form @submit.prevent="login">
      <div>
        <label for="login">Логин:</label>
        <input type="text" id="login" v-model="loginData.username" />
      </div>
      <div>
        <label for="password">Пароль:</label>
        <input type="password" id="password" v-model="loginData.password" />
      </div>
      <div>
        <label for="password">Повторите пароль:</label>
        <input type="password" id="password" v-model="loginData.password" />
      </div>
      <button type="submit">Зарегистрироваться</button>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      loginData: {
        username: '',
        password: '',
      },
      errorMessage: '',
    };
  },
  methods: {
    login() {
      axios.get(`http://localhost:8081/user/id?login=${this.loginData.username}`)
        .then(response => {
          // Обработка ответа от сервера
          const userData = response.data;
          if (userData.password === this.loginData.password) {
            this.$emit('userLoggedIn', userData); // Отправляем событие с данными пользователя
            this.$router.push('/home'); // Переход на домашнюю страницу
          } else {
            this.errorMessage = 'Неправильный логин или пароль'; // Установка сообщения об ошибке
          }
        })
        .catch(error => {
          this.errorMessage = 'Ошибка при выполнении запроса к серверу'; // Установка сообщения об ошибке
          console.error(error);
        });
    },
  },
};
</script>

<style scoped>
.error-message {
  color: red;
}

.register {
  max-width: 500px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

h2 {
  text-align: center;
}

form {
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 5px;
}

input {
  margin-bottom: 10px;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  padding: 10px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>