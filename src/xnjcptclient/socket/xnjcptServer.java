package xnjcptclient.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class xnjcptServer {
	public void startService() {
		try {
			ServerSocket server = new ServerSocket(8888);
			while (true) {
				Socket socket = server.accept();
				new Thread(new ServerRunnable(socket)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
