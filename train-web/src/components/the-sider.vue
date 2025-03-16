<script setup>

import {LaptopOutlined, NotificationOutlined, UserOutlined} from "@ant-design/icons-vue";
</script>
<!--      :openKeys="['batch','base']"  可以展开侧边，看到全部内容-->
<template>
  <a-layout-sider width="200" style="background: #fff">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        :openKeys="['batch','base','business']"
        mode="inline"
        style="height: 100%"
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
      <a-sub-menu key="batch">
        <template #title>
          <span>
            <UnorderedListOutlined/>
            会员管理
          </span>
        </template>
        <a-menu-item key="/member/ticket">
          <router-link to="/member/ticket">
            <user-outlined /> &nbsp;会员订单信息
          </router-link>
        </a-menu-item>
      </a-sub-menu>


      <a-sub-menu key="business">
        <template #title>
          <span>
            <UnorderedListOutlined/>
           业务管理
          </span>
        </template>
        <a-menu-item key="/business/sk-token">
          <router-link to="/business/sk-token">
            <user-outlined /> 令牌余量
          </router-link>
        </a-menu-item>

        <a-menu-item key="/business/confirm-order">
          <router-link to="/business/confirm-order">
            <user-outlined /> 订单信息
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-ticket">
          <router-link to="/business/daily-train-ticket">
            <user-outlined /> 车票信息
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train">
          <router-link to="/business/daily-train">
            <user-outlined /> 每日车次
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-station">
          <router-link to="/business/daily-train-station">
            <user-outlined /> 每日车站
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-carriage">
          <router-link to="/business/daily-train-carriage">
            <user-outlined /> 每日车厢
          </router-link>
        </a-menu-item>
        <a-menu-item key="/business/daily-train-seat">
          <router-link to="/business/daily-train-seat">
            <user-outlined /> 每日车座
          </router-link>
        </a-menu-item>
      </a-sub-menu>

      <a-sub-menu key="base">
        <template #title>
          <span>
            <UnorderedListOutlined/>
           基础数据
          </span>
        </template>
      <a-menu-item key="/base/station">
        <router-link to="/base/station">
          <user-outlined /> 车站管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/train">
        <router-link to="/base/train">
          <user-outlined /> 火车管理
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/train-station">
        <router-link to="/base/train-station">
          <user-outlined /> 火车车站
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/train-carriage">
        <router-link to="/base/train-carriage">
          <user-outlined /> 火车车厢
        </router-link>
      </a-menu-item>
      <a-menu-item key="/base/train-seat">
        <router-link to="/base/train-seat">
          <user-outlined /> 车厢座位
        </router-link>
      </a-menu-item>
      </a-sub-menu>

      <a-sub-menu key="batch">
        <template #title>
          <span>
            <UnorderedListOutlined/>
            跑批管理
          </span>
        </template>
        <a-menu-item key="/batch/job">
          <router-link to="/batch/job">
            <user-outlined /> &nbsp;任务管理
          </router-link>
        </a-menu-item>
      </a-sub-menu>

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