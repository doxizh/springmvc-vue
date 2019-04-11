<template>
  <div class="aside-menu">
    <el-menu
      router
      :default-active="activeIndex"
      class="el-menu-vertical-demo"
      @open="handleOpen"
      @close="handleClose"
      background-color="transparent"
      text-color="#fff"
      active-text-color="#ffd04b">
      <el-menu-item index="/home">
          <i class="fa fa-home"></i>
          <span>首页</span>
      </el-menu-item>
      <el-menu-item index="/home/user">
        <i class="fa fa-users"></i>
        <span slot="title">用户管理</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
  export default {
    name: "AsideMenu",
    data() {
      return {
        activeIndex: this.$route.path,
      }
    },
    created(){
      console.log(this.$route.path);
      if(this.$route.path==='/'){
        this.activeIndex='/home';
      }
    },
    methods: {
      handleOpen(key, keyPath) {
        console.log(key, keyPath);
      },
      handleClose(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>

<style scoped type="text/scss" lang="scss">
  .el-menu{
    border-right: none;
    .fa{
      margin-right: 5px;
      width: 24px;
      text-align: center;
      font-size: 18px;
      vertical-align: middle;
    }
  }
</style>
