package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by notifyAll on 2017/7/11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;
    @Test
    public void insertSuccessKilled() throws Exception {
        int count=successKilledDao.insertSuccessKilled(1000L,13456006963L);
        if (count==0)
            System.out.println("主键冲突 插入失败");
        else
        System.out.println(count);
    }

    @Test
    public void queryByIdWidthSeckill() throws Exception {
        SuccessKilled successKilled=successKilledDao.queryByIdWidthSeckill(1000,13456006963L);

        System.out.println(successKilled.getSeckillId());
        System.out.println(successKilled.getSeckill().getName());
    }

}