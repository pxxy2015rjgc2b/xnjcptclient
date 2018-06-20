package xnjcptclient.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

//获得实时内存
public class MemoryStatusRunnable implements Runnable {

	private final String URL = "/xnjcpt/receive/receive_getMemoryStateInfor";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			String ipv4 = null;
			try {
				ipv4 = ComputerInfor.getIp();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			long currTime = System.currentTimeMillis();
			String command = "cat /proc/meminfo";
			long memTotal = 0, memFree = 0, swapTotal = 0, swapFree = 0;
			float memory_state_swap_rate, memory_state_mem_rate;
			try {
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("MemTotal")) {
						memTotal = Long.parseLong(line.split("\\s+")[1]);
					} else if (line.startsWith("MemFree")) {
						memFree = Long.parseLong(line.split("\\s+")[1]);
					} else if (line.startsWith("SwapTotal")) {
						swapTotal = Long.parseLong(line.split("\\s+")[1]);
					} else if (line.startsWith("SwapFree")) {
						swapFree = Long.parseLong(line.split("\\s+")[1]);
					}
				}
				// memory_state_swap_rate = new BigDecimal(1 - ((float) swapFree
				// / (float) swapTotal)).setScale(3, 4)
				// .floatValue();
				memory_state_mem_rate = new BigDecimal(1 - ((float) memFree / (float) memTotal)).setScale(3, 4)
						.floatValue();
				SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_memory_state.memory_state_swap_rate="
						+ 0.0 + "&xnjcpt_memory_state.memory_state_mem_rate=" + memory_state_mem_rate
						+ "&xnjcpt_memory_state.memory_state_time=" + spf.format(new Date(currTime));
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
