
/**
 * Created by tuterdust on 21/11/2560.
 */

import java.net.*;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Vector;

public class NetworkInfo extends Observable {

	private String myNetworkIPs = "";
	private Vector<String> availableDevices = new Vector<>();

	public NetworkInfo() {}

	public Vector<String> getAvailableDevices() {
		return availableDevices;
	}

	public void getData() {
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				if (iface.isLoopback() || !iface.isUp() || iface.isVirtual() || iface.isPointToPoint())
					continue;

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();

					final String ip = addr.getHostAddress();
					if (Inet4Address.class == addr.getClass())
						myNetworkIPs = ip;
				}
			}
		} catch (SocketException e) { throw new RuntimeException(e); }
		getNetworkData();
	}

	public String getMyIPAddress() {
		try {
			String myIP = InetAddress.getLocalHost().getHostAddress();
			return myIP;
		} catch (Exception e) { return "";	}
	}

	public void getNetworkData() {
		Thread t = new Thread() {
			public void run() {
				try {
					availableDevices = new Vector<String>();
					String myip = InetAddress.getLocalHost().getHostAddress();
					String mynetworkips = new String();

					for (int i = myip.length(); i > 0; --i) {
						if (myip.charAt(i - 1) == '.') {
							mynetworkips = myip.substring(0, i);
							break;
						}
					}
					int countChecking = 0;
					for (int i = 1; i <= 254; ++i) {
						try {
							InetAddress addr = InetAddress.getByName(mynetworkips + new Integer(i).toString());
							String newAddr = addr.getHostAddress();
							countChecking++;
							if (addr.isReachable(100)) {
								availableDevices.add(newAddr);
							}
							setChanged();
							notifyObservers(countChecking);
						} catch (Exception e) {
						}
					}

					System.out.println("\nAll Connected devices(" + availableDevices.size() + "):");
					for (int i = 0; i < availableDevices.size(); ++i)
						System.out.println(availableDevices.get(i));
					setChanged();
					notifyObservers("Finish");
				} catch (Exception e) {}
			}
		};
		t.start();
	}

	public String getMyNetworkIPs() {
		return myNetworkIPs;
	}

}
