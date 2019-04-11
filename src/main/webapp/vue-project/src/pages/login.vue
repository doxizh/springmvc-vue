<template>
<div class="login" id="login">
  <el-container>
    <el-card class="form-box">
      <div class="login-header">登 录</div>
      <el-form status-icon ref="loginForm" :model="loginForm" :rules="loginFormRules" label-width="4em">
        <el-form-item label="用户名" prop="name">
          <el-input v-model="loginForm.name" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item class="last" label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <p class="go-register-box">没有账号？<router-link class="go-register" :to="{path:'/register',query:{redirect:$route.query.redirect}}">去注册</router-link></p>
        <el-form-item>
          <el-button type="primary" @click="login" :loading="loginLoading">{{loginLoading?"登录中":"登录"}}</el-button>
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
      const checkName=(rule,value,callback)=>{
        if(value===''){
          callback(new Error("请输入用户名"));
        }else {
          callback();
        }
      };
      const checkPassword=(rule,value,callback)=>{
        if(value===''){
          callback(new Error("请输入密码"));
        }else {
          callback();
        }
      };
      return {
        loginForm:{
          name:'',
          password:''
        },
        loginFormRules:{
          name:[
            {validator:checkName,trigger:'blur'}
          ],
          password:[
            {validator:checkPassword,trigger:'blur'}
          ],
        },
        loginLoading:false
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
        this.$refs.loginForm.validate(valid=>{
          if(valid){
            let postData={
              name:this.loginForm.name,
              password:this.loginForm.password
            };
            this.loginLoading=true;
            this.$axios.post(this.$proxy+this.$apis.login,postData).then(data=>{
              this.loginLoading=false;
              if(data.data.success){
                this.SAVE_USER_INFO(data.data.result.userData);
                this.changeLoginStatus(true);
                this.$router.push(this.$route.query.redirect||'home');
              }
            })
          }
        });
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
