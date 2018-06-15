package xnjcptclient.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

//瀹炴椂鏇存柊cpu鐘舵�佽〃
public class CpuStatusRunnable implements Runnable {
	String URL = "http://114.215.196.150:8004/xnjcpt/receive/receive_getCpuStateInfor";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			String command = "iostat";
			float user, nice, system, iowait, idle;
			String[] temp = null;
			String ipv4 = null;
			long startTime = System.currentTimeMillis();
			try {
				ipv4 = ComputerInfor.getIp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Process process;
			try {
				process = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				int flag = 0;
				while ((line = br.readLine()) != null) {
					flag++;
					if (flag == 4) {
						temp = line.split("\\s+");
					}
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			user = Float.parseFloat(temp[1]) / 100;
			user = new BigDecimal(user).setScale(3, 4).floatValue();
			nice = Float.parseFloat(temp[2]) / 100;
			nice = new BigDecimal(nice).setScale(3, 4).floatValue();
			system = Float.parseFloat(temp[3]) / 100;
			system = new BigDecimal(system).setScale(3, 4).floatValue();
			iowait = Float.parseFloat(temp[4]) / 100;
			iowait = new BigDecimal(iowait).setScale(3, 4).floatValue();
			idle = Float.parseFloat(temp[6]) / 100;
			idle = new BigDecimal(idle).setScale(3, 4).floatValue();
			SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_cpu_state.cpu_state_user=" + user
					+ "&xnjcpt_cpu_state.cpu_state_nice=" + nice + "&xnjcpt_cpu_state.cpu_state_system=" + system
					+ "&xnjcpt_cpu_state.cpu_state_iowait=" + iowait + "&xnjcpt_cpu_state.cpu_state_idle=" + idle
					+ "&xnjcpt_cpu_state.cpu_state_time=" + spf.format(new Date(startTime));
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
		}
	}

}
