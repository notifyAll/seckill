package org.seckill;

import org.junit.Test;

/**
 * Created by notifyAll on 2017/7/22.
 */
public class ThreadTest {
    class Flag{
        public boolean flag=true;
    }

    @Test
    public  void  test1() {
        final Flag test=new Flag();

        Thread thread=new Thread(new Runnable() {
            public void run() {
                System.out.println("线程启动");
                int i=0;
                Flag flag=test;
                while (flag.flag){
                    i++;
                }
                System.out.println("线程结束"+i);
            }
        });
        thread.setName("aaa");
        thread.start();

        try {
            System.out.println("主线程休眠");
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test.flag=false;
//        this.notifyAll();
        if (thread.isAlive()){
            System.out.println("处于活动状态");
        }
        if (thread.isInterrupted()){
            System.out.println("线程中断");
        }
        System.out.println("主线程结束");
    }

    @Test
    public void test2(){
        Flag test=new Flag();
        while (test.flag==true){

        }
        System.out.println("线程结束");
    }
}
