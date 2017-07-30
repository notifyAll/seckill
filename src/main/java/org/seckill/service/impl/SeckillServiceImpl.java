package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dta.Exposer;
import org.seckill.dta.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by notifyAll on 2017/7/13.
 */
@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger= LoggerFactory.getLogger(this.getClass());
//注入service依赖
//    @Resource
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
//md5 盐值字符串，用于混淆MD5;  越复杂越好
    private final String salt ="kasjcijakw$as&";
    public List<Seckill> getSeckillList() {

        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {

        return seckillDao.querryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill=seckillDao.querryById(seckillId);
//        没有这个商品则不能秒杀
        if (seckill==null){
            return new Exposer(false,seckillId);
        }
//        如果有这条记录 先判断时间 能不能秒杀
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
//        系统当前时间
        Date nowTime=new Date();
        if (nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
//        转化特定字符转的过程，不可逆
        String md5=getMD5(seckillId);//TODO
        return new Exposer(true,md5,seckillId);
    }

    /**
     * 用于生成md5 这玩意不可逆
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId){
//        自定义了一个拼接规则 salt 啥子盐值像密码一样
        String base=seckillId+"/"+ salt;
//        用工具类 生成md5
        String md5= DigestUtils.md5DigestAsHex(base.getBytes());

        return md5;
    }

    /**
     * 秒杀执行操作
     * 使用注解控制事物方法的优点
     * 1 开发团队达成一致约定，明确标注事务方法的编程风格
     * 2 保证事物方法的执行时间尽可能短，不要穿插其他网络操作rpg、http请求或者剥离到事物方法外部
     *3 不是所有的方法都需要事物，如只有一条修改操作，只读操作不需要事物控制
     * @param seckillId
     * @param userPhone
     * @param md5 加密措施
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5==null||!md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite 伪造数据");
        }
        try {
            //        执行秒杀逻辑：减库存 + 购买记录

            Date nowTime=new Date();
            int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
            if (updateCount<=0){
//            没有更新到记录 ：关闭 秒杀结束
                throw new SeckillCloseException("seckill is closed");
            }else {
//            记录购买明细
                int insertCount= successKilledDao.insertSuccessKilled(seckillId,userPhone);
                if (insertCount<=0){
//                重复购买
                    throw new RepeatKillException("seckill repeated");
                }else {
//                秒杀成功
                    SuccessKilled successKilled=successKilledDao.queryByIdWidthSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatKillException e2){
            throw e2;
        }
        catch (Exception e){
            logger.error(e.getMessage(),e);
//            所有编译期异常 转化为运行时异常
            throw new SeckillException("seckill inner error:"+e.getMessage());
        }
    }
}
