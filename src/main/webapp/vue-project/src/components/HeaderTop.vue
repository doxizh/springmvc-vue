<template>
  <div class="header-top">
    <div class="logo-box"></div>
    <!--<el-menu :default-active="activeIndex" @select="handleSelect" mode="horizontal" text-color="#fff" active-text-color="#ffd04b">
      <el-menu-item index="1">首页</el-menu-item>
    </el-menu>-->
    <div class="user-box">
      <el-dropdown @command="handleCommand">
        <span class="el-dropdown-link">
          {{userInfo.name}}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="openUpdatePasswordDialog">修改密码</el-dropdown-item>
          <el-dropdown-item command="logout">注销</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible">
      <el-form status-icon :model="updatePasswordForm" ref="updatePasswordForm" :rules="rules">
        <el-form-item label="原密码" :label-width="formLabelWidth" prop="oldPassword">
          <el-input type="password" v-model="updatePasswordForm.oldPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="新密码" :label-width="formLabelWidth" prop="newPassword">
          <el-input type="password" v-model="updatePasswordForm.newPassword" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" :label-width="formLabelWidth" prop="rePassword">
          <el-input type="password" v-model="updatePasswordForm.rePassword" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updatePassword" :loading="loading">{{loading?'请稍后':'确 定'}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {mapState,mapMutations} from 'vuex';
  export default {
    name: "HeaderTop",
    data() {
      const checkOldPassword=(rule,value,callback)=>{
        if(value===''){
          callback(new Error('请输入原密码'))
        }else{
          callback();
        }
      };
      const checkNewPassword=(rule,value,callback)=>{
        if(value===''){
          callback(new Error('请输入新密码'));
        }else if(this.updatePasswordForm.rePassword!==''&&this.updatePasswordForm.rePassword!==value){
          callback(new Error('两次输入密码不一致'))
        } else{
          callback();
        }
      };
      const checkRePassword=(rule,value,callback)=>{
        if(value===''){
          callback(new Error('请再次输入新密码'))
        }else if(this.updatePasswordForm.newPassword!==value){
          callback(new Error('两次输入密码不一致'))
        } else{
          callback();
        }
      };
      return {
        activeIndex: "1",
        dialogFormVisible:false,
        loading:false,
        formLabelWidth: '5em',
        updatePasswordForm:{
          oldPassword:'',
          newPassword:'',
          rePassword:''
        },
        rules:{
          oldPassword:[
            {validator:checkOldPassword, trigger:'blur'}
          ],
          newPassword:[
            {validator:checkNewPassword, trigger:'blur'}
          ],
          rePassword:[
            {validator:checkRePassword, trigger:'blur'}
          ]
        }
      }
    },
    computed:{
      ...mapState([
        'userInfo'
      ])
    },
    created(){
      // console.log(this.userInfo);
    },
    methods: {
      ...mapMutations([
        'changeLoginStatus'
      ]),
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
      handleCommand(command){
        command&&this[command]();
      },
      openUpdatePasswordDialog(){
        this.dialogFormVisible=true;
      },
      updatePassword(){
        this.$refs.updatePasswordForm.validate(valid=>{
          if(valid){
            let postData={
              oldPassword:this.updatePasswordForm.oldPassword,
              newPassword:this.updatePasswordForm.newPassword,
            };
            this.loading=true;
            this.$axios.post(this.$proxy+this.$apis.updatePassword,postData).then(data=>{
              this.loading=false;
              if(data.data.success){
                this.$notify({
                  title: '提示',
                  message: '修改密码成功',
                  type: 'success'
                });
                this.$refs.updatePasswordForm.resetFields();
                this.dialogFormVisible=false;
              }
            })
          }
        });
      },
      logout(){
        this.$axios.post(this.$proxy+this.$apis.logout).then(data=>{
          if(data.data.success){
            this.changeLoginStatus(false);
            this.$router.push('Login');
          }
        })
      }
    }
  }
</script>

<style scoped type="text/scss" lang="scss">
  .header-top{
    background: #545c64;
    height: 100%;
    display: flex;
    justify-content: space-between;
  }
  .logo-box {
    width: 200px;
    height: 60px;
    font-size: 0;
  }
  .el-menu{
    background: none;
    flex: 1;
    border: none;
  }
  .user-box{
    width: 200px;
    height: 60px;
    color: #fff;
    display: flex;
    justify-content: center;
    align-items: center;
    .el-dropdown-link {
      cursor: pointer;
      color: #409EFF;
    }
    .el-icon-arrow-down {
      font-size: 12px;
    }
  }
</style>
