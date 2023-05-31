<template>
  <div class="logout">
    <router-link to="/login">Войти</router-link>
  </div>
  <div class="register">
    <h2>Регистрация</h2>
    <form @submit.prevent="register">
      <div>
        <label for="login">Логин:</label>
        <input type="text" id="login" v-model="registerData.username" />
      </div>
      <div>
        <label for="password1">Пароль:</label>
        <input type="password" id="password1" v-model="registerData.password1" />
      </div>
      <div>
        <label for="password2">Повторите пароль:</label>
        <input type="password" id="password2" v-model="registerData.password2" />
      </div>
      <button type="submit">Зарегистрироваться</button>
      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      registerData: {
        username: '',
        password1: '',
        password2: '',
      },
      errorMessage: '',
      successMessage: '',
    };
  },
  methods: {
    register() {
      axios.get(`http://localhost:8081/user/id?login=${this.registerData.username}`)
        .then(response => {
          // Обработка ответа от сервера
          const userData = response.data;
          if (userData !== null) {
            this.errorMessage = 'Учетная запись с именем ' + userData.login + ' уже существует.' // Установка сообщения об ошибке
          }
        })
        .catch(error => {
          if (error.message == 'Request failed with status code 404') {
            if (this.registerData.password1 == this.registerData.password2) {
              axios.post(`http://localhost:8081/user`,
                {
                  login: this.registerData.username,
                  password: this.registerData.password1
                }
                )
                .then(response => {
                  // Обработка ответа от сервера
                  const userData = response.data;
                  this.successMessage = 'Учетная запись с именем ' + userData.login + ' успешно создана.'; // Установка сообщения об успехе
                })
                .catch(error => {
                  this.errorMessage = 'Ошибка при выполнении запроса к серверу'; // Установка сообщения об ошибке
                  console.error(error);
                });
            }
          } else {
            this.errorMessage = 'Ошибка при выполнении запроса к серверу'; // Установка сообщения об ошибке
            console.error(error);
          }
          
        });
      
    },
  },
};
</script>

<style scoped>
.error-message {
  color: red;
}

.success-message {
  color: green;
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