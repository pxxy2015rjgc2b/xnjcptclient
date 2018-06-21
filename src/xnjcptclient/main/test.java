package xnjcptclient.main;

import com.google.gson.Gson;

public class test {
	public static void main(String[] args) {
		// Calendar c = Calendar.getInstance();
		// c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 7);
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		// System.out.println(sdf.format(c.getTime()));
		Gson gson = new Gson();
	}

	public static void getPaht() {
		System.out.println(Class.class.getClass().getResource("/").getPath());
	}
}
