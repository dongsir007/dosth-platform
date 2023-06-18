<template>
  <div>
    <el-form :rules="rules" ref="loginForm" :model="loginForm" class="loginContainer">
      <h3 class="loginTitle">
        系统登录
      </h3>
      <el-form-item prop="username">
        <el-input type="text" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input type="text" auto-complete="false" v-model="loginForm.code" placeholder="点击图片更换验证码" style="width:250px; margin-right:5px"></el-input>
        <img :src="captchaUrl">
      </el-form-item>
      <el-checkbox v-model="checked" class="loginRemember">记住我</el-checkbox>
      <el-button type="primary" style="width:100%" @click="submitLogin">登录</el-button>
    </el-form>
  </div>
</template>

<script>
import $router from 'vue-router'
export default {
  __name: 'Login',
  data () {
    return {
      captchaUrl: '',
      loginForm: {
        username: 'admin',
        password: '111111',
        code: ''
      },
      checked: true,
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 5, max: 14, message: '长度要在5到14位', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 14, message: '密码长度不能小于6', trigger: 'blur' }],
        code: [{ required: true, messsage: '请输入验证码', trigger: 'blur' }]
      }
    }
  },
  methods: {
    submitLogin () {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          alert('提交成功')
          localStorage.setItem('token', 123)
          this.$router.push('/index')
        } else {
          this.$message.error('登录出错请重新登录')
          return false
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .loginContainer {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 15px 35px 15px 35px;
    background: aliceblue;
    border: 1px solid blueviolet;
    box-shadow: 0 0 25px #f885ff;
  }
  .loginTitle {
    margin: 0px auto 48px auto;
    text-align: center;
    font-size: 40px;
  }
  .loginRemember {
    text-align: left;
    margin: 0 0 15px 0;
  }
  body {
    background-image: url('../assets/logo.png');
    background-size: 100%;
  }
</style>
