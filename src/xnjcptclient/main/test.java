package xnjcptclient.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class test {
	public static void main(String[] args) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR) - 7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println(sdf.format(c.getTime()));
	}

	public static void getPaht() {
		System.out.println(Class.class.getClass().getResource("/").getPath());
	}
}
