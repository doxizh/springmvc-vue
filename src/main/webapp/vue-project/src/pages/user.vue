<template>
<div class="user-page">
  <div class="body-title">
    <h4>用户管理</h4>
  </div>
  <div class="content-body">
    <div class="control-bar">
      <el-form :model="form" label-width="5em">
        <el-row :gutter="15">
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item prop="name" label="用户名：">
              <el-input v-model="form.name" placeholder="请输入用户名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <div class="btn-box">
              <el-button type="primary">查询</el-button>
              <el-button type="warning">重置</el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="table-box">
      <div class="btn-box">
        <el-button type="success">新增</el-button>
        <el-button type="primary" @click="batchAddUser">批量新增</el-button>
      </div>
      <el-table :data="userData" height="0">
        <el-table-column
          type="index"
          label="序号"
          width="50">
        </el-table-column>
        <el-table-column
          property="id"
          label="用户ID">
        </el-table-column>
        <el-table-column
          property="name"
          label="用户名">
        </el-table-column>
      </el-table>
      <el-pagination v-if="total>pageSize"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="pageSizes"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
      </el-pagination>
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
        },
        userData:[],
        pageSize:15,
        pageSizes:[15, 30, 45, 60],
        pageNum:1,
        total:0
      }
    },
    created() {
      this.findUserAll();
    },
    methods:{
      handleSizeChange(val) {
        console.log(`每页 ${val} 条`);
        this.pageSize=val;
        this.pageNum=1;
        this.findUserAll();
      },
      handleCurrentChange(val) {
        console.log(`当前页: ${val}`);
        this.pageNum=val;
        this.findUserAll();
      },
      findUserAll(){
        let postData={
          pageSize:this.pageSize,
          pageNum:this.pageNum,
        };
        this.$axios.post(this.$proxy+this.$apis.findUserAll,this.$qs.stringify(postData)).then(data=>{
          console.log(data);
          if(data.data.success){
            this.userData=data.data.result.list||[];
            this.total=data.data.result.total;
          }
        })
      },
      batchAddUser(){
        this.$axios.post(this.$proxy+this.$apis.batchAddUser).then(data=>{
          console.log(data);
          if(data.data.success){
            this.findUserAll();
          }
        })
      },
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
      display: flex;
      flex-flow: column;
      .control-bar,.table-box{
        padding: 15px;
        background: #fff;
        margin-top: 15px;
      }
      .control-bar{
        .el-form-item{
          margin-bottom: 0;
        }
      }
      .table-box{
        flex: 1;
        display: flex;
        flex-flow: column;
        .el-table{
          flex: 1;
          margin-top: 15px;
        }
        .el-pagination{
          margin-top: 15px;
          text-align: right;
        }
      }
    }
  }
</style>
