package top.after.internet.security.core.user;


import java.util.List;

import javax.sql.DataSource;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

public class UserAccountService extends JdbcUserDetailsManager{

	private String usersByUsernameQuery = "select id,username,password,enabled "
			+ "from users " + "where username = ?";
	private String userByUserIdQuery = "select id,username,password,enabled "
			+ "from users " + "where id = ?";

	public UserAccountService() {
		super();
	}
	
	public UserAccountService(DataSource datasource) {
		super(datasource);
		
	}
	
	public UserAccount loadUserById(String userId) {
		return getJdbcTemplate().query(this.userByUserIdQuery,
				new Integer[] { Integer.parseInt(userId) },rs ->{
					Integer id = rs.getInt(1);
					String username1 = rs.getString(2);
					String password = rs.getString(3);
					boolean enabled = rs.getBoolean(4);
					return new UserAccount(id, username1, password, enabled,
							AuthorityUtils.NO_AUTHORITIES);
				});
	}
	/**
	 * Executes the SQL <tt>usersByUsernameQuery</tt> and returns a list of UserDetails
	 * objects. There should normally only be one matching user.
	 */
	protected List<UserDetails> loadUsersByUsername(String username) {
		return getJdbcTemplate().query(this.usersByUsernameQuery,
				new String[] { username }, (rs, rowNum) -> {
					Integer id = rs.getInt(1);
					String username1 = rs.getString(2);
					String password = rs.getString(3);
					boolean enabled = rs.getBoolean(4);
					return new UserAccount(id, username1, password, enabled,
							AuthorityUtils.NO_AUTHORITIES);
				});
	}

	protected UserDetails createUserDetails(String username, UserDetails userFromUserQuery, List<GrantedAuthority> combinedAuthorities) {
	    if(!(userFromUserQuery instanceof UserAccount)){
	    	return super.createUserDetails(username, userFromUserQuery, combinedAuthorities);
		}
		UserAccount userAccount = (UserAccount) userFromUserQuery;
		return new UserAccount(userAccount.getId(), username, userAccount.getPassword(), userFromUserQuery.isEnabled(), true, true, true, combinedAuthorities);
	}


}
