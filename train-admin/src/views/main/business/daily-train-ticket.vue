<template>
  <p>
    <a-space>
      <train-select-view v-model="params.trainCode" width="200px"/>
      <a-date-picker v-model:value="params.date" value-format="YYYY-MM-DD" placeholder=""/>
      <station-select-view v-model="params.start" width="200px"/>
      <station-select-view v-model="params.end" width="200px"/>
      <a-button type="primary" @click="handleQuery()">查找</a-button>
    </a-space>
  </p>
  <a-table :dataSource="dailyTrainTickets"
           :columns="columns"
           :pagination="pagination"
           @change="handleTableChange"
           :loading="loading">
    <template #bodyCell="{ column, record }">
      <template v-if="column.dataIndex === 'operation'">
      </template>
      <template v-else-if="column.dataIndex==='station'">
        {{record.start}}<br/>
        {{record.end}}
      </template>
      <template v-else-if="column.dataIndex==='time'">
        {{record.startTime}}<br/>
        {{record.endTime}}
      </template>
      <template v-else-if="column.dataIndex === 'duration'">
        {{calDuration(record.startTime, record.endTime)}}<br/>
        <div v-if="record.startTime.replaceAll(':', '') >= record.endTime.replaceAll(':', '')">
          次日到达
        </div>
        <div v-else>
          当日到达
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'ydz'">
        <div v-if="record.ydz >= 0">
          {{record.ydz}}<br/>
          {{record.ydzPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'edz'">
        <div v-if="record.edz >= 0">
          {{record.edz}}<br/>
          {{record.edzPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'rw'">
        <div v-if="record.rw >= 0">
          {{record.rw}}<br/>
          {{record.rwPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
      <template v-else-if="column.dataIndex === 'yw'">
        <div v-if="record.yw >= 0">
          {{record.yw}}<br/>
          {{record.ywPrice}}￥
        </div>
        <div v-else>
          --
        </div>
      </template>
    </template>
  </a-table>
<!--  <a-modal v-model:visible="visible" title="余票信息" @ok="handleOk"-->
<!--           ok-text="确认" cancel-text="取消">-->
<!--    <a-form :model="dailyTrainTicket" :label-col="{span: 4}" :wrapper-col="{ span: 20 }">-->
<!--      <a-form-item label="日期">-->
<!--        <a-date-picker v-model:value="dailyTrainTicket.date" valueFormat="YYYY-MM-DD" placeholder="请选择日期" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="车次编号">-->
<!--        <a-input v-model:value="dailyTrainTicket.trainCode" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="出发站">-->
<!--        <a-input v-model:value="dailyTrainTicket.start" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="出发站拼音">-->
<!--        <a-input v-model:value="dailyTrainTicket.startPinyin" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="出发时间">-->
<!--        <a-time-picker v-model:value="dailyTrainTicket.startTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="出发站序">-->
<!--        <a-input v-model:value="dailyTrainTicket.startIndex" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="到达站">-->
<!--        <a-input v-model:value="dailyTrainTicket.end" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="到达站拼音">-->
<!--        <a-input v-model:value="dailyTrainTicket.endPinyin" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="到站时间">-->
<!--        <a-time-picker v-model:value="dailyTrainTicket.endTime" valueFormat="HH:mm:ss" placeholder="请选择时间" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="到站站序">-->
<!--        <a-input v-model:value="dailyTrainTicket.endIndex" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="一等座余票">-->
<!--        <a-input v-model:value="dailyTrainTicket.ydz" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="一等座票价">-->
<!--        <a-input v-model:value="dailyTrainTicket.ydzPrice" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="二等座余票">-->
<!--        <a-input v-model:value="dailyTrainTicket.edz" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="二等座票价">-->
<!--        <a-input v-model:value="dailyTrainTicket.edzPrice" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="软卧余票">-->
<!--        <a-input v-model:value="dailyTrainTicket.rw" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="软卧票价">-->
<!--        <a-input v-model:value="dailyTrainTicket.rwPrice" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="硬卧余票">-->
<!--        <a-input v-model:value="dailyTrainTicket.yw" />-->
<!--      </a-form-item>-->
<!--      <a-form-item label="硬卧票价">-->
<!--        <a-input v-model:value="dailyTrainTicket.ywPrice" />-->
<!--      </a-form-item>-->
<!--    </a-form>-->
<!--  </a-modal>-->
</template>

<script>
import { defineComponent, ref, onMounted } from 'vue';
import {notification} from "ant-design-vue";
import axios from "axios";
import TrainSelectView from "@/components/train-select.vue";
import StationSelectView from "@/components/station-select.vue";
import dayjs from "dayjs";

export default defineComponent({
  name: "daily-train-ticket-view",
  components: {StationSelectView, TrainSelectView},
  setup() {
    const visible = ref(false);
    let dailyTrainTicket = ref({
      id: undefined,
      date: undefined,
      trainCode: undefined,
      start: undefined,
      startPinyin: undefined,
      startTime: undefined,
      startIndex: undefined,
      end: undefined,
      endPinyin: undefined,
      endTime: undefined,
      endIndex: undefined,
      ydz: undefined,
      ydzPrice: undefined,
      edz: undefined,
      edzPrice: undefined,
      rw: undefined,
      rwPrice: undefined,
      yw: undefined,
      ywPrice: undefined,
      createTime: undefined,
      updateTime: undefined,
    });
    const dailyTrainTickets = ref([]);
    // 分页的三个属性名是固定的
    const pagination = ref({
      total: 0,
      current: 1,
      pageSize: 10,
    });
    let loading = ref(false);
    let params =ref({});
    const columns = [
    {
      title: '日期',
      dataIndex: 'date',
      key: 'date',
    },
    {
      title: '车次编号',
      dataIndex: 'trainCode',
      key: 'trainCode',
    },
      {
        title: '车站',
        dataIndex: 'station',
      },
      {
        title: '时间',
        dataIndex: 'time',
      },
      {
        title: '历时',
        dataIndex: 'duration',
      },
    // {
    //   title: '出发站',
    //   dataIndex: 'start',
    //   key: 'start',
    // },
    // {
    //   title: '出发站拼音',
    //   dataIndex: 'startPinyin',
    //   key: 'startPinyin',
    // },
    // {
    //   title: '出发时间',
    //   dataIndex: 'startTime',
    //   key: 'startTime',
    // },
    // {
    //   title: '出发站序',
    //   dataIndex: 'startIndex',
    //   key: 'startIndex',
    // },
    // {
    //   title: '到达站',
    //   dataIndex: 'end',
    //   key: 'end',
    // },
    // {
    //   title: '到达站拼音',
    //   dataIndex: 'endPinyin',
    //   key: 'endPinyin',
    // },
    // {
    //   title: '到站时间',
    //   dataIndex: 'endTime',
    //   key: 'endTime',
    // },
    // {
    //   title: '到站站序',
    //   dataIndex: 'endIndex',
    //   key: 'endIndex',
    // },
     {
      title: '一等座',
      dataIndex: 'ydz',
      key: 'ydz',
    },
    // {
    //   title: '一等座票价',
    //   dataIndex: 'ydzPrice',
    //   key: 'ydzPrice',
    // },
    {
      title: '二等座',
      dataIndex: 'edz',
      key: 'edz',
    },
    // {
    //   title: '二等座票价',
    //   dataIndex: 'edzPrice',
    //   key: 'edzPrice',
    // },
    {
      title: '软卧',
      dataIndex: 'rw',
      key: 'rw',
    },
    // {
    //   title: '软卧票价',
    //   dataIndex: 'rwPrice',
    //   key: 'rwPrice',
    // },
    {
      title: '硬卧',
      dataIndex: 'yw',
      key: 'yw',
    },
    // {
    //   title: '硬卧票价',
    //   dataIndex: 'ywPrice',
    //   key: 'ywPrice',
    // },
    ];

    const onAdd = () => {
      dailyTrainTicket.value = {};
      visible.value = true;
    };

    const onEdit = (record) => {
      dailyTrainTicket.value = window.Tool.copy(record);
      visible.value = true;
    };

    const onDelete = (record) => {
      axios.delete("/business/admin/dailyTrainTicket/delete/" + record.id).then((response) => {
        const data = response.data;
        if (data.code == 200) {
          notification.success({description: "删除成功！"});
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleOk = () => {
      axios.post("/business/admin/dailyTrainTicket/save", dailyTrainTicket.value).then((response) => {
        let data = response.data;
        if (data.code == 200) {
          notification.success({description: "保存成功！"});
          visible.value = false;
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          });
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleQuery = (param) => {
      if (!param) {
        param = {
          page: 1,
          size: pagination.value.pageSize
        };
      }
      loading.value = true;
      axios.get("/business/admin/dailyTrainTicket/query-list", {
        params: {
          page: param.page,
          size: param.size,
          trainCode:params.value.trainCode,
          date:params.value.date,
          start:params.value.start,
          end:params.value.end

        }
      }).then((response) => {
        loading.value = false;
        let data = response.data;
        if (data.code == 200) {
          dailyTrainTickets.value = data.data.list;
          // 设置分页控件的值
          pagination.value.current = param.page;
          pagination.value.total = data.data.total;
        } else {
          notification.error({description: data.message});
        }
      });
    };

    const handleTableChange = (page) => {
      // console.log("看看自带的分页参数都有啥：" + JSON.stringify(page));
      pagination.value.pageSize = page.pageSize;
      handleQuery({
        page: page.current,
        size: page.pageSize
      });
    };
    const calDuration = (startTime, endTime) => {
      let diff = dayjs(endTime, 'HH:mm:ss').diff(dayjs(startTime, 'HH:mm:ss'), 'seconds');
      return dayjs('00:00:00', 'HH:mm:ss').second(diff).format('HH:mm:ss');
    };


    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return {
      dailyTrainTicket,
      visible,
      dailyTrainTickets,
      pagination,
      columns,
      handleTableChange,
      handleQuery,
      loading,
      onAdd,
      handleOk,
      onEdit,
      onDelete,
      params,
      calDuration
    };
  },
});
</script>
