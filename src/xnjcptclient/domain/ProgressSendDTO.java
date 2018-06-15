package xnjcptclient.domain;

import java.util.List;

public class ProgressSendDTO {
	private String ip;
	private List<ProgressDTO> list;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public List<ProgressDTO> getList() {
		return list;
	}

	public void setList(List<ProgressDTO> list) {
		this.list = list;
	}

}
