use train_business;
drop table if exists `member`;
create table `member`
(
    `id`     bigint not null comment 'id',
    `mobile` varchar(11) comment '手机号',
    primary key (`id`),
    unique key `mobile_unique` (`mobile`)
) engine=innodb default charset=utf8mb4 comment='会员';

drop table if exists `train`;
create table `train` (
                         `id` bigint not null comment 'id',
                         `code` varchar(20) not null comment '车次编号',
                         `type` char(1) not null comment '车次类型|枚举[TrainTypeEnum]',
                         `start` varchar(20) not null comment '始发站',
                         `start_pinyin` varchar(50) not null comment '始发站拼音',
                         `start_time` time not null comment '出发时间',
                         `end` varchar(20) not null comment '终点站',
                         `end_pinyin` varchar(50) not null comment '终点站拼音',
                         `end_time` time not null comment '到站时间',
                         `create_time` datetime(3) comment '新增时间',
                         `update_time` datetime(3) comment '修改时间',
                         primary key (`id`),
                         unique key `code_unique` (`code`)
) engine=innodb default charset=utf8mb4 comment='车次';
