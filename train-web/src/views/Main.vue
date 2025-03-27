<!--<template>-->
<!--  <a-layout>-->
<!-- <the-header-view></the-header-view>-->
<!--    <a-layout-content style="padding: 0 50px">-->
<!--      <a-breadcrumb style="margin: 16px 0">-->
<!--        <a-breadcrumb-item>Home</a-breadcrumb-item>-->
<!--        <a-breadcrumb-item>List</a-breadcrumb-item>-->
<!--        <a-breadcrumb-item>App</a-breadcrumb-item>-->
<!--      </a-breadcrumb>-->
<!--      <a-layout style="padding: 24px 0; background: #fff">-->
<!--    <the-sider-view></the-sider-view>-->
<!--        <a-layout-content :style="{ padding: '0 24px', minHeight: '280px' }">-->
<!--          所有会员总数：{{count}}-->
<!--        </a-layout-content>-->
<!--      </a-layout>-->
<!--    </a-layout-content>-->
<!--    <a-layout-footer style="text-align: center">-->
<!--      Ant Design ©2018 Created by Ant UED-->
<!--    </a-layout-footer>-->
<!--  </a-layout>-->
<!--</template>-->
<template>
  <a-layout id="components-layout-demo-top-side-2">
    <the-header-view></the-header-view>
    <a-layout>
      <the-sider-view></the-sider-view>
      <a-layout-content
          :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
      >
        <router-view></router-view>
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script>

import { defineComponent, ref } from 'vue';
import TheHeaderView from "@/components/the-header.vue";
import TheSiderView from "@/components/the-sider.vue";

import store from "@/store";
export default defineComponent({
  components: {
    TheSiderView,
    TheHeaderView,

  },
  setup() {
    return {
    };
  },
});
</script>
<style>
</style>