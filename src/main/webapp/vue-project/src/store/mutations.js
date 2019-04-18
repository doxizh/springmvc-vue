const mutations = {
  setUserInfo(state,obj) {
    state.userInfo=obj;
  },
  saveStateToLocal(state){
    localStorage.setItem('springmvc-vue-state',JSON.stringify(state));
  },
  changeLoginStatus:(state,flag)=>{
    state.isLogin=flag;
    localStorage.setItem('springmvc-vue-state',JSON.stringify(state));
  }
};
export default mutations;
