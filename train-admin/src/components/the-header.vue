<template>
  <a-layout-header class="header">
    <div class="logo" />
    <div style="float: right; color:white;">
      您好：{{member.mobile}}&nbsp;&nbsp;
      <router-link to="/login" style="color: white;">
        退出登录
      </router-link>
    </div>
    <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
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
  </a-layout-header>
</template>

<script>
import { defineComponent, ref, watch } from "vue";
import store from "@/store";
import router from "@/router";
import { UserOutlined } from "@ant-design/icons-vue";

export default defineComponent({
  name: "TheHeaderView",
  components: { UserOutlined },
  setup() {
    let member = store.state.member;
    const selectedKeys = ref([]);

    // 监听路由变化，更新 selectedKeys
    watch(
        () => router.currentRoute.value.path,
        (newValue) => {
          selectedKeys.value = [newValue];
        },
        {immediate: true}
    );

    // 处理菜单点击事件
    const handleMenuClick = ({key}) => {
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

<style scoped>
.logo {
  float: left;
  height: 31px;
  width: 150px;
  color: white;
  font-size: 20px;
}
</style>