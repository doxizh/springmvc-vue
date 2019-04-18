import Vue from 'vue'
import Router from 'vue-router'
import store from '../store'
import Home from '@/pages/home'
import Login from '@/pages/login'
import Register from '@/pages/register'
import User from '@/pages/user'
import Role from '@/pages/role'

Vue.use(Router)
const router = new Router({
  routes: [
    {
      path: '/',
      redirect:{
        name:'Home'
      }
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      children: [
        {
          path: 'user',
          name: 'User',
          component: User,
        },
        {
          path: 'role',
          name: 'Role',
          component: Role,
        }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/register',
      name: 'Register',
      component: Register
    },
  ]
});
router.beforeEach((to, from, next) => {
  let paths1 = ['Login', 'Register'];
  let isLogin = store.state.isLogin;
  if (paths1.indexOf(to.name) > -1) {
    if (isLogin) {
      router.push({
        path: to.query.redirect||'/home',
      });
      return;
    }
  }else{
    if (!isLogin&&from.name!==null) {
      router.replace({
        path: '/login',
        query: {redirect: to.fullPath}
      });
      return;
    }
  }
  next();
});
export default router;
