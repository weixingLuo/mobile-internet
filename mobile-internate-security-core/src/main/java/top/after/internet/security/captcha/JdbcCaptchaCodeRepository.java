package top.after.internet.security.captcha;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * TODO
 * @author david
 *
 */
public class JdbcCaptchaCodeRepository extends JdbcDaoSupport implements CaptchaCodeRepository{

	private String insertCodeSql = "insert into captcha_code(id,code,type,expire_time,verify) values(?,?,?,?,?)";
	private String verifyCodeSql = "update captcha_code set verify = 1 where id = ?";
	private String findCodeForSeriesSql = "select * from captcha_code where id = ?";

	@Override
	public CaptchaCode getCodeForSeries(String id) {
		List<CaptchaCode> captchaCodes = getJdbcTemplate().query(findCodeForSeriesSql,new Object[]{id},(rs, rowNum) -> new CaptchaCode(
				rs.getString(1),
				rs.getString(2),
				rs.getString(3),
				rs.getTimestamp(4).toLocalDateTime(),
				rs.getBoolean(5)));
		if(captchaCodes == null || captchaCodes.isEmpty()){
			return null;
		}
		return captchaCodes.get(0);
	}

	@Override
	public void createNewCode(CaptchaCode code){
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(con ->{
			PreparedStatement pt = getJdbcTemplate().getDataSource().getConnection().prepareStatement(insertCodeSql);
			pt.setString(1,code.getId());
			pt.setString(2,code.getCode());
			pt.setString(3,code.getType());
			pt.setTimestamp(4,java.sql.Timestamp.valueOf(code.getExpireTime()));
			pt.setBoolean(5,code.isVerify());
			return pt;
		},keyHolder);
	}

	@Override
	public void setVerified(String id) {
		getJdbcTemplate().update(verifyCodeSql,new Object[]{id});
	}


}
