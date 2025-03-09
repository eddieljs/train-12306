<script setup>

import {LaptopOutlined, NotificationOutlined, UserOutlined} from "@ant-design/icons-vue";
</script>

<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        v-model:openKeys="openKeys"
        mode="inline"
        style="height: 100%"
    >
      <a-menu-item key="/welcome">
        <router-link to="/welcome">
          <coffee-outlined /> &nbsp; 欢迎
        </router-link>
      </a-menu-item>
      <a-menu-item key="/passenger">
        <router-link to="/passenger">
          <user-outlined /> &nbsp; 乘车人管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/ticket">
        <router-link to="/ticket">
          <user-outlined /> &nbsp; 车票信息
        </router-link>
      </a-menu-item>
      <a-menu-item key="/my-ticket">
        <router-link to="/my-ticket">
          <user-outlined /> &nbsp; 我的车票
        </router-link>
      </a-menu-item>
      <a-menu-item key="/seat">
        <router-link to="/seat">
          <user-outlined /> &nbsp; 座位销售图
        </router-link>
      </a-menu-item>
    </a-menu>
  </a-layout-sider>
</template>

<script socpe>
import {defineComponent, ref,watch} from "vue";
import router from "@/router";

export  default  defineComponent({
  name:"the-sider-view",
  setup(){
    const selectedKeys = ref([]);

    watch(() => router.currentRoute.value.path, (newValue) => {
      console.log('watch', newValue);
      selectedKeys.value = [];
      selectedKeys.value.push(newValue);
    }, {immediate: true});
    return{
      selectedKeys,
      openKeys: ref(['sub1']),
    }

  }
})
</script>