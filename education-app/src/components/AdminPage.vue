<template>
    <div class="home-page">
      <div class="home">
        <h2>Администраторская страница</h2>
      </div>
      <div class="spacer">
      </div>
      <div class="home">
        <div v-for="topic in topics" :key="topic">
          <h3 class="topic-heading">{{ topic }}</h3>
          <div class="task-grid">
            <div
              class="task-item"
              v-for="task in getTasksByTopic(topic)"
              :key="task.id"
              :class="{}"
              @click="handleTaskClick(task)"
            >
              <p>Задание {{ task.id }}</p>
            </div>
          </div>
          
        </div>
        <div class="add-task">
          <router-link to="/adminCreate">
            <button class="add-task-button">
              Добавить задачу
            </button>
          </router-link>
        </div>
      </div>
    </div>
    
  </template>
  
  <script>
  import axios from 'axios';

  export default {
    computed: {
      userData() {
        return this.$root.userData; // Получение данных пользователя из корневого экземпляра Vue
      },
      topics() {
        // Получаем список уникальных тем из задач
        const uniqueTopics = new Set(this.tasks.map((task) => task.topic));
        return Array.from(uniqueTopics);
      },
    },
    data() {
      return {
        tasks: [], // Массив для хранения задач
        currentTopic: '', // Текущий выбранный топик
      };
    },
    mounted() {
      this.fetchTasks(); // Вызываем метод для загрузки задач при монтировании компонента
    },
    methods: {
      fetchTasks() {
        // Выполняем HTTP-запрос для получения задач
        axios.get(`http://localhost:8081/task`)
          .then(response => {
            // Обработка ответа от сервера
            this.tasks = response.data;

            // Заполнение массива уникальными темами
            const uniqueTopics = [...new Set(this.tasks.map(task => task.topic))];
            this.topics = uniqueTopics;
          })
          .catch(error => {
            console.error(error);
          });
      },

      handleTaskClick(task) {
        this.$router.push({ name: 'AdminDetails', params: { id: task.id } });
      },

      getTasksByTopic(topic) {
        // Фильтруем задачи по текущему топику
        return this.tasks.filter((task) => task.topic === topic);
      },
    },  
  };
  </script>
  
  <style>
  .home {
    max-width: 75%;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  .spacer {
    height: 30px;
  }

  h2 {
    text-align: center;
  }

  .task-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    gap: 10px;
    grid-auto-flow: row dense;
  }

  .task-item {
    padding: 10px;
    border: 1px solid #ccc;
    cursor: pointer;
    position: relative;
  }

  .topic-heading {
    text-align: center;
    margin-top: 20px;
  }

  .add-task {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .add-task-button {
    padding: 10px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    font-size: 16px;
  }
  </style>