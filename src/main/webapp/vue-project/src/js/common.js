import Axios from 'axios';
import Qs from 'qs';
const Common = {
  proxy: process.env.NODE_ENV === 'production' ? '/front' : '/api',
  apis: {
    findUserByIdTest: '/findUserByIdTest',
    findUserAll: '/findUserAll',
    login: '/login',
    isLogin: '/isLogin',
    logout: '/logout',
    register: '/register',
    updatePassword: '/updatePassword',
    addUser: '/addUser',
    batchAddUser: '/batchAddUser',
    searchUserByName: '/searchUserByName',
    searchUser: '/searchUser',
    deleteUser: '/deleteUser',
    editUser: '/editUser',
    batchDeleteUser: '/batchDeleteUser',
  },
  axios: Axios,
  qs: Qs,
  async isLogin(){
    return await Axios.post(Common.proxy+Common.apis.isLogin)
  }
};
export default Common;
