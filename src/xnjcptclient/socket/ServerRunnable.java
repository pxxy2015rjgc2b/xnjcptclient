package xnjcptclient.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class ServerRunnable implements Runnable {

	private Socket socket;

	public ServerRunnable(Socket socket) {
		super();
		this.socket = socket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = null;
			String head = null;// 请求头
			String content = null;// 请求内容
			Deal deal = new Deal();
			int flag = 0;
			line = br.readLine();
			head = line;
			line = br.readLine();
			content = line;
			Class dealClass = deal.getClass();
			String[] temp = head.split("\\s+");
			try {
				Method method = dealClass.getMethod(head, String.class, Socket.class);
				try {
					method.invoke(deal, new Object[] { content, socket });
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (

		IOException e)

		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
