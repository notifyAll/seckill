-- 数据库初始化脚步

-- 创建数据库
CREATE  DATABASE seckill;

-- use
USE  seckill;

-- 建表
CREATE TABLE seckill(
 seckill_id2 222222222222222222222222222222222222222222222222222222222 BIGINT NOT NULL AUTO_INCREMENT COMMENT  '商品库存表',
  name VARCHAR(120) NOT NULL COMMENT '商品名称',
   number int NOT NULL COMMENT '库存数量',
  create_time TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  start_time TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
  end_time TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET =utf8 COMMENT ='秒杀库存表';

-- 初始化数据
INSERT INTO seckill(name, number, start_time, end_time)
VALUES
  ('1000秒杀iPhone6',10,'2017-7-9 00:00:00','2017-7-9 00:01:00'),
  ('800秒杀iPhone5s',20,'2017-7-8 10:10:00','2017-7-8 10:10:10'),
  ('600秒杀iPhone5',30,'2017-7-8 06:00:00','2017-7-8 06:01:00'),
  ('400秒杀iPhone4s',40,'2017-7-8 11:00:00','2017-7-8 11:01:00');

-- 秒杀成功表
CREATE TABLE success_killed(
  seckill_id BIGINT NOT NULL  COMMENT '秒杀商品id',
  user_phone BIGINT NOT NULL COMMENT '用户手机号',
  state TINYINT NOT NULL DEFAULT -1 COMMENT '标识状态 -1 无效 0成功 1已付款 2 已发货 ',
  create_time TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id,user_phone),
   KEY idx_create_time(create_time)
)ENGINE =InnoDB DEFAULT CHARSET =utf8 COMMENT ='秒杀成功明细';