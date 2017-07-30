package hello;

import org.junit.Test;

/**
 * Created by notifyAll on 2017/7/29.
 */
public class StringTest {
    @Test
    public void test1(){
        String s1="a";
        String s2=s1+"b";
        String s3="a"+"b";

        System.out.println(s2=="ab"); //false
        System.out.println(s3=="ab"); //true
    }
}
