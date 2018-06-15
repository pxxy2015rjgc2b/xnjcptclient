package xnjcptclient.capture;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SendData {

	public static void post(String strURL, String sendD) throws IOException {
		URL url = new URL(strURL);// 创建连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestMethod("POST"); // 设置请求方式
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); // 设置发送数据的格式
		connection.setRequestProperty("Content-Lenth", String.valueOf(sendD.length()));
		connection.connect();
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
		BufferedWriter bw = new BufferedWriter(out);
		bw.write(sendD);
		bw.flush();
		bw.close();
		InputStream is = connection.getInputStream();
	}
}
