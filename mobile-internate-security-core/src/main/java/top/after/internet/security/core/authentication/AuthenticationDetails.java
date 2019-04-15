package top.after.internet.security.core.authentication;
import java.io.Serializable;

public interface AuthenticationDetails extends Serializable {

	public String getUserId();
	
	public String getUserName();
	
}
