<template>
<div class="user-page">
  <div class="body-title">
    <h4>用户管理</h4>
  </div>
  <div class="content-body">
    <div class="control-bar">
      <el-form :model="form" label-width="5em">
        <el-row>
          <el-col span="6">
            <el-form-item prop="name" label="用户名：">
              <el-input v-model="form.name" placeholder="请输入用户名"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="table-box">
      <el-table></el-table>
    </div>
  </div>
</div>
</template>

<script>
  export default {
    name: "user",
    data() {
      return {
        form:{
          name:''
        }
      }
    },
    created() {

    },
    methods:{

    }
  }
</script>

<style type="text/scss" lang="scss" scoped>
.body-title{
  background: #fff;
  padding: 10px 15px;
  h4{
    margin: 0;
    padding-left: 8px;
    position: relative;
    &:before{
      content: '';
      position: absolute;
      left: 0;
      top: 3px;
      width: 2px;
      height: 1em;
      background-color: $--color-primary;
    }
  }
}
  .user-page{
    display: flex;
    flex-flow: column;
    width: 100%;
    .content-body{
      flex: 1;
      .control-bar,.table-box{
        padding: 15px;
        background: #fff;
        margin-top: 15px;
      }
    }
  }
</style>
