<template>
<div class="login" id="login">
  <el-container>
    <el-card class="form-box">
      <div class="login-header">登 录</div>
      <el-form ref="form" :model="form" label-width="4em">
        <el-form-item label="用户名">
          <el-input v-model="form.name" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item class="last" label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <p class="go-register-box">没有账号？<router-link class="go-register" :to="{path:'/register',query:{redirect:$route.query.redirect}}">去注册</router-link></p>
        <el-form-item>
          <el-button type="primary" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </el-container>
</div>
</template>

<script>
  import {mapMutations} from 'vuex';
  export default {
    name: "login",
    data() {
      return {
        form:{
          name:'',
          password:''
        }
      }
    },
    created() {
      // this.login();
    },
    methods:{
      ...mapMutations([
        'SAVE_USER_INFO',
        'changeLoginStatus'
      ]),
      login(){
        let postData={
          name:this.form.name,
          password:this.form.password
        };
        this.$axios.post(this.$proxy+this.$apis.login,postData).then(data=>{
          if(data.data.success){
            this.SAVE_USER_INFO(data.data.result.userData);
            this.changeLoginStatus(true);
            this.$router.push(this.$route.query.redirect||'home');
          }
        })
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  #login{
    height: 100%;
    position: relative;
  }
  .form-box{
    /*width: 400px;*/
    margin: 0 auto;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%,-50%);
    .login-header{
      text-align: center;
      font-size: 32px;
      color: $--color-primary;
    }
    .el-form{
      padding: 0 50px;
      padding-top: 40px;
      .el-form-item{
        &.last{
          margin-bottom: 0;
        }
      }
      .el-button{
        width: 100%;
      }
    }
    .go-register-box{
      font-size: 12px;
      text-align: right;
    }
  }
</style>
