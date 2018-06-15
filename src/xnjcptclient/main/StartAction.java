package xnjcptclient.main;

import java.io.IOException;

import xnjcptclient.capture.ComputerInfor;
import xnjcptclient.capture.CpuStatusRunnable;
import xnjcptclient.capture.DiskStatusRunnable;
import xnjcptclient.capture.IOStatusRunnable;
import xnjcptclient.capture.MemoryStatusRunnable;
import xnjcptclient.capture.NetStatusRunnable;
import xnjcptclient.capture.ProgressRunnable;
import xnjcptclient.socket.xnjcptServer;

// 客户端脚本
public class StartAction {
	public static void main(String[] args) throws IOException {
		ComputerInfor.getIPByCurl();
		ComputerInfor computerInfor = new ComputerInfor();
		computerInfor.getComputerInfor();
		computerInfor.getCPUInfo();
		computerInfor.getMemoryInfo();
		computerInfor.getDiskInfo();
		computerInfor.getNetInfo();
		// 实时上传cpu
		Thread threadCpu = new Thread(new CpuStatusRunnable());
		threadCpu.start();
		// 实时上传内存
		Thread threadMem = new Thread(new MemoryStatusRunnable());
		threadMem.start();
		// 实时上传io
		Thread threadIO = new Thread(new IOStatusRunnable());
		threadIO.start();
		// 实时上传磁盘
		Thread threadDisk = new Thread(new DiskStatusRunnable());
		threadDisk.start();
		// 实时上传网络
		Thread threadNet = new Thread(new NetStatusRunnable());
		threadNet.start();
		// 实时上传进程信息
		Thread threadProgress = new Thread(new ProgressRunnable());
		threadProgress.start();
		// 开启接收服务
		xnjcptServer xnjcptServer = new xnjcptServer();
		xnjcptServer.startService();
	}
}