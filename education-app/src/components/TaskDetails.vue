<template>
  <div class="logout">
    <router-link to="/login">Выйти</router-link>
  </div>
  <div class="task-details">
    <div class="task-navigation">
      <router-link to="/home">К списку задач</router-link>
    </div>
    <div class="task-info" v-if="task">
      <h3>Задача {{ task.id }}</h3>
      <p>{{ task.taskText }}</p>
      <button @click="toggleCollapse">Подсказка</button>
      <div v-if="collapsed">
        {{ task.prompt }}
      </div>
    </div>
    <div>
      <p v-if="showResult" :style="{ color: resultColor }">{{ resultText }}</p>  
    </div>
    <div class="code-working">
      <div class="code-editor" v-if="task">
        <textarea class="code-editor-ta" v-model="code" placeholder="Введите ваш код на C++"></textarea>
      </div>
      <div class="compiler-output">
        <textarea class="compiler-output-ta" v-model="output" readonly></textarea>
      </div>
      <div class="buttons">
        <button class="button reset-button" @click="resetCode" title="Вернуть изначальный код задания">
          Сброс
        </button>
        <button class="button check-button" @click="checkCode" title="Проверить правильность выполнения">
          Проверка
        </button>
      </div>
    </div>
  </div>
</template>
  
  <script>
  import axios from 'axios';

  export default {
    props: ['id'], // Получение параметра id из URL

    computed: {
      userData() {
        return this.$root.userData; // Получение данных пользователя из корневого экземпляра Vue
      },
    },

    data() {
      return {
        task: null, // Объект задачи
        code: '', // Код на C++
        output: '', // Вывод результата компиляции
        collapsed: false,
        progress: null,
        showResult: false,
        resultText: "",
        resultColor: "",
      };
    },

    mounted() {
        this.fetchTask();
    },

    methods: {
      fetchTask() {
        // Выполняем HTTP-запрос для получения задачи по id
        const taskId = parseInt(this.id);
        axios.get(`http://localhost:8081/task/${taskId}`)
          .then(response => {
            this.task = response.data;
            this.code = this.task.file;
          })
          .catch(error => {
            console.error(error);
          });
      },

      toggleCollapse() {
        this.collapsed = !this.collapsed;
      },

      resetCode() {
        if (confirm('Вы уверены, что хотите сбросить код?')) {
          this.code = this.task.file; // Сброс кода на исходное значение
        }
      },

      checkCode() {
        // Код для проверки правильности выполнения
        this.compileCode()
      },

      async compileCode() {
        const requestData = {
          cmd: 'g++ main.cpp && ./a.out',
          src: this.code
        };

        try {
          const response = await axios.post('/api', requestData);
          const compiledCode = response.data;
          this.output = compiledCode;
          const codeLines = compiledCode.split('\n');
          const out1 = this.task.out1
          const out2 = this.task.out2
          const out3 = this.task.out3
          if (codeLines[0] === out1 && codeLines[1] === out2 && codeLines[2] === out3) {
            // Если все ответы совпали, то отправим запрос на текущий прогресс этой задачи
            const userId = this.userData.id;
            const taskId = this.id;
            axios.get(`http://localhost:8081/progress/usertask?user_id=${userId}&task_id=${taskId}`)
              .then(response => {
                // Обработка ответа от сервера
                this.progress = response.data;
                // Затем сверим, не была ли эта задача уже решена ранее, чтобы не делать лишние записи в бд
                if (!this.progress.completed) {
                  // Если не была решена ранее, то отправим put запрос с целью изменить информацию
                  axios.put('http://localhost:8081/progress/' + this.progress.id, { completed: true })
                    .then(response => {
                      console.log(response.data)
                      this.showResult = true;
                      this.resultText = "Задача успешно выполнена";
                      this.resultColor = "green"
                    })
                    .catch(error => {
                      // Обработка ошибки
                      console.error(error);
                    });
                } else {
                  // Если задача уже была решена ранее, просто выведем сообщение об успешном выполнении
                  this.showResult = true;
                  this.resultText = "Задача успешно выполнена";
                  this.resultColor = "green"
                }
              })
              .catch(error => {
                console.error(error);
              });  
          } else {
            // Код не соответствует ожидаемым выходным значениям
            this.showResult = true;
            this.resultText = "Ошибка выполнения задачи";
            this.resultColor = "red";
          }         
        } catch (error) {
          console.error(error);
          this.showResult = true;
          this.resultText = "Что-то пошло не так";
          this.resultColor = "red";
        }
      },
    },
  };
  </script>
  
  <style>
  .logout{
    display: flex;
    justify-content: flex-end;
  }

  .task-details {
    margin-top: 20px;
  }
  
  .task-info {
    background-color: #f2f2f2;
    padding: 10px;
    border: 1px solid #ccc;
  }

  .code-working {
    width: 90%;
    margin: 20px;
  }
  
  .code-editor {
    margin-top: 10px;
  }

  .compiler-output {
    margin-top: 10px;
  }

  .code-editor-ta {
    height: 350px;
  }

  .compiler-output-ta {
    height: 50px;
    background-color: #eee;
  }
  
  textarea {
    width: 100%;
    padding: 10px;
  }

  .button {
    font-family: 'Arial', sans-serif;
    font-size: 16px;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-right: 10px;
  }

  .reset-button {
    background-color: #999;
    color: #fff;
  }

  .check-button {
    background-color: #007bff;
    color: #fff;
  }

  .buttons {
    display: flex;
    justify-content: flex-start;
  }
  </style>