package xnjcptclient.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

//瀹炴椂鑾峰彇io鐘舵��
public class IOStatusRunnable implements Runnable {

	private final String URL = "/xnjcpt/receive/receive_getIoStateInfor";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			String rsec = null, wsec = null, rkb = null, wkb = null, wait = null, util = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long currTime = System.currentTimeMillis();
			String command = "iostat -d -x -k";
			String ipv4 = null;
			try {
				ipv4 = ComputerInfor.getIp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String[] temp = null;
			try {
				Process process = Runtime.getRuntime().exec(command);
				String line = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				int flag = 0;
				while ((line = br.readLine()) != null) {
					flag++;
					if (flag == 4) {
						temp = line.split("\\s+");
						rkb = temp[5];
						wkb = temp[6];
						wait = temp[9]; //
						util = temp[13];
					}
				}

				String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_io_state.io_state_rkb=" + rkb
						+ "&xnjcpt_io_state.io_state_wkb=" + wkb + "&xnjcpt_io_state.io_state_wait=" + wait
						+ "&xnjcpt_io_state.io_state_util=" + util + "&xnjcpt_io_state.io_state_time="
						+ sdf.format(new Date(currTime));
				System.out.println(sendD);
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							SendData.post(URL, sendD);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				thread.start();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
