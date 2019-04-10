<template>
  <div class="home">
    <el-container class="container">
      <el-header>
        <header-top></header-top>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <aside-menu></aside-menu>
        </el-aside>
        <el-container>
          <el-main>
            <router-view/>
          </el-main>
          <el-footer>Footer</el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
  import HeaderTop from '@/components/HeaderTop'
  import AsideMenu from '@/components/AsideMenu'
  export default {
    name: "home",
    data() {
      return {}
    },
    components:{
      HeaderTop,
      AsideMenu
    },
    created() {

    },
    methods:{

    }
  }
</script>

<style scoped type="text/scss" lang="scss">
  .home{
    height: 100%;
  }
  .container{
    height: 100%;
  }
  .el-header{
    padding: 0;
  }
  .el-aside{
    background: #545c64;
  }
  .el-main{
    padding: 0;
    background-color: #f8f9fc;
    display: flex;
  }
</style>
