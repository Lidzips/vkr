<template>
    <div class="tasks-page">
      <div class="tasks">
        <h2>Страница заданий</h2>
      </div>
      <div class="spacer"></div>
      <div class="tasks">
        <div v-for="topic in topics" :key="topic">
          <h3 class="topic-heading">{{ topic }}</h3>
          <div class="task-grid">
            <div
              class="task-item"
              v-for="task in getTasksByTopic(topic)"
              :key="task.id"
              :class="{ 'locked': !isTaskAvailable(task.id)}"
              @click="handleTaskClick(task)"
            >
              <span v-if="isTaskCompleted(task.id)" class="completed-icon">✓</span>
              <span v-else-if="!isTaskAvailable(task.id)" class="lock-icon">🔒</span>
              <p>Задание {{ task.id }}</p>
            </div>
          </div>
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
        userProgress: [], // Массив для хранения прогресса
        currentTopic: '', // Текущий выбранный топик
      };
    },
    mounted() {
      this.fetchTasks(); // Вызываем метод для загрузки задач при монтировании компонента
      this.fetchUserProgress();
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
      fetchUserProgress() {
        // Выполняем HTTP-запрос для получения прогресса пользователя
        const userId = this.userData.id; // Получаем ID текущего пользователя
        axios
          .get(`http://localhost:8081/progress/by_user?user_id=${userId}`)
          .then(response => {
            this.userProgress = response.data;
          })
          .catch(error => {
            console.error(error);
          });
      },
      isTaskAvailable(taskId) {
        // Проверяем доступность задачи в прогрессе пользователя
        const progress = this.userProgress.find(item => item.taskId === taskId);
        return progress && progress.available;
      },
      isTaskCompleted(taskId) {
        // Проверяем, выполнено ли задание пользователем
        const progress = this.userProgress.find((item) => item.taskId === taskId);
        return progress && progress.completed;
      },
      handleTaskClick(task) {
        // Обработка клика по задаче
        if (this.isTaskAvailable(task.id)) {
          // Выполнение действий при доступной задаче
          this.$router.push({ name: 'TaskDetails', params: { id: task.id } });
        } else {
          // Действия при недоступной задаче
        }
      },
      getTasksByTopic(topic) {
        // Фильтруем задачи по текущему топику
        return this.tasks.filter((task) => task.topic === topic);
      },
    },  
  };
  </script>
  
  <style>
  .tasks-page {
    margin-top: 2%;
  }

  .tasks {
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

  .locked {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .lock-icon,
  .completed-icon {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }

  .completed-icon {
    color: green;
    font-weight: bolder;
    font-size: 24px;
    text-shadow: 2px 2px 4px #00000062;
  }

  .topic-heading {
    text-align: center;
    margin-top: 20px;
  }
  </style>