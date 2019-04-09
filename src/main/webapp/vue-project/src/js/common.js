import Axios from 'axios';
const Common = {
  proxy: process.env.NODE_ENV === 'production' ? '' : '/api',
  apis: {
    findUserByIdTest: '/findUserByIdTest',
    findUserAll: '/findUserAll',
    login: '/login',
    isLogin: '/isLogin',
    logout: '/logout',
    register: '/register',
    updatePassword: '/updatePassword',
  },
  axios: Axios,
  async isLogin(){
    return await Axios.post(Common.proxy+Common.apis.isLogin)
  }
};
export default Common;
