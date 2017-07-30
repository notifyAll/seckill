package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by notifyAll on 2017/7/8.
 */
public interface SuccessKilledDao {
    /**
     * 添加一条购买明细 可过滤重复
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);

    /**
     * 查询 successkilled 并携带秒杀产品对象实体
     * @param seckillId
     * @param userPhone
     * @return
     */
    SuccessKilled queryByIdWidthSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
