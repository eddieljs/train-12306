<template xmlns:color="http://www.w3.org/1999/xhtml">
  <a-layout-header class="header">
    <div class="logo">
      <router-link to="/welcome" style="color: white; font-size: 18px">
        宫阙12306控台
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined /> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">
          <user-outlined /> &nbsp; 关于
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-header>
</template>

<script socpe>
import {defineComponent, ref,watch} from "vue";

import router from "@/router";

export  default  defineComponent({
  name:"the-header-view",
  setup(){

    const selectedKeys=ref([]);
    watch(()=>
      router.currentRoute.value.path,(newValue)=>{
      console.log('watch',newValue);
      selectedKeys.value=[];
      selectedKeys.value.push(newValue);
    },{immediate:true});
    return{
      selectedKeys,
    }

  }
})
</script>

<style scoped>
.logo {
  float: left;
  height: 31px;
  width: 150px;
  color: white;
  font-size: 20px;
}
</style>