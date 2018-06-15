package xnjcptclient.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

//瀹炴椂涓婁紶缃戠粶
public class NetStatusRunnable implements Runnable {

	private final String URL = "http://114.215.196.150:8004/xnjcpt/receive/receive_getNetStateInfor";
	private final int FLAGCURR = 4;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			long currTime = System.currentTimeMillis();
			String[] temp1 = null, temp2 = null;
			float net_state_obandwith = 0, net_state_ibandwith = 0, net_state_opackage = 0, net_state_ipackage = 0,
					net_state_otraffic = 0;
			int net_state_tcp_number = 0;
			int tcpNumber1 = 0;
			int tcpNumber2 = 0;
			String commandNet = "cat /proc/net/dev";
			String commandTcp = "cat /proc/net/snmp";
			String ipv4 = null;
			try {
				ipv4 = ComputerInfor.getIp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Process process;
			try {
				process = Runtime.getRuntime().exec(commandNet);
				BufferedReader br;
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line;
				int flag1 = 0;
				while ((line = br.readLine()) != null) {
					flag1++;
					if (flag1 == FLAGCURR) {
						temp1 = line.split("\\s+");
					}
				}
				br.close();
				process.destroy();
				process = Runtime.getRuntime().exec(commandTcp);
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				int flag = 0;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("Tcp")) {
						flag++;
						if (flag == 2) {
							tcpNumber1 = Integer.parseInt(line.split("\\s+")[9]);
						}
					}
				}
				flag = 0;
				br.close();
				process.destroy();
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				process = Runtime.getRuntime().exec(commandNet);
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				int flag2 = 0;
				while ((line = br.readLine()) != null) {
					flag2++;
					if (flag2 == FLAGCURR) {
						System.out.println(line);
						temp2 = line.split("\\s+");
					}
				}
				br.close();
				process.destroy();
				process = Runtime.getRuntime().exec(commandTcp);
				br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((line = br.readLine()) != null) {
					if (line.startsWith("Tcp")) {
						flag++;
						if (flag == 2) {
							tcpNumber2 = Integer.parseInt(line.split("\\s+")[9]);
						}
					}
				}
				flag = 0;
				br.close();
				process.destroy();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				net_state_tcp_number = tcpNumber2; // 鑾峰緱tcp杩炴帴涓暟
				net_state_otraffic = new BigDecimal(
						(float) (Integer.parseInt(temp2[10]) - Integer.parseInt(temp1[10])) / 1000000).setScale(3, 4)
								.floatValue();// 鍑烘祦閲�
				net_state_obandwith = new BigDecimal(
						(float) (Integer.parseInt(temp2[10]) - Integer.parseInt(temp1[10])) / 10000000).setScale(3, 4)
								.floatValue(); // 鍑哄甫瀹組b/s
				net_state_ibandwith = new BigDecimal(
						(float) (Integer.parseInt(temp2[2]) - Integer.parseInt(temp1[2])) / 10000000).setScale(3, 4)
								.floatValue(); // 鍏ュ甫瀹組b/s
				net_state_opackage = new BigDecimal(
						(float) (Integer.parseInt(temp2[11]) - Integer.parseInt(temp1[11])) / 10).setScale(3, 4)
								.floatValue(); // 鍏ュ寘閲廙b/s
				net_state_ipackage = new BigDecimal(
						(float) (Integer.parseInt(temp2[3]) - Integer.parseInt(temp1[3])) / 10).setScale(3, 4)
								.floatValue(); // 鍏ュ寘閲廙b/s
				String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_net_state.disk_state_time="
						+ sdf.format(new Date(currTime)) + "&xnjcpt_net_state.net_state_obandwidth="
						+ net_state_obandwith + "&xnjcpt_net_state.net_state_ibandwidth=" + net_state_ibandwith
						+ "&xnjcpt_net_state.net_state_opackage=" + net_state_opackage
						+ "&xnjcpt_net_state.net_state_ipackage=" + net_state_ipackage
						+ "&xnjcpt_net_state.net_state_tcp_number=" + net_state_tcp_number
						+ "&xnjcpt_net_state.net_state_otraffic=" + net_state_otraffic;
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
