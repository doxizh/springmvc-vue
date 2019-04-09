let elementProjectState=localStorage.getItem('element-project-state');
if(elementProjectState){
  elementProjectState=JSON.parse(elementProjectState);
}
const state=elementProjectState||{
  num:0,
  userInfo:{},
  isLogin:false
};
export default state;
