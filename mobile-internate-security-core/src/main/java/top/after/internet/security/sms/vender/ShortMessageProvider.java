package top.after.internet.security.sms.vender;

public interface ShortMessageProvider {
	public void send(String phone,String templateId,Object... params);
}
