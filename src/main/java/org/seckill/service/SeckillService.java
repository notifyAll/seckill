package org.seckill.service;

import org.seckill.dta.Exposer;
import org.seckill.dta.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在使用者角度设计接口
 * 三个方面：方法定义的力度，参数，返回类型 （return 类型/异常）
 * Created by notifyAll on 2017/7/12.
 */
public interface SeckillService {
    /**
     * 查询所有记录
     */
    List<Seckill> getSeckillList();

    /**
     * 通过id查询seckill 查询单个秒杀记录
     * @param seckillId
     * @return
     */
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时 输出秒杀接口的地址，否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 秒杀执行操作
     * @param seckillId
     * @param userPhone
     * @param md5 加密措施
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,RepeatKillException,SeckillCloseException;
}
