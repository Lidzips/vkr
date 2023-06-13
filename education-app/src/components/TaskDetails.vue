<template>
  <div class="task-details">
    <div class="task-navigation">
      <router-link to="/tasks">К списку задач</router-link>
    </div>
    <div class="task-info" v-if="task">
      <h3>Задача {{ task.id }}</h3>
      <div class="task-difficulty">
        <label for="stars">Сложность:</label>
        <span id="stars" class="star" v-for="index in 5" :key="index" :class="{ 'star-filled': index <= task.complexity }">&#9733;</span>
      </div>
      <div class="task-status" v-if="progress">
        <label for="status">Статус:</label>
        <span id="status" class="status-completed" v-if="progress.completed"> Выполнено</span>
        <span id="status" class="status-not-completed" v-else> Не выполнено</span>
      </div>
      <p>{{ task.taskText }}</p>
      <button class="button hint-button" @click="toggleCollapse">Подсказка</button>
      <div v-if="collapsed" class="collapsed-hint">
        {{ task.prompt }}
      </div>
    </div>
    <div>
      <p v-if="showResult" :style="{ color: resultColor }">{{ resultText }}</p>  
    </div>
    <div class="code-working">
      <div class="code-editor" v-if="task">
        <textarea id="editor" ref="codeEditorTextarea" class="code-editor-ta" v-model="code" placeholder="Введите ваш код на C++"></textarea>
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
  import CodeMirror from 'codemirror';
  import 'codemirror/lib/codemirror.css';
  import 'codemirror/theme/eclipse.css';
  import 'codemirror/mode/clike/clike';

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
        codeEditorInitialized: false,
      };
    },

    beforeMount() {
        this.fetchTask();
    },

    watch: {
      '$root.userData': {
        immediate: true,
        handler(userData) {
          if (userData) {
            this.fetchTaskProgress();
          }
        },
      },
    },

    updated() {
      if (!this.codeEditorInitialized) {
        const textarea = document.getElementById('editor');
        if (!textarea) return;

        CodeMirror.fromTextArea(textarea, {
          lineNumbers: true,
          mode: 'text/x-c++src',
          theme: 'eclipse',
        }).on('change', (editor) => {
          this.code = editor.getValue();
        });

        this.codeEditorInitialized = true;
      }
      
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

      fetchTaskProgress() {
        // Выполняем HTTP-запрос для получения прогресса по id
        const userId = this.userData.id;
        const taskId = this.id;
        axios.get(`http://localhost:8081/progress/usertask?user_id=${userId}&task_id=${taskId}`)
          .then(response => {
            this.progress = response.data;
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
            // Если при компиляции получен ожидаемый результат..
            if (!this.progress.completed) {
              // Если не была решена ранее, то отправим put запрос с целью изменить информацию
              axios.put('http://localhost:8081/progress/' + this.progress.id, { completed: true })
                .then(response => {
                  this.progress = response.data;
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
  .task-details {
    margin-top: 20px;
  }

  .CodeMirror {
    text-align: left !important;
  }
  
  .task-info {
    background-color: #f2f2f2;
    padding: 10px;
    border: 1px solid #ccc;
  }

  .collapsed-hint {
    background-color: white;
    border: 1px solid #ccc;
    padding: 10px;
    margin-top: 10px;
    box-shadow: inset 2px 2px 4px rgba(0, 0, 0, 0.2);
    white-space: pre-line;
    white-space: pre;
    text-align: left !important;
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

  .hint-button {
    background-color: #999;
    color: #fff;
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

  .task-difficulty {
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .star {
    font-size: 20px;
    color: #ccc;
  }

  .star-filled {
    color: gold;
  }
  </style>