package xnjcptclient.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

//实时上传硬盘
public class DiskStatusRunnable implements Runnable {

	private final String URL = "http://114.215.196.150:8004/xnjcpt/receive/receive_getDiskStateInfor";

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (true) {
			String commandIo = "iostat -d -x";
			String commandDisk = "iostat -d";
			String commandDiskSize = "df -hl";
			String disk_state_use_size = null, disk_state_tps = null, disk_state_r = null, disk_state_w = null;
			long currTime = System.currentTimeMillis();
			String ipv4 = null;
			try {
				ipv4 = ComputerInfor.getIp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Process process;
			try {
				process = Runtime.getRuntime().exec(commandIo);
				String line = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				int flag = 0;
				while ((line = br.readLine()) != null) {
					flag++;
					if (flag == 4) {
						String[] temp = line.split("\\s+");
						disk_state_r = temp[3];
						disk_state_w = temp[4];
					}
				}
				br.close();
				process.destroy();
				process = Runtime.getRuntime().exec(commandDisk);
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				flag = 0;
				while ((line = br.readLine()) != null) {
					flag++;
					if (flag == 4) {
						String[] temp = line.split("\\s+");
						disk_state_tps = temp[1];
					}
				}
				br.close();
				process.destroy();
				process = Runtime.getRuntime().exec(commandDiskSize);
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				int flag1 = 0;
				while ((line = br.readLine()) != null) {
					flag1++;
					if (flag1 == 4) {
						String[] temp = line.split("\\s+");
						disk_state_use_size = temp[2];
					}
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_disk_state.disk_state_use_size="
						+ disk_state_use_size + "&xnjcpt_disk_state.disk_state_tps=" + disk_state_tps
						+ "&xnjcpt_disk_state.disk_state_r=" + disk_state_r + "&xnjcpt_disk_state.disk_state_w="
						+ disk_state_w + "&xnjcpt_disk_state.disk_state_time=" + sdf.format(new Date(currTime));
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
