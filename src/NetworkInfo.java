/**
 * Created by tuterdust on 21/11/2560.
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.Vector;

public class NetworkInfo {

    private String mynetworkips = "";
    private Vector<String> Available_Devices = new Vector<>();
    private Vector<String> Unavailable_Devices = new Vector<>();

    public NetworkInfo() {
        getData();
    }

    public Vector<String> getAvailable_Devices() {
        return Available_Devices;
    }

    public Vector<String> getUnavailable_Devices() {
        return Unavailable_Devices;
    }

    public void getData() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    final String ip = addr.getHostAddress();
                    if(Inet4Address.class == addr.getClass())
                        mynetworkips = ip;
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        getNetworkData();
    }

    public void getNetworkData() {
       try {
           Vector<String> Available_Devices=new Vector<>();
           String myip=InetAddress.getLocalHost().getHostAddress();
           String mynetworkips=new String();

           for(int i=myip.length();i>0;--i) {
               if(myip.charAt(i-1)=='.'){ mynetworkips=myip.substring(0,i); break; }
           }

           System.out.println("My Device IP: " + myip+"\n");

           System.out.println("Search log:");
           for(int i=1;i<=254;++i){
               try {
                   InetAddress addr=InetAddress.getByName(mynetworkips + new Integer(i).toString());
                   if (addr.isReachable(1000)){
                       System.out.println("Available: " + addr.getHostAddress());
                       Available_Devices.add(addr.getHostAddress());
                   }
                   else System.out.println("Not available: "+ addr.getHostAddress());

               }catch (Exception ioex){}
           }

           System.out.println("\nAll Connected devices(" + Available_Devices.size() +"):");
           for(int i=0;i<Available_Devices.size();++i) System.out.println(Available_Devices.get(i));
       } catch (Exception e) {

       }
    }

    public String getMynetworkips() {
        return mynetworkips;
    }
}
