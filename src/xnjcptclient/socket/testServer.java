package xnjcptclient.socket;

public class testServer {
	public static void main(String[] args) {
		// 开启接收服务
		xnjcptServer xnjcptServer = new xnjcptServer();
		xnjcptServer.startService();
	}
}
