package top.after.internet.security.core.user;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class UserAccount extends User{

	private static final long serialVersionUID = -1884475895405628423L;
    protected Integer id;
    protected LocalDateTime updateTime;
    protected LocalDateTime createTime;
    
    public UserAccount(Integer id, String username, String password,boolean enabled, Collection<? extends GrantedAuthority> authorities) {
    	super(username, password, enabled, true, true, true, authorities);
    	this.id=id;
    }

	public UserAccount(Integer id,String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired,credentialsNonExpired,accountNonLocked, authorities);
		this.id = id;
	}
    
    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
}
