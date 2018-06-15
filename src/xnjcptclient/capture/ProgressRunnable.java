package xnjcptclient.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import xnjcptclient.domain.ProgressDTO;

public class ProgressRunnable implements Runnable {

	private final String URL = "http://114.215.196.150:8004/xnjcpt/receive/receive_getProgressInfor";

	@Override
	public void run() {
		while (true) {
			// TODO Auto-generated method stub
			String command = "ps -ef";
			String ip = null;
			try {
				ip = ComputerInfor.getIp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String sendD = "ip=" + ip;
			List<ProgressDTO> list = new ArrayList<ProgressDTO>();
			try {
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String[] temp;
				String line;
				int flag = 0;
				while ((line = br.readLine()) != null) {

					flag++;
					if (flag > 1) {
						int flagSend = flag - 2;
						temp = line.split("\\s+");
						// p.setPid(temp[1]);
						// p.setCmd(temp[7]);
						sendD = sendD + "&progressList[" + flagSend + "].progress_pid=" + temp[1] + "&progressList["
								+ flagSend + "].progress_name=" + temp[7];
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(sendD);
			try {
				SendData.post(URL, sendD);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
