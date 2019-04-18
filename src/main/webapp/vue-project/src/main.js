// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import './css/style.scss'
Vue.use(ElementUI);
import Plugin from './js/plugin'
Vue.use(Plugin);
import store from './store'
import axios from 'axios'
import 'font-awesome/css/font-awesome.min.css'
// http request 拦截器
axios.interceptors.request.use(
  config => {
    if (store.state.token) {  // 判断是否存在token，如果存在的话，则每个http header都加上token
      config.headers.Authorization = `token ${store.state.token}`;
    }
    return config;
  },
  err => {
    return Promise.reject(err);
  });

// http response 拦截器
axios.interceptors.response.use(
  response => {
    if(response.data.code==="401"){
      store.commit('changeLoginStatus',false);
      let paths = ['Login', 'Register'];
      if(paths.indexOf(router.currentRoute.name)===-1){
        router.replace({
          path: '/login',
          query: {redirect: router.currentRoute.fullPath}
        })
      }
    }
    return response;
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // 返回 401 清除token信息并跳转到登录页面
          store.commit('changeLoginStatus',false);
          let paths = ['Login', 'Register'];
          if(paths.indexOf(router.currentRoute.name)===-1){
            router.replace({
              path: '/login',
              query: {redirect: router.currentRoute.fullPath}
            })
          }
      }
    }
    return Promise.reject(error.response.data)   // 返回接口返回的错误信息
  }
);
/*if(!window.isUnload){
  window.isUnload=true;
  store.commit('saveStateToLocal');
}*/
Vue.config.productionTip = false;

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: {App},
  template: '<App/>'
});
