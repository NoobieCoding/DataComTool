import java.util.Vector;

/**
 * Created by tuterdust on 21/11/2560.
 */
public class Main {
    public static void main(String[] args) {
        NetworkInfo nw = new NetworkInfo();
        System.out.println("My IP Address:" + nw.getMyNetworkIPs());
        System.out.println("My MAC Address:" + nw.getMacAddress());
    }
}
