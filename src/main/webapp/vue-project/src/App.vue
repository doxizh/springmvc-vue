<template>
  <div id="app">
      <router-view/>
  </div>
</template>

<script>
  import {mapMutations} from 'vuex'
  export default {
    name: 'App',
    created(){
      window.onbeforeunload = (e) => {
        this.saveStateToLocal();
      }
    },
    mounted(){

    },
    methods:{
      ...mapMutations([
        'setStateFromLocal',
        'saveStateToLocal'
      ])
    }

  }
</script>
<style scoped type="text/scss" lang="scss">
  #app {
    height: 100%;
  }
</style>
