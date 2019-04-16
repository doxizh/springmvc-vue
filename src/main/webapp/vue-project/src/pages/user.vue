<template>
  <div class="user-page page">
    <div class="body-title">
      <h4>用户管理</h4>
    </div>
    <div class="content-body">
      <div class="control-bar">
        <el-form :inline="true" :model="controlBarForm" ref="controlBarForm">
          <el-form-item prop="name" label="用户名：">
            <el-input v-model="controlBarForm.name" placeholder="请输入用户名"></el-input>
          </el-form-item>
          <el-form-item prop="selectDate" label="选择日期：">
            <el-date-picker
              v-model="controlBarForm.selectDate"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item prop="role" label="角色：">
            <el-select v-model="controlBarForm.role">
              <el-option value=""></el-option>
              <el-option v-for="item in roles" :value="item.id" :label="item.roleName" :key="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="btn-box">
            <el-button type="primary" @click="searchUser">查询</el-button>
            <el-button type="warning" @click="resetControlBarForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="table-box">
        <div class="btn-box">
          <el-button type="success" @click="addUserDialog=true">新增</el-button>
          <el-button type="primary" @click="batchAddUser" :loading="batchAddUserLoading">{{batchAddUserLoading?"新增中":"批量新增"}}</el-button>
          <el-button type="danger" @click="batchDelete" :loading="batchDeleteLoading">{{batchDeleteLoading?"删除中":"批量删除"}}</el-button>
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
          <el-table-column
            property="nickname"
            label="昵称">
          </el-table-column>
          <el-table-column
            property="roleNames"
            label="角色">
          </el-table-column>
          <el-table-column
            property="createDate"
            label="创建时间">
          </el-table-column>
          <el-table-column
            property="updateDate"
            label="更新时间">
          </el-table-column>
          <el-table-column
            label="操作">
            <template slot-scope="scope">
              <el-button @click="openCheckDialog(scope.row)" type="text" size="small">查看</el-button>
              <el-button @click="openEditDialog(scope.row)" type="text" size="small">编辑</el-button>
              <el-button type="text" size="small" @click="deleteUser(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination background
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
        <el-button type="primary" @click="addUser" :loading="addUserDialogFormLoading">
          {{addUserDialogFormLoading?'请稍后':'确 定'}}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑用户" :visible.sync="editUserDialog">
      <el-form status-icon :model="editUserDialogForm" ref="editUserDialogForm" :rules="editUserDialogRules" label-width="5em">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="editUserDialogForm.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="roleIds">
          <el-checkbox-group v-model="editUserDialogForm.roleIds">
            <el-checkbox v-for="item in roles" :key="item.id.toString()" :value="item.id.toString()" :label="item.id.toString()" name="roleIds">{{item.roleName}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editUserDialog=false">取 消</el-button>
        <el-button type="primary" @click="editUser" :loading="editUserDialogLoading">
          {{editUserDialogLoading?'请稍后':'确定'}}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title="查看用户" :visible.sync="checkUserDialog">
      <div class="dialog-body">
        <div class="lists">
          <div class="item">
            <div class="label">用户ID：</div>
            <div class="text">{{selectedItem.id}}</div>
          </div>
          <div class="item">
            <div class="label">用户名：</div>
            <div class="text">{{selectedItem.name}}</div>
          </div>
          <div class="item">
            <div class="label">角色：</div>
            <div class="text">{{selectedItem.roleNames}}</div>
          </div>
          <div class="item">
            <div class="label">创建日期：</div>
            <div class="text">{{selectedItem.createDate}}</div>
          </div>
          <div class="item">
            <div class="label">修改日期：</div>
            <div class="text">{{selectedItem.updateDate}}</div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="checkUserDialog=false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "user",
    data() {
      const checkName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error("请输入用户名"));
        } else {
          callback();
        }
      };
      return {
        userData: [],
        pageSize: 15,
        pageSizes: [15, 30, 45, 60],
        pageNum: 1,
        total: 0,
        addUserDialog: false,
        editUserDialog: false,
        controlBarForm: {
          name: '',
          selectDate: '',
          nickname: '',
          role:''
        },
        addUserDialogForm: {
          name: '',
          nickname: ''
        },
        editUserDialogForm: {
          nickname: '',
          roleIds:[],
        },
        addUserDialogFormLoading: false,
        editUserDialogLoading: false,
        addUserDialogRules: {
          name: [
            {validator: checkName, trigger: 'blur'}
          ]
        },
        editUserDialogRules: {
          roleIds:[
            { type: 'array', required: true, message: '请至少选择一个角色', trigger: 'change' }
          ]
        },
        selectedItems: [],
        selectedItem: {},
        checkUserDialog:false,
        batchDeleteLoading:false,
        deleteUserLoading:false,
        batchAddUserLoading:false,
        roles:[],
      }
    },
    created() {
      this.findUserAll();
      this.getRoles();
    },
    methods: {
      getRoles(){
        let postData = {
          pageSize: 999,
          pageNum: 1,
        };
        this.$axios.post(this.$apis.getRoles,postData).then(data=>{
          if(data.data.success){
            this.roles=data.data.result.list||[];
          }
        })
      },
      openEditDialog(row) {
        this.editUserDialogForm.roleIds=[];
        this.selectedItem = row;
        this.editUserDialogForm.nickname = row.nickname;
        let roleIds=!!row.roleIds?row.roleIds.split(','):[];
        this.roles.map(item=>{
          if(roleIds.indexOf(item.id.toString())>-1){
            this.editUserDialogForm.roleIds.push(item.id.toString());
          }
        });
        this.editUserDialog = true;
      },
      editUser() {
        this.$refs.editUserDialogForm.validate(valid => {
          if (valid) {
            let postData = {
              id: this.selectedItem.id,
              nickname: this.editUserDialogForm.nickname,
              roleIds: this.editUserDialogForm.roleIds
            };
            this.editUserDialogLoading = true;
            this.$axios.post(this.$apis.editUser, postData).then(data => {
              this.editUserDialogLoading = false;
              if (data.data.success) {
                this.$message({
                  type: 'success',
                  message: '编辑成功!'
                });
                this.$refs.editUserDialogForm.resetFields();
                this.editUserDialog = false;
                this.pageNum = 1;
                this.findUserAll();
              } else {
                this.$message({
                  type: 'error',
                  message: data.data.msg
                });
              }
            }).catch(()=>{
              this.$notify.error({
                message:"网络异常",
              });
              this.editUserDialogLoading=false;
            })
          }
        })
      },
      deleteUser(row) {
        this.$confirm('确认删除该用户吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteUserLoading=true;
          let postData = {
            id: row.id
          };
          this.$axios.post(this.$apis.deleteUser, postData).then(data => {
            this.deleteUserLoading=false;
            if (data.data.success) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.pageNum = 1;
              this.findUserAll();
            } else {

            }
          }).catch(()=>{
            this.$notify.error({
              message:"网络异常",
            });
            this.deleteUserLoading=false;
          })
        }).catch(() => {

        });
      },
      openCheckDialog(row){
        this.selectedItem = row;
        console.log(this.selectedItem);
        this.checkUserDialog = true;
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.pageNum = 1;
        this.searchUser();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.searchUser();
      },
      findUserAll() {
        let postData = {
          pageSize: this.pageSize,
          pageNum: this.pageNum,
        };
        this.$axios.post(this.$apis.findUserAll, this.$qs.stringify(postData)).then(data => {
          if (data.data.success) {
            this.userData = data.data.result.list || [];
            this.total = data.data.result.total;
          }
        })
      },
      batchAddUser() {
        this.batchAddUserLoading=true;
        this.$axios.post(this.$apis.batchAddUser).then(data => {
          this.batchAddUserLoading=false;
          if (data.data.success) {
            this.findUserAll();
          }
        }).catch(()=>{
          this.$notify.error({
            message:"网络异常",
          });
          this.batchAddUserLoading=false;
        })
      },
      addUser() {
        this.$refs.addUserDialogForm.validate(valid => {
          if (valid) {
            this.addUserDialogFormLoading = true;
            let postData = {
              name: this.addUserDialogForm.name
            };
            this.$axios.post(this.$apis.addUser, postData).then(data => {
              this.addUserDialogFormLoading = false;
              if (data.data.success) {
                this.$notify({
                  title: '提示',
                  message: '新增用户成功',
                  type: 'success'
                });
                this.$refs.addUserDialogForm.resetFields();
                this.addUserDialog = false;
                this.pageNum = 1;
                this.findUserAll();
              } else {
                this.$notify.error({
                  title: '错误',
                  message: data.data.msg
                });
              }
            }).catch(()=>{
              this.$notify.error({
                message:"网络异常",
              });
              this.addUserDialogFormLoading=false;
            })
          }
        })
      },
      searchUser() {
        let postData = {
          pageNum: this.pageNum,
          pageSize: this.pageSize
        };
        if(!!this.controlBarForm.name){
          postData.name=this.controlBarForm.name;
        }
        if(!!this.controlBarForm.selectDate){
          postData.startDate=this.controlBarForm.selectDate[0];
          postData.endDate=this.controlBarForm.selectDate[1];
        }
        if(!!this.controlBarForm.role){
          postData.roleId=this.controlBarForm.role;
        }
        this.$axios.post(this.$apis.searchUser, postData).then(data => {
          if (data.data.success) {
            this.userData = data.data.result.list || [];
            this.total = data.data.result.total;
          } else {
            this.$notify.error({
              title: '错误',
              message: data.data.msg
            });
          }
        })
      },
      resetControlBarForm() {
        this.$refs.controlBarForm.resetFields();
      },
      handleSelectionChange(val) {
        this.selectedItems = val;
      },
      batchDelete() {
        if (this.selectedItems.length > 0) {
          this.$confirm('确认删除所选用户吗?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.batchDeleteLoading=true;
            let ids = [];
            for (let i = 0; i < this.selectedItems.length; i++) {
              ids.push(this.selectedItems[i].id);
            }
            let postData = {
              ids: ids
            };
            this.$axios.post(this.$apis.batchDeleteUser, postData).then(data => {
              this.batchDeleteLoading=false;
              if (data.data.success) {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
                this.pageNum = 1;
                this.findUserAll();
              } else {

              }
            }).catch(()=>{
              this.$notify.error({
                message:"网络异常",
              });
              this.batchDeleteLoading=false;
            })
          }).catch(() => {

          });
        } else {
          this.$notify.error({
            title: '错误',
            message: "请选择要删除的用户"
          })
        }
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>

</style>
