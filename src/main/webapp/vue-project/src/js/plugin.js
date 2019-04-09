import Common from './common';

export default function(Vue,options){
  Object.keys(Common).forEach(key=>{
    Vue.prototype['$'+key]=Common[key];
  })
}
