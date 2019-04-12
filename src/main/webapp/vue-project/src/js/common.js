import Axios from 'axios';
import Qs from 'qs';
const proxy=process.env.NODE_ENV === 'production' ? '/front' : '/api';
const Common = {
  proxy: proxy,
  apis: {
    findUserByIdTest: proxy+'/findUserByIdTest',
    findUserAll: proxy+'/findUserAll',
    login: proxy+'/login',
    isLogin: proxy+'/isLogin',
    logout: proxy+'/logout',
    register: proxy+'/register',
    updatePassword: proxy+'/updatePassword',
    addUser: proxy+'/addUser',
    batchAddUser: proxy+'/batchAddUser',
    searchUserByName: proxy+'/searchUserByName',
    searchUser: proxy+'/searchUser',
    deleteUser: proxy+'/deleteUser',
    editUser: proxy+'/editUser',
    batchDeleteUser: proxy+'/batchDeleteUser',
    getRoles: proxy+'/findRoleAll',
  },
  axios: Axios,
  qs: Qs,
  async isLogin(){
    return await Axios.post(proxy+Common.apis.isLogin)
  }
};
export default Common;
