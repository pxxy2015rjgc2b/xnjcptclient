package xnjcptclient.capture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;

import xnjcptclient.domain.ComputerIpDTO;

public class ComputerInfor {
	private Process processHost;
	private String hostname;
	private final String URLCOMPUTER = "/xnjcpt/receive/receive_getComputerInfor";
	private final String URLCPU = "/xnjcpt/receive/receive_getCpuInfo";
	private final String URLMEMORY = "/xnjcpt/receive/receive_getMemoryInfor";
	private final String URLDISK = "/xnjcpt/receive/receive_getDiskInfor";
	private final String URLNET = "/xnjcpt/receive/receive_getNetInfor";
	private static String ip = null;

	// 获得主机信息
	public void getComputerInfor() {
		Runtime run = Runtime.getRuntime();
		String command = "cat /etc/hostname";
		try {
			processHost = run.exec(command);
			BufferedReader br = new BufferedReader(new InputStreamReader(processHost.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				hostname = line;
				String ipv4 = this.getIp();
				String sendD = "xnjcpt_computer.computer_name=" + hostname + "&xnjcpt_computer.computer_ip=" + ipv4;
				SendData.post(URLCOMPUTER, sendD);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 获得cpu信息
	public void getCPUInfo() throws IOException {
		String ipv4 = this.getIp();
		// 涓婚鍗曚綅MHZ 缂撳瓨澶у皬KB
		String cpu_model = null, cpu_basic_frequency = null, cpu锛縞atch_size = null, cpu_cores = null;
		Runtime run = Runtime.getRuntime();// 鑾峰緱涓绘満杩愯缁堢
		String command = "cat /proc/cpuinfo";// 鎵цcat /proc/cpuinfo鍛戒护
		Process proccessCput = run.exec(command);// 鎵ц鍛戒护
		BufferedReader br = new BufferedReader(new InputStreamReader(proccessCput.getInputStream()));// 鑾峰緱鍛戒护鎵ц缁撴灉
		int flag = 0;
		String line = null;
		while ((line = br.readLine()) != null) {
			flag++;
			switch (flag) {
			case 5:
				int f = line.indexOf(":");
				cpu_model = line.substring(f + 2, line.length());
				break;
			case 8:
				int f1 = line.indexOf(":");
				cpu_basic_frequency = line.substring(f1 + 2, line.length());
				break;

			case 9:
				int f2 = line.indexOf(":");
				cpu锛縞atch_size = line.substring(f2 + 2, line.length());
				break;
			case 13:
				int f3 = line.indexOf(":");
				cpu_cores = line.substring(f3 + 2, line.length());
				break;
			}
		}
		String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_cpu.cpu_model=" + cpu_model
				+ "&xnjcpt_cpu.cpu_basic_frequency=" + cpu_basic_frequency + "&xnjcpt_cpu.cpu_catch_size="
				+ cpu锛縞atch_size + "&xnjcpt_cpu.cpu_cores=" + cpu_cores;
		System.out.println(sendD);
		SendData.post(URLCPU, sendD);
	}

	// 获得内存信息
	public void getMemoryInfo() throws IOException {
		String ipv4 = this.getIp();
		// 鍗曚綅kb
		String memory_size = null, memory_swap = null;
		String command = "cat /proc/meminfo";
		Process processMem = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(processMem.getInputStream()));
		String line = "";
		while ((line = br.readLine()) != null) {
			if (line.startsWith("MemTotal")) {
				String[] temp = line.split("\\s+");
				memory_size = temp[1];
			} else if (line.startsWith("SwapTotal")) {
				String[] temp = line.split("\\s+");
				memory_swap = temp[1];
			}
		}
		String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_memory.memory_size=" + memory_size
				+ "&xnjcpt_memory.memory_swap=" + memory_swap;
		System.out.println(sendD);
		SendData.post(URLMEMORY, sendD);
	}

	// 获得硬盘信息
	public void getDiskInfo() throws IOException {
		String ipv4 = this.getIp();
		String disk_size = null;
		String command = "df -hl";
		Process processDisk = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(processDisk.getInputStream()));
		String line = "";
		int flag = 0;
		while ((line = br.readLine()) != null) {
			flag++;
			if (flag == 4) {
				String[] temp = line.split("\\s+");
				disk_size = temp[1];
			}
		}
		String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_disk.disk_size=" + disk_size;
		System.out.println(sendD);
		SendData.post(URLDISK, sendD);
	}

	// 获得网络信息
	public void getNetInfo() throws IOException {
		String ipv4 = this.getIp();
		String net_mac = null, net_ipv6 = null;
		String command = "ifconfig";
		Process processNet = Runtime.getRuntime().exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(processNet.getInputStream()));
		String line = "";
		int flag = 0;
		while ((line = br.readLine()) != null) {
			flag++;
			if (flag == 1) {
				String[] temp = line.split("\\s+");
				net_mac = temp[4];
			} else if (flag == 3) {
				String[] temp = line.split("\\s+");
				net_ipv6 = temp[2];
			}
		}
		String sendD = "xnjcpt_computer.computer_ip=" + ipv4 + "&xnjcpt_net.net_mac=" + net_mac
				+ "&xnjcpt_net.net_ipv6=" + net_ipv6;
		SendData.post(URLNET, sendD);
	}

	// 获得ip地址
	public static String getIp() throws IOException {
		return ip;
	}

	// 初始化ip
	public static void getIPByCurl() throws IOException {
		String ipv4 = "";
		Process processIP;
		Runtime run = Runtime.getRuntime();
		String command = "curl ipinfo.io";
		int flag = 0;
		processIP = run.exec(command);
		BufferedReader br = new BufferedReader(new InputStreamReader(processIP.getInputStream()));
		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		Gson gson = new Gson();
		ComputerIpDTO c = gson.fromJson(sb.toString(), ComputerIpDTO.class);
		ip = c.getIp();
		br.close();
	}
}
