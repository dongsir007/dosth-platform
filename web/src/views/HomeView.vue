<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <el-link icon="el-icon-switch-button" @click="logOut">退出</el-link>
  </div>
</template>

<script>

export default {
  name: 'HomeView',
  components: {
  },
  methods: {
    // 退出登录
    logOut () {
      localStorage.removeItem('token')
      this.$router.push('/login')
    }
  }
}
</script>
