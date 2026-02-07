-- auto-generated definition
create table t_alert_record
(
    id           bigint auto_increment comment '主键ID'
        primary key,
    worker_id    bigint                                            not null comment '关联工人ID',
    alert_type   enum ('疲劳', '心率异常', 'SDNN异常', '综合高危') not null comment '预警类型',
    alert_level  enum ('警告', '严重')                             not null comment '预警级别',
    message      text                                              not null comment '预警消息内容',
    is_handled   tinyint(1) default 0                              null comment '是否已处理',
    handled_by   varchar(100)                                      null comment '处理人',
    handle_time  datetime                                          null comment '处理时间',
    created_time datetime   default CURRENT_TIMESTAMP              not null,
    constraint t_alert_record_ibfk_1
        foreign key (worker_id) references t_worker (id)
            on delete cascade
)
    comment '预警记录表' charset = utf8mb4;

create index idx_alert_type
    on t_alert_record (alert_type);

create index idx_created_time
    on t_alert_record (created_time);

create index idx_is_handled
    on t_alert_record (is_handled);

create index idx_worker_id
    on t_alert_record (worker_id);

-- auto-generated definition
create table t_risk_history
(
    id              bigint auto_increment comment '主键ID'
        primary key,
    worker_id       bigint                                  not null comment '关联工人ID',
    heart_rate      int           default 0                 null comment '心率',
    sdnn_ms         decimal(5, 2) default 0.00              null comment 'SDNN',
    fatigue_percent decimal(5, 2) default 0.00              null comment '疲劳度%',
    breath_rate     int           default 0                 null comment '呼吸频率',
    au_score        int           default 0                 null comment '面部AU评分（微表情）',
    stress_level    int           default 0                 null comment '压力水平（0~100）',
    record_time     datetime      default CURRENT_TIMESTAMP not null comment '记录时间',
    constraint t_risk_history_ibfk_1
        foreign key (worker_id) references t_worker (id)
            on delete cascade
)
    comment '历史风险数据表' charset = utf8mb4;

create index idx_fatigue_percent
    on t_risk_history (fatigue_percent);

create index idx_record_time
    on t_risk_history (record_time);

create index idx_worker_id
    on t_risk_history (worker_id);

-- auto-generated definition
create table t_risk_indicator
(
    id              bigint auto_increment comment '主键ID'
        primary key,
    worker_id       bigint                                                                    not null comment '关联工人ID',
    heart_rate      int                                             default 0                 null comment '心率 (bpm)',
    sdnn_ms         decimal(5, 2)                                   default 0.00              null comment '心率变异性 SDNN (ms)',
    fatigue_percent decimal(5, 2)                                   default 0.00              null comment '疲劳百分比 (%)',
    risk_level      enum ('低风险', '中风险', '高风险', '严重风险') default '低风险'          null comment '当前风险等级',
    alert_flag      tinyint(1)                                      default 0                 null comment '是否触发报警',
    record_time     datetime                                        default CURRENT_TIMESTAMP not null comment '记录时间',
    constraint t_risk_indicator_ibfk_1
        foreign key (worker_id) references t_worker (id)
            on delete cascade
)
    comment '实时风险指标表' charset = utf8mb4;

create index idx_alert_flag
    on t_risk_indicator (alert_flag);

create index idx_record_time
    on t_risk_indicator (record_time);

create index idx_risk_level
    on t_risk_indicator (risk_level);

create index idx_worker_id
    on t_risk_indicator (worker_id);

-- auto-generated definition
create table t_work_area
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    area_code   varchar(50)                                                   not null comment '区域编码',
    area_name   varchar(100)                                                  not null comment '区域名称',
    risk_level  enum ('低风险', '中风险', '高风险') default '低风险'          null comment '区域风险等级',
    description text                                                          null comment '描述',
    create_time timestamp                           default CURRENT_TIMESTAMP null,
    update_time timestamp                           default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint area_code
        unique (area_code)
)
    comment '工作区域表' charset = utf8mb4;

create index idx_area_code
    on t_work_area (area_code);

create index idx_area_name
    on t_work_area (area_name);

-- auto-generated definition
create table t_worker
(
    id          bigint auto_increment comment '主键ID'
        primary key,
    worker_code varchar(50)                                             not null comment '工号',
    name        varchar(50)                                             not null comment '姓名',
    position    varchar(50)                                             not null comment '岗位',
    work_years  int                           default 0                 null comment '工龄（年）',
    work_type   enum ('高空作业', '受限空间', '设备操作', '正常作业')   not null comment '工作类型',
    status      enum ('正常', '异常', '离线') default '正常'            not null comment '当前状态',
    create_time timestamp                     default CURRENT_TIMESTAMP null,
    update_time timestamp                     default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    constraint worker_code
        unique (worker_code)
)
    comment '工人基本信息表' charset = utf8mb4;

create index idx_name
    on t_worker (name);

create index idx_position
    on t_worker (position);

create index idx_worker_code
    on t_worker (worker_code);

