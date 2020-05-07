package org.kunze.diansh.pust;


import org.kunze.diansh.pust.ios.IOSBroadcast;

public class IosDemo {
	private String appkey = null;
	private String appMasterSecret = null;
	private String timestamp = null;
	private PushClient client = new PushClient();
	public IosDemo(String key, String secret) {
		try {
			appkey = key;
			appMasterSecret = secret;
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void sendIOSBroadcast() throws Exception {
		IOSBroadcast broadcast = new IOSBroadcast(appkey, appMasterSecret);
//		broadcast.setTicker( "Android broadcast ticker");
//		broadcast.setTitle(  "中文的title");
//		broadcast.setText(   "Android broadcast text");
//		broadcast.goAppAfterOpen();
	}
}
