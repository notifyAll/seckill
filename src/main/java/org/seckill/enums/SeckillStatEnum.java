package org.seckill.enums;

/**
 * 使用枚举表示常量数据字段
 * Created by notifyAll on 2017/7/15.
 */
//enum
public enum  SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"系统异常"),
    INNER_ERROR(-2,"系统内部异常"),
    DATA_REWRITE(-3,"数据篡改");
    private int state;

    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    private static SeckillStatEnum stateOf(int index){
        for (SeckillStatEnum state: values()){
            if (state.getState()==index){
                return state;
            }
        }
        return null;
    }
    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }
}
