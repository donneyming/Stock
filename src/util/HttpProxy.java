package util;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Random;

public class HttpProxy {
	final static String array[][] = {

	{ "122.72.33.138", "80" }, { "175.158.10.9", "80" },
			{ "218.152.121.185", "8080" }, { "114.34.133.132", "8080" },
			{ "210.107.100.251", "8080" }, { "129.143.4.64", "8080" },
			{ "194.170.16.75", "8888" }, { "202.93.221.63", "8080" },
			{ "111.1.32.72", "80" }, { "221.130.18.14", "80" } };

	public static Proxy getProxy() {
		/*Random random = new Random();
		int s = random.nextInt(11);
		if (s == 11)
			return null;
		else {
			String ip = array[s][0];
			int port = Integer.parseInt(array[s][1]);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip,
					port));
			return proxy;
		}*/
		return null;
	}
}
