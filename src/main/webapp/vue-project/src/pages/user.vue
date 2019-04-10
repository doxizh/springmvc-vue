<template>
<div class="user-page">
  <div class="body-title">
    <h4>用户管理</h4>
  </div>
  <div class="content-body">
    <div class="control-bar">
      <el-form :model="controlBarForm" ref="controlBarForm" label-width="5em">
        <el-row :gutter="15">
          <el-col :xs="24" :sm="12" :md="6">
            <el-form-item prop="name" label="用户名：">
              <el-input v-model="controlBarForm.name" placeholder="请输入用户名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :sm="12" :md="6">
            <div class="btn-box">
              <el-button type="primary" @click="searchUserByName">查询</el-button>
              <el-button type="warning" @click="resetControlBarForm">重置</el-button>
            </div>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div class="table-box">
      <div class="btn-box">
        <el-button type="success" @click="addUserDialog=true">新增</el-button>
        <el-button type="primary" @click="batchAddUser">批量新增</el-button>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
      </div>
      <el-table :data="userData" height="0" @selection-change="handleSelectionChange">
        <el-table-column
          type="selection"
          width="55">
        </el-table-column>
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
  <el-dialog title="新增用户" :visible.sync="addUserDialog">
    <el-form status-icon :model="addUserDialogForm" ref="addUserDialogForm" :rules="addUserDialogRules">
      <el-form-item label="用户名" label-width="5em" prop="name">
        <el-input v-model="addUserDialogForm.name" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="addUserDialog=false">取 消</el-button>
      <el-button type="primary" @click="addUser" :loading="addUserDialogFormLoading">{{addUserDialogFormLoading?'请稍后':'确 定'}}</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
  export default {
    name: "user",
    data() {
      const checkName=(rule,value,callback)=>{
        if(value===''){
          callback(new Error("请输入用户名"));
        }else {
          callback();
        }
      };
      return {
        form:{
          name:''
        },
        userData:[],
        pageSize:15,
        pageSizes:[15, 30, 45, 60],
        pageNum:1,
        total:0,
        addUserDialog:false,
        controlBarForm:{
          name:''
        },
        addUserDialogForm:{
          name:'',
        },
        addUserDialogFormLoading:false,
        addUserDialogRules:{
          name:[
            {validator:checkName,trigger:'blur'}
          ]
        },
        selectedItem:[]
      }
    },
    created() {
      this.findUserAll();
    },
    methods:{
      handleSizeChange(val) {
        this.pageSize=val;
        this.pageNum=1;
        this.findUserAll();
      },
      handleCurrentChange(val) {
        this.pageNum=val;
        this.findUserAll();
      },
      findUserAll(){
        let postData={
          pageSize:this.pageSize,
          pageNum:this.pageNum,
        };
        this.$axios.post(this.$proxy+this.$apis.findUserAll,this.$qs.stringify(postData)).then(data=>{
          if(data.data.success){
            this.userData=data.data.result.list||[];
            this.total=data.data.result.total;
          }
        })
      },
      batchAddUser(){
        this.$axios.post(this.$proxy+this.$apis.batchAddUser).then(data=>{
          if(data.data.success){
            this.findUserAll();
          }
        })
      },
      addUser(){
        this.$refs.addUserDialogForm.validate(valid=>{
          if(valid){
            let postData={
              name:this.addUserDialogForm.name
            };
            this.addUserDialogFormLoading=true;
            this.$axios.post(this.$proxy+this.$apis.addUser,postData).then(data=>{
              this.addUserDialogFormLoading=false;
              if(data.data.success){
                this.$notify({
                  title: '提示',
                  message: '新增用户成功',
                  type: 'success'
                });
                this.$refs.addUserDialogForm.resetFields();
                this.addUserDialog=false;
              }else {
                this.$notify.error({
                  title: '错误',
                  message: data.data.msg
                });
              }
            })
          }
        })
      },
      searchUserByName(){
        this.pageNum=1;
        let postData={
          name:this.controlBarForm.name,
          pageSize:this.pageSize
        };
        this.$axios.post(this.$proxy+this.$apis.searchUserByName,postData).then(data=>{
          if(data.data.success){
            this.userData=data.data.result.list||[];
            this.total=data.data.result.total;
          }else {
            this.$notify.error({
              title: '错误',
              message: data.data.msg
            });
          }
        })
      },
      resetControlBarForm(){
        this.$refs.controlBarForm.resetFields();
      },
      handleSelectionChange(val){
        this.selectedItem=val;
      },
      batchDelete(){
        if(this.selectedItem.length>0){
          this.$confirm('确认删除所选用户吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let ids=[];
            for (let i = 0; i <this.selectedItem.length ; i++) {
              ids.push(this.selectedItem[i].id);
            }
            let postData={
              ids:ids
            };
            this.$axios.post(this.$proxy+this.$apis.batchDeleteUser,postData).then(data=>{
              if(data.data.success){
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
                this.pageNum=1;
                this.findUserAll();
              }else {

              }
            })
          }).catch(() => {

          });
        }else {
          this.$notify.error({
            title:'错误',
            message:"请选择要删除的用户"
          })
        }
      }
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
