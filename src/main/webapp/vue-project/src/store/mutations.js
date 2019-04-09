import types from './types';

const mutations = {
  addNum: state => {
    state.num++;
  },
  [types.SAVE_USER_INFO](state,obj) {
    state.userInfo=obj;
  },
  saveStateToLocal(state){
    localStorage.setItem('element-project-state',JSON.stringify(state));
  },
  setStateFromLocal:(state)=>{
    let localState=localStorage.getItem('element-project-state');
    if(localState){
      localState=JSON.parse(localState);
      Object.keys(localState).forEach(key=>{
        state[key]=localState[key];
      });
    }
  },
  changeLoginStatus:(state,flag)=>{
    state.isLogin=flag;
  }
};
export default mutations;
