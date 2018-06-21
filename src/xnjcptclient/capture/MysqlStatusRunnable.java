package xnjcptclient.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MysqlStatusRunnable implements Runnable {
	private String openURL = "/xnjcpt/receive/receive_updateComputerMysql";
	private String statusURL = "/xnjcpt/receive/receive_getMysqlStatus";

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String openCommand = "mysql --help";// 验证是否开启mysql
		String statusCommand = "mysqladmin -u root --host=127.0.0.1 --password=root extended-status";// 获取mysql状态
		try {
			String ipv4 = ComputerInfor.getIp();
			while (true) {
				long currTime = System.currentTimeMillis();
				String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(currTime));
				Process process = Runtime.getRuntime().exec(openCommand);
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = br.readLine();
				br.close();
				if (line.startsWith("The")) {
					String sendData = "xnjcpt_computer.computer_ip=" + ipv4
							+ "&xnjcpt_computer.computer_isopen_mysql=0";
					SendData.post(openURL, sendData);
				} else {
					String sendData = "xnjcpt_computer.computer_ip=" + ipv4
							+ "&xnjcpt_computer.computer_isopen_mysql=1";
					SendData.post(openURL, sendData);
					process.destroy();
					process = Runtime.getRuntime().exec(statusCommand);
					br = new BufferedReader(new InputStreamReader(process.getInputStream()));
					int questions1 = 0, com_commit1 = 0, com_rollback1 = 0;
					int questions2 = 0, com_commit2 = 0, com_rollback2 = 0;
					String temp[];
					while ((line = br.readLine()) != null) {
						if (line.indexOf("Questions") != -1) {
							temp = line.split("\\s+");
							questions1 = Integer.parseInt(temp[3]);
						} else if (line.indexOf("Com_commit") != -1) {
							temp = line.split("\\s+");
							com_commit1 = Integer.parseInt(temp[3]);
						} else if (line.indexOf("Com_rollback") != -1) {
							temp = line.split("\\s+");
							com_rollback1 = Integer.parseInt(temp[3]);
						}
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					process.destroy();
					process = Runtime.getRuntime().exec(statusCommand);
					br = new BufferedReader(new InputStreamReader(process.getInputStream()));
					while ((line = br.readLine()) != null) {
						if (line.indexOf("Questions") != -1) {
							temp = line.split("\\s+");
							questions2 = Integer.parseInt(temp[3]);
						} else if (line.indexOf("Com_commit") != -1) {
							temp = line.split("\\s+");
							com_commit2 = Integer.parseInt(temp[3]);
						} else if (line.indexOf("Com_rollback") != -1) {
							temp = line.split("\\s+");
							com_rollback2 = Integer.parseInt(temp[3]);
						}
					}

					float qps = new BigDecimal(((float) questions2 - (float) questions1) / 10).setScale(3, 4)
							.floatValue();
					float tps = new BigDecimal((((float) com_commit2 - (float) com_commit1)
							+ ((float) com_rollback2 - (float) com_rollback1)) / 10).setScale(3, 4).floatValue();
					String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_mysql.mysql_qps=" + qps
							+ "&xnjcpt_mysql.mysql_tps=" + tps + "&xnjcpt_mysql.mysql_time=" + time;
					System.out.println(sendD);
					SendData.post(statusURL, sendD);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
