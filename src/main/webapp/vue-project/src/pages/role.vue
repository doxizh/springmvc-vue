<template>
  <div class="role-page page">
    <div class="body-title">
      <h4>角色管理</h4>
    </div>
    <div class="content-body">
      <div class="control-bar">
        <el-form :inline="true" :model="controlBarForm" ref="controlBarForm">
          <el-form-item prop="roleName" label="角色名：">
            <el-input v-model="controlBarForm.roleName" placeholder="请输入角色名"></el-input>
          </el-form-item>
          <el-form-item class="btn-box">
            <el-button type="primary" @click="searchRole">查询</el-button>
            <el-button type="warning" @click="resetControlBarForm">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="table-box">
        <div class="btn-box">
          <el-button type="success" @click="addRoleDialog=true">新增</el-button>
          <el-button type="danger" @click="batchDelete" :loading="batchDeleteLoading">{{batchDeleteLoading?"删除中":"批量删除"}}</el-button>
        </div>
        <el-table :data="roleData" height="0" @selection-change="handleSelectionChange">
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
            label="角色ID">
          </el-table-column>
          <el-table-column
            property="roleName"
            label="角色名">
          </el-table-column>
          <el-table-column
            label="操作">
            <template slot-scope="scope">
              <el-button @click="openCheckDialog(scope.row)" type="text" size="small">查看</el-button>
              <el-button @click="openEditDialog(scope.row)" type="text" size="small">编辑</el-button>
              <el-button type="text" size="small" @click="deleteRole(scope.row)">删除</el-button>
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
    <el-dialog title="新增角色" :visible.sync="addRoleDialog">
      <el-form status-icon :model="addRoleDialogForm" ref="addRoleDialogForm" :rules="addRoleDialogRules">
        <el-form-item label="角色名" label-width="5em" prop="name">
          <el-input v-model="addRoleDialogForm.name" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addRoleDialog=false">取 消</el-button>
        <el-button type="primary" @click="addRole" :loading="addRoleDialogFormLoading">
          {{addRoleDialogFormLoading?'请稍后':'确 定'}}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑角色" :visible.sync="editRoleDialog">
      <el-form status-icon :model="editRoleDialogForm" ref="editRoleDialogForm" :rules="editRoleDialogRules" label-width="5em">
        <el-form-item label="角色名" prop="roleName">
          <el-input v-model="editRoleDialogForm.roleName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editRoleDialog=false">取 消</el-button>
        <el-button type="primary" @click="editRole" :loading="editRoleDialogLoading">
          {{editRoleDialogLoading?'请稍后':'确定'}}
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title="查看角色" :visible.sync="checkRoleDialog">
      <div class="dialog-body">
        <div class="lists">
          <div class="item">
            <div class="label">角色ID：</div>
            <div class="text">{{selectedItem.id}}</div>
          </div>
          <div class="item">
            <div class="label">角色名：</div>
            <div class="text">{{selectedItem.roleName}}</div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="checkRoleDialog=false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "role",
    data() {
      const checkName = (rule, value, callback) => {
        if (value === '') {
          callback(new Error("请输入角色名"));
        } else {
          callback();
        }
      };
      return {
        roleData: [],
        pageSize: 15,
        pageSizes: [15, 30, 45, 60],
        pageNum: 1,
        total: 0,
        addRoleDialog: false,
        editRoleDialog: false,
        controlBarForm: {
          roleName: ''
        },
        addRoleDialogForm: {
          roleName: ''
        },
        editRoleDialogForm: {
          roleName: ''
        },
        addRoleDialogFormLoading: false,
        editRoleDialogLoading: false,
        addRoleDialogRules: {
          roleName: [
            {validator: checkName, trigger: 'blur'}
          ]
        },
        editRoleDialogRules: {
          roleName: [
            {validator: checkName, trigger: 'blur'}
          ]
        },
        selectedItems: [],
        selectedItem: {},
        checkRoleDialog:false,
        batchDeleteLoading:false,
        deleteRoleLoading:false,
        batchAddRoleLoading:false,
        roles:[]
      }
    },
    created() {
      this.searchRole();
    },
    methods: {
      openEditDialog(row) {
        this.selectedItem = row;
        this.editRoleDialogForm.nickname = row.nickname;
        this.editRoleDialog = true;
      },
      editRole() {
        this.$refs.editRoleDialogForm.validate(valid => {
          if (valid) {
            let postData = {
              id: this.selectedItem.id,
              nickname: this.editRoleDialogForm.nickname
            };
            this.editRoleDialogLoading = true;
            this.$axios.post(this.$apis.editRole, postData).then(data => {
              this.editRoleDialogLoading = false;
              if (data.data.success) {
                this.$message({
                  type: 'success',
                  message: '编辑成功!'
                });
                this.$refs.editRoleDialogForm.resetFields();
                this.editRoleDialog = false;
                this.pageNum = 1;
                this.findRoleAll();
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
              this.editRoleDialogLoading=false;
            })
          }
        })
      },
      deleteRole(row) {
        this.$confirm('确认删除该角色吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.deleteRoleLoading=true;
          let postData = {
            id: row.id
          };
          this.$axios.post(this.$apis.deleteRole, postData).then(data => {
            this.deleteRoleLoading=false;
            if (data.data.success) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              this.pageNum = 1;
              this.findRoleAll();
            } else {

            }
          }).catch(()=>{
            this.$notify.error({
              message:"网络异常",
            });
            this.deleteRoleLoading=false;
          })
        }).catch(() => {

        });
      },
      openCheckDialog(row){
        this.selectedItem = row;
        console.log(this.selectedItem);
        this.checkRoleDialog = true;
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.pageNum = 1;
        this.findRoleAll();
      },
      handleCurrentChange(val) {
        this.pageNum = val;
        this.findRoleAll();
      },
      addRole() {
        this.$refs.addRoleDialogForm.validate(valid => {
          if (valid) {
            this.addRoleDialogFormLoading = true;
            let postData = {
              name: this.addRoleDialogForm.name
            };
            this.$axios.post(this.$apis.addRole, postData).then(data => {
              this.addRoleDialogFormLoading = false;
              if (data.data.success) {
                this.$notify({
                  title: '提示',
                  message: '新增角色成功',
                  type: 'success'
                });
                this.$refs.addRoleDialogForm.resetFields();
                this.addRoleDialog = false;
                this.pageNum = 1;
                this.findRoleAll();
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
              this.addRoleDialogFormLoading=false;
            })
          }
        })
      },
      searchRole() {
        let postData = {
          pageSize: this.pageSize,
          pageNum: this.pageNum
        };
        if(!!this.controlBarForm.roleName){
          postData.roleName=this.controlBarForm.roleName;
        }
        this.$axios.post(this.$apis.getRoles, postData).then(data => {
          if (data.data.success) {
            this.roleData = data.data.result.list || [];
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
          this.$confirm('确认删除所选角色吗?', '提示', {
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
            this.$axios.post(this.$apis.batchDeleteRole, postData).then(data => {
              this.batchDeleteLoading=false;
              if (data.data.success) {
                this.$message({
                  type: 'success',
                  message: '删除成功!'
                });
                this.pageNum = 1;
                this.findRoleAll();
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
            message: "请选择要删除的角色"
          })
        }
      }
    }
  }
</script>

<style type="text/scss" lang="scss" scoped>

</style>
