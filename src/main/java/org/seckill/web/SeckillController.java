package org.seckill.web;

import org.seckill.dta.Exposer;
import org.seckill.dta.SeckillExecution;
import org.seckill.dta.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by notifyAll on 2017/7/18.
 */
//1标注类型
@Controller //
//2
//模块 url：/模块/资源/{id}/细分  如 /seckill/list
@RequestMapping("/seckill")
public class SeckillController {
    //    日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //    注入事物层
    @Resource
    private SeckillService seckillService;
//    二级   method = RequestMethod.GET 请求方式 必须要get

    /**
     * 查询列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
// list.jsp + model= ModelAndView

//        获取列表页
        List<Seckill> seckillList = seckillService.getSeckillList();
        model.addAttribute("list", seckillList);
        return "list"; //由于 spring-web.xml里配置了 所以这里返回的意思指/WEB-INF/jsp/list.jsp
    }


    @RequestMapping(value = "/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model) {
//       传空id
        if (seckillId == null) {
            return "redirect:/seckill/list"; //请求重定向 去上面那个方法
        }
        Seckill byIdSeckill = seckillService.getById(seckillId);
//       秒杀对象不存在
        if (byIdSeckill == null) {
            return "forward:/seckill/list";//请求转发
        }
        model.addAttribute("seckill", byIdSeckill);
        return "detail";
    }


    /**
     * 秒杀开启时 输出秒杀接口的地址，否则输出系统时间和秒杀时间
     *
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer"
            , method = RequestMethod.POST
            , produces = {"application/json;charset=UTF-8"})
//    @ResponseBody告诉他返回的json数据
    @ResponseBody
    public SeckillResult<Exposer> /*TODO*/ exposer(@PathVariable("seckillId") Long seckillId) {
        SeckillResult<Exposer> result = null;
//       获取连接
        try {
            Exposer exposer = seckillService.exportSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }

    /**
     * 秒杀执行操作
     * required = false 参数是可选的 可以没有
     *
     * @param seckillId
     * @param md5
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execute"
            , method = RequestMethod.POST
            , produces = "application/json;charset=UTF-8")
    @ResponseBody //json
    public SeckillResult<SeckillExecution> execute
    (
            @PathVariable("seckillId") Long seckillId
            , @PathVariable("md5") String md5
            , @CookieValue(value = "killPhone", required = false) Long Phone
//   由于传入的参数没有Phone 但又是必须的 所以   @CookieValue 去cookie 中去找参数
//   由于这个参数必须要有 否则报错 required = false 参数是必须的不能没有
    ) {
        if (Phone == null) {
            //        判断电话号码 当然也可以交给springMvc 去验证 valid
            return new SeckillResult<SeckillExecution>(false, "用户未注册");
        }

        try {
//            成功
            SeckillExecution seckillExposer = seckillService.executeSeckill(seckillId, Phone, md5);
            return new SeckillResult<SeckillExecution>(true, seckillExposer);
        } catch (RepeatKillException re) { //重复秒杀异常
//            SeckillStatEnum.REPEAT_KILL 枚举 系统异常
            SeckillExecution seckillException = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
//            失败 但不是用字符串  还是使用SeckillExecution
            return new SeckillResult<SeckillExecution>(true, seckillException);
        } catch (SeckillCloseException sc) { //秒杀关闭异常
//            请求失败 但是还是true 为了前端可以显示出来 应为前端哪边 js只考虑true
//            为了显示重复秒杀让其为true  当然也可以修改逻辑
            SeckillExecution seckillException = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(true, seckillException);
        } catch (Exception e) {
            SeckillExecution seckillException = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, seckillException);
        }
    }

    /**
     * 获取系统当前时间
     * @return
     */
    @RequestMapping(value = "/time/now",method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date date=new Date();
        return new SeckillResult<Long>(true,date.getTime());
    }
}
