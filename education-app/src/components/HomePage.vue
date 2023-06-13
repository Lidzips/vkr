<template>
  <div class="home-page">
    <div class="content">
      <h1>Добро пожаловать на платформу по изучению C++!</h1>
      <p>
        Наша платформа предоставляет удобное решение для изучения и практики языка программирования C++.
        Здесь вы найдете интересные задания и проекты, которые помогут вам освоить и совершенствовать свои навыки в C++.
      </p>
      <p>
        Мы предлагаем широкий выбор задач и проектов на разные темы в C++, начиная от основ и базовых концепций
        до более сложных и продвинутых тем, таких как алгоритмы, структуры данных и многопоточное программирование.
        Выбирайте задания, которые вас интересуют, выполняйте их и отслеживайте свой прогресс на нашей платформе.
      </p>
      <p v-if="!isUserLoggedIn">
        Зарегистрируйтесь, чтобы начать использовать платформу и получить доступ к полному спектру возможностей.
        Учите новое, развивайтесь и достигайте новых высот в программировании на C++!
      </p>
      <button v-else @click="goToTasks" class="action-button">
        {{ isUserAdmin ? 'Перейти к администрированию' : 'Перейти к задачам' }}
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomePage',
  computed: {
    isUserLoggedIn() {
      return this.$root.userData !== null;
    },
    isUserAdmin() {
      return this.$root.userData !== null && this.$root.userData.isAdmin;
    },
  },
  methods: {
    goToTasks() {
      if (this.isUserAdmin) {
        this.$router.push('/admin');
      } else {
        this.$router.push('/tasks');
      }
    },
  },
};
</script>

<style scoped>
.home-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

.content {
  border: 1px solid #ccc;
  border-radius: 10px;
  padding: 20px;
  margin-bottom: 20px;
}

.action-button {
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  font-size: 16px;
  cursor: pointer;
}

.action-button:hover {
  background-color: #0056b3;
}

</style>