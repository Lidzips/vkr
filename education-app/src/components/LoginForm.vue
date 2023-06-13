  <template>
    <div class="login">
      <h2>Вход</h2>
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
          <router-link to="/register">Зарегистрироваться</router-link>
        </div>
        <button type="submit">Войти</button>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      </form>
    </div>
  </template>
  
  <script>
  import axios from 'axios';

  export default {
    props: ['userData'],
    data() {
      return {
        loginData: {
          username: '',
          password: '',
        },
        errorMessage: '',
      };
    },
    created() {
      this.$emit('clearUserData'); // Сброс данных пользователя при переходе на страницу логина
    },
    methods: {
      login() {
        // Создаем объект с данными аутентификации
        const authData = {
          login: this.loginData.username,
          password: this.loginData.password
        };

        // Отправляем запрос на сервер
        axios.post('http://localhost:8081/user/login', authData)
          .then(response => {
            // Обработка успешного ответа от сервера
            const accessToken = response.data.accessToken;
            const { id, login, isAdmin, points } = response.data;
            const userData = {
              id,
              login,
              isAdmin,
              points
            };
            const sessionData = {accessToken};
            
            // Сохраняем токен доступа в localStorage
            localStorage.setItem('session', JSON.stringify(sessionData));

            // Отправляем событие с данными пользователя
            this.$emit('userLoggedIn', userData);

            // Переход на соответствующую страницу
            if (isAdmin) {
              this.$router.push('/admin');
            } else {
              this.$router.push('/tasks');
            }
          })
          .catch(error => {
            // Обработка ошибки при аутентификации
            this.errorMessage = 'Ошибка при выполнении запроса к серверу';
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

  .login {
    max-width: 300px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 4px;
    margin-top: 2%;
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