<template>
  <div style="border: 4px solid orange;
              border-radius: 4px;
              padding: 10px; ">
    <h3>
      <b>项目简要说明</b>
    </h3>
    本项目共有15张业务表，自制通用代码生成器，快速生成增删改查包含界面，减少无意义的增删改查占用开发时间。<br>
    购票流程看起来简单，但用了很多看不见的高并发技术，比如10万人抢1000张票：<br>
    <li>利用<b>CDN</b>，提高用户访问页面速度</li>
    <li>利用<b>分布式缓存</b>，在秒杀开始前，提供高性能余票查询，同时要考虑缓存击穿、穿透、雪崩等问题</li>
    <li>使用<b>第一层验证码</b>，纯前端验证码在前端削弱瞬时高峰，将100毫秒内10万人的请求，分散成1~2秒内10万人请求</li>
    <li>使用<b>第二层验证码</b>，后端验证码，进一步分散请求，同时防止机器人抢票</li>
    <li>使用<b>限流技术</b>减轻无谓请求，同时给用户快速失败响应（告知票没有了），将9万请求快速失败，变成1万请求抢1000张票</li>
    <li>使用<b>令牌发放技术</b>，控制抢票量，同时防止机器人刷票，比如开放2000令牌，即变成8000请求快速失败，变成2000请求抢1000张票</li>
    <li>使用<b>分布式锁技术</b>，防止超卖，即2000人抢1000张票，最终只能卖出1000张，不能卖出1001张票</li>
    <li>使用<b>异步削峰+排队机制</b>，解决吞吐量问题，实现最短时间内给用户反馈，1000请求告知票没有了，1000请求告知排队中</li>
    <li>使用<b>分布式事务</b>，保证数据最终一致性，不能库存减少了，票却没打出来。</li>
  </div>
  <a-divider style="border-color: #7cb305" dashed />
  <div style="border: 4px solid #99CCFF;
              border-radius: 4px;
              padding: 10px; ">
    <h3>
      <b>学习于慕课甲蛙老师仿12306高性能微服务项目</b>
    </h3>
    <div>
      如果您还没有乘车人，可以先去
      <router-link to="/passenger">
        添加乘车人
      </router-link>
    </div>
    <div>
      购票功能支持选座，去
      <router-link to="/ticket">
        查询余票并购票
      </router-link>
      。购票成功后，可以查看
      <router-link to="/my-ticket">
        我的车票
      </router-link>
      ；去
      <router-link to="/seat">
        座位销售图
      </router-link>
      查看每个座位的销售情况
    </div>
    <div style="border: 4px solid orange;
              border-radius: 4px;
              padding: 10px; ">
      <h3>
        <b>本项目来源于
          <a href="https://coding.imooc.com/class/641.html" target="_blank">
            《Springboot3+微服务实战12306高性能售票系统》
          </a>
        </b>
      </h3>
      </div>

  </div>

  <a-divider style="border-color: #7cb305" dashed />

</template>

<script>

import { defineComponent } from 'vue';

export default defineComponent({
  name: "welcome-view",
  setup() {
    return {
    };
  },
});
</script>
