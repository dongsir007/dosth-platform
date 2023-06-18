<template>
  <router-view/>
</template>
<script>
import Login from './views/LoginView.vue'
export default {
  name: 'App',
  components: [
    Login
  ]
}
</script>
