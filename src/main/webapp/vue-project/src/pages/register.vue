<template>
<div class="register" id="register">
  <el-container>
    <el-card class="form-box">
      <div class="login-header">注册</div>
      <el-form status-icon ref="registerForm" :rules="rules" :model="registerForm" label-width="5em">
        <el-form-item label="用户名" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item class="last" label="确认密码" prop="repassword">
          <el-input v-model="registerForm.repassword" type="password" placeholder="请确认输入密码"></el-input>
        </el-form-item>
        <p class="go-login-box">已有账号，<router-link class="go-login" :to="{path:'/login',query:{redirect:$route.query.redirect}}">去登录</router-link></p>
        <el-form-item>
          <el-button type="primary" @click="register">注册</el-button>
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
      const checkName=(rule, value, callback)=>{
        if (value === '') {
          callback(new Error('请输入用户名'));
        } else {
          callback();
        }
      };
      const checkPassword=(rule, value, callback)=>{
        if (value === '') {
          callback(new Error('请输入密码'));
        } else if (this.registerForm.repassword!==''&&value !== this.registerForm.repassword) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      const checkRePassword=(rule, value, callback)=>{
        console.log(value);
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.registerForm.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        registerForm:{
          name:'',
          password:'',
          repassword:'',
        },
        rules: {
          name: [
            { validator: checkName, trigger: 'blur' }
          ],
          password: [
            { validator: checkPassword, trigger: 'blur' }
          ],
          repassword: [
            { validator: checkRePassword, trigger: 'blur' }
          ],
        }
      }
    },
    created() {

    },
    methods:{
      ...mapMutations([
        'SAVE_USER_INFO',
        'changeLoginStatus'
      ]),
      register(){
        let postData={
          name:this.registerForm.name,
          password:this.registerForm.password
        };
        this.$refs.registerForm.validate((valid)=>{
          if(valid){
            this.$axios.post(this.$proxy+this.$apis.register,postData).then(data=>{
              if(data.data.success){
                this.SAVE_USER_INFO(data.data.result.userData);
                this.changeLoginStatus(true);
                this.$router.push(this.$route.query.redirect||'home');
              }else {
                this.$notify.error({
                  message:data.data.msg,
                  title:"错误"
                })
              }
            })
          }else {
            return false;
          }
        })
      },
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
  #register{
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
    .go-login-box{
      font-size: 12px;
      text-align: right;
    }
  }
</style>
