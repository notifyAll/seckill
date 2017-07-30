import org.junit.Test;


import java.net.Inet4Address;
import java.net.InetAddress;
/**
 * Created by notifyAll on 2017/7/23.
 */
public class IpTest {
    @Test
    public void getIp(){
            int i=1;
//        http://192.168.1.10/
            final byte[] bytes=new byte[4];
            bytes[0]= (byte) 192;
            bytes[1]= (byte) 168;
            bytes[2]= 0;
        while (i<=255){
            bytes[3]= (byte) i;
//            System.out.println(i);
            new Thread(new Runnable() {
                public void run() {
                    InetAddress inetAddress=null;
                    try {
                        inetAddress=  Inet4Address.getByAddress(bytes);

                        if (inetAddress.isReachable(20)){
                            System.out.println(inetAddress.getHostAddress());
                        }
                    } catch (Exception e) {
                        return;
                    }
                }
            }).start();
               i++;
            }

    }
}
