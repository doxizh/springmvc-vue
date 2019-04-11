import types from './types';

const mutations = {
  [types.SAVE_USER_INFO](state,obj) {
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
