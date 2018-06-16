package xnjcptclient.main;

public class test {
	public static void main(String[] args) {
		test.getPaht();
	}

	public static void getPaht() {
		System.out.println(Class.class.getClass().getResource("/").getPath());
	}
}
