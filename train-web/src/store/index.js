import { createStore } from 'vuex'
//保存在前端需要的全局变量
//state:定义
//mutations:相当于set方法
//modules:相当于模块。可以将上面的几步封装一个整体，然后在modules中可以有多个整体


const MEMBER = "MEMBER";

export default createStore({
  state: {
    member: window.SessionStorage.get(MEMBER) || {}
  },
  getters: {
  },
  mutations: {
    setMember (state, _member) {
      state.member = _member;
      window.SessionStorage.set(MEMBER, _member);
    }
  },
  actions: {
  },
  modules: {
  }
})
