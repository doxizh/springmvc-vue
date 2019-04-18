<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>
  import {mapMutations} from 'vuex';

  export default {
    name: 'App',
    computed: {},
    data() {
      return {}
    },
    async created() {
      let paths = ['Login', 'Register'];
      let loginData = await this.$isLogin();
      if (loginData.data.success) {
        this.changeLoginStatus(true);
        this.getUserInfo();
        if(paths.indexOf(this.$route.name)>-1){
          this.$router.push({
            path: this.$route.query.redirect || '/home',
          });
        }
      }
    },
    mounted() {

    },
    methods: {
      ...mapMutations([
        "setUserInfo",
        "changeLoginStatus"
      ]),
      getUserInfo() {
        this.$axios.post(this.$apis.getUserInfo).then(data => {
          if (data.data.success) {
            this.setUserInfo(data.data.result);
          }
        })
      }
    }
  }
</script>
<style scoped type="text/scss" lang="scss">
  #app {
    height: 100%;
  }
</style>
