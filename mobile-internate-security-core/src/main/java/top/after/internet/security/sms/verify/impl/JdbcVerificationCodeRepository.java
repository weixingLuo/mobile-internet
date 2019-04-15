package top.after.internet.security.sms.verify.impl;

import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import top.after.internet.security.sms.verify.VerificationCode;
import top.after.internet.security.sms.verify.VerificationCodeRepository;

/**
 * TODO 
 * @author david
 *
 */
public class JdbcVerificationCodeRepository extends JdbcDaoSupport implements VerificationCodeRepository {

	private String insertCodeSql = "insert into verification_code(id,target,code,type,expire_time,disabled) values(?,?,?,?,?,?)";
	private String removeCodeSql = "update verification_code set disabled = ? where id = ?";
	private String findCodeForSeriesSql = "select * from verification_code where id = ?";
	private String findReuseableSql = "select * from verification_code where target =? and type =? and disabled = 0";

	@Override
	public VerificationCode getCodeForSeries(String id) {
		List<VerificationCode> verificationCodes = getJdbcTemplate().query(findCodeForSeriesSql,new Object[]{id}, (rs, rowNum) ->
				new VerificationCode(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getTimestamp(5).toLocalDateTime(),
						rs.getBoolean(6)));
		if(verificationCodes == null || verificationCodes.isEmpty()){
			return null;
		}
		return verificationCodes.get(0);
	}

	@Override
	public void createNewCode(VerificationCode code) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(con ->{
				PreparedStatement pt = getJdbcTemplate().getDataSource().getConnection().prepareStatement(insertCodeSql);
				pt.setString(1,code.getId());
				pt.setString(2,code.getTarget());
				pt.setString(3,code.getCode());
				pt.setString(4,code.getType());
				pt.setTimestamp(5,java.sql.Timestamp.valueOf(code.getExpireTime()));
				pt.setBoolean(6,code.isDisabled());
				return pt;
		},keyHolder);
	}

	@Override
	public void removeCode(String id) {
		getJdbcTemplate().update(removeCodeSql,Boolean.TRUE,id);
	}

	@Override
	public List<VerificationCode> findReuseable(String type, String target) {
		return getJdbcTemplate().query(findReuseableSql,new Object[]{target,type},(rs, rowNum) ->
				new VerificationCode(
						rs.getString(1),
						rs.getString(2),
						rs.getString(3),
						rs.getString(4),
						rs.getTimestamp(5).toLocalDateTime(),
						rs.getBoolean(6)));
	}


}
