<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        style="height: 100%"
        @click="handleMenuClick"
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

<script>
import { defineComponent, ref, watch } from "vue";
import router from "@/router";
import store from "@/store";

export default defineComponent({
  name: "TheSiderView",
  setup() {
    const selectedKeys = ref([]);
    let member = store.state.member;

    // 监听路由变化，更新 selectedKeys
    watch(
        () => router.currentRoute.value.path,
        (newValue) => {
          selectedKeys.value = [newValue];
        },
        { immediate: true }
    );

    // 处理菜单点击事件
    const handleMenuClick = ({ key }) => {
      router.push(key);
    };

    return {
      selectedKeys,
      member,
      handleMenuClick,
    };
  },
});
</script>