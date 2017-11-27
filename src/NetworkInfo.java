
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
	private String mac_address = "";

	public NetworkInfo() {
		findMacAddress();
		// getData();
	}

	public Vector<String> getAvailableDevices() {
		return availableDevices;
	}

	public void findMacAddress() {
		try {
			InetAddress ipAddress = InetAddress.getLocalHost();
			NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
			byte[] macAddressBytes = networkInterface.getHardwareAddress();
			StringBuilder macAddressBuilder = new StringBuilder();

			for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++) {
				String macAddressHexByte = String.format("%02X", macAddressBytes[macAddressByteIndex]);
				macAddressBuilder.append(macAddressHexByte);

				if (macAddressByteIndex != macAddressBytes.length - 1) {
					macAddressBuilder.append(":");
				}
			}

			mac_address = macAddressBuilder.toString();

		} catch (Exception e) {

		}
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
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		getNetworkData();
	}

	public String getMyIPAddress() {
		try {
			String myIP = InetAddress.getLocalHost().getHostAddress();
			return myIP;
		} catch (Exception e) {
			return "";
		}
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

					System.out.println("Search log:");
					for (int i = 1; i <= 254; ++i) {
						try {
							InetAddress addr = InetAddress.getByName(mynetworkips + new Integer(i).toString());
							String newAddr = addr.getHostAddress();
							if (addr.isReachable(300)) {
								availableDevices.add(newAddr);
								setChanged();
								notifyObservers(availableDevices);
								System.out.println("Find " + newAddr);
							} else {
								System.out.println("Not find " + newAddr);
							}

						} catch (Exception e) {
						}
					}

					System.out.println("\nAll Connected devices(" + availableDevices.size() + "):");
					for (int i = 0; i < availableDevices.size(); ++i)
						System.out.println(availableDevices.get(i));
				} catch (Exception e) {
				}
			}
		};
		t.start();
	}

	public String getMyNetworkIPs() {
		return myNetworkIPs;
	}

	public String getMacAddress() {
		return mac_address;
	}
}
