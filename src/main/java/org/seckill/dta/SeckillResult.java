package org.seckill.dta;

/**
 * Created by notifyAll on 2017/7/18.
 */
//所有的 ajax 请求返回类型，封装 json结果
public class SeckillResult<T> {
    private boolean success;

    private T data;

    private String error;

//    true
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
// false
    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    /**
     * 是否成功 状态
     * @return
     */
    public boolean isSuccess(){
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
