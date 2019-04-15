package top.after.internet.security.sms.vender;


public class LocalShortMessageProvider implements ShortMessageProvider {

	@Override
	public void send(String phone, String templateId, Object... params) {
		System.out.println("------------------发送短信-------------------------------");
		System.out.print(String.join(",", phone,templateId));
		System.out.print(": ");
		for(Object o:params) {
			System.out.print(o);
			System.out.print(',');
		}
		System.out.println();
		System.out.println("------------------发送完成-------------------------------");
	}

	public static void main(String[] args) {
		LocalShortMessageProvider m = new LocalShortMessageProvider();
		m.send("159898", "1", 1,"o",true,null);
	}

}
