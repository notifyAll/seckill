package org.seckill.exception;

/**
 * 秒杀相关的异常
 * Created by notifyAll on 2017/7/12.
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
