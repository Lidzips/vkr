<template>
  <div class="form-container">
    <div class="task-navigation">
      <router-link to="/admin">К списку задач</router-link>
    </div>
    <div>
      <h3>Новая задача</h3>
    </div>
    <div class="inputs">
      <p v-if="showResult" :style="{ color: resultColor }">{{ resultText }}</p>  
    </div>
    <div class="inputs">
      <label for="complexity">Сложность (1-5):</label>
      <select id="complexity" v-model="complexity">
        <option value="1">1</option>
        <option value="2">2</option>
        <option value="3">3</option>
        <option value="4">4</option>
        <option value="5">5</option>
      </select>
    </div>

    <div class="inputs">
      <label for="taskText">Текст задачи:</label>
      <textarea id="taskText" v-model="taskText"></textarea>
    </div>

    <div class="inputs">
      <label for="file">Файл:</label>
      <textarea class="codePaste" id="file" v-model="file"></textarea>
    </div>

    <div class="inputs">
      <label for="prompt">Подсказка:</label>
      <input type="text" id="prompt" v-model="prompt" />
    </div>

    <div class="inputs">
      <label for="topic">Тема:</label>
      <input type="text" id="topic" v-model="topic" />
    </div>

    <div class="inputs">
      <label for="out1">Выход 1:</label>
      <input type="text" id="out1" v-model="out1" />
    </div>

    <div class="inputs">
      <label for="out2">Выход 2:</label>
      <input type="text" id="out2" v-model="out2" />
    </div>

    <div class="inputs">
      <label for="out3">Выход 3:</label>
      <input type="text" id="out3" v-model="out3" />
    </div>

    <div class="buttons">
      <button class="button check-button" @click="createTask" title="Перезаписать текущие значения">
        Создать
      </button>
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
    },

    data() {
      return {
        task: null,
        complexity: null,
        file: '',
        prompt: '',
        topic: '',
        out1: '',
        out2: '',
        out3: '',
        showResult: false,
        resultText: "",
        resultColor: "",
      };
    },

    mounted() {
        
    },

    methods: {
      createTask() {
        if (confirm('Вы уверены, что хотите создать новую задачу с текущими значениями?')) {
          axios.post('http://localhost:8081/task/', 
          { complexity: this.complexity,
            taskText: this.taskText,
            file: this.file,
            prompt: this.prompt,
            topic: this.topic,
            out1: this.out1,
            out2: this.out1,
            out3: this.out1
          })
            .then(response => {
              console.log(response.data)
              this.showResult = true;
              this.resultText = "Данные успешно сохранены";
              this.resultColor = "green"
            })
            .catch(error => {
              // Обработка ошибки
              console.error(error);
              this.showResult = true;
              this.resultText = "Ошибка сохранения";
              this.resultColor = "red";
            });
        }
      },
    },
  };

</script>

<style>
  .form-container {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
    margin: 0 auto;
  }

  .form-container label {
    font-weight: bold;
  }

  .form-container input[type="text"],
  .form-container textarea 
  .form-container select {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    box-sizing: border-box;
  }

  .codePaste {
    height: 280px;
  }

  .inputs {
    width: 80%;
  }

</style>