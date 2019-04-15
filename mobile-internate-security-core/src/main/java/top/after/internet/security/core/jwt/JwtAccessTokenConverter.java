package top.after.internet.security.core.jwt;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import io.jsonwebtoken.impl.TextCodec;
import top.after.internet.security.core.user.UserAccount;

public class JwtAccessTokenConverter {
	public static final String ROLE="role";
    private Clock clock = DefaultClock.INSTANCE;
    private static Map<String,GrantedAuthority> flyway = new ConcurrentHashMap<>();
    private String secret;
    private Long expiration;

    public JwtAccessTokenConverter(String secret,Long expiration) {
    	this.secret = secret;
    	this.expiration = expiration;
    }
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public UserAccount getUserFormClaims(Claims claims,Collection<? extends GrantedAuthority> authorities) {
    	return new UserAccount(Integer.parseInt(claims.getId()), claims.getSubject(), claims.getSubject(), true, authorities);
    }
    
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(clock.now());
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(UserAccount userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails);
    }

    private String doGenerateToken(Map<String, Object> claims, UserAccount userDetails) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);
        Set<String> roles = AuthorityUtils.authorityListToSet(userDetails.getAuthorities());
        return Jwts.builder()
            .setClaims(claims)
            .setId(userDetails.getId().toString())
            .setSubject(userDetails.getUsername())
            .setIssuedAt(createdDate)
            .setExpiration(expirationDate)
            .claim(ROLE, roles)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getIssuedAtDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
            && (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        final Date createdDate = clock.now();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
            .setClaims(claims)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }


    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + expiration * 1000);
    }
	@SuppressWarnings("unchecked")
	public  Set<GrantedAuthority> getAuthoritiesFromClaims(Claims claims) {
		Collection<String> r = (Collection<String>)claims.get(ROLE);
		if(r == null || r.isEmpty()) {
			return null;
		}
		
		Set<GrantedAuthority> auth = r.stream().filter(Objects::nonNull).map(this::get).collect(Collectors.toSet());
		if(auth.isEmpty()) {
			return null;
		}
		return auth;
	}

	public  GrantedAuthority get(String role) {
		GrantedAuthority auth = flyway.get(role);
		if(auth==null) {
			auth = new SimpleGrantedAuthority(role);
			flyway.putIfAbsent(role, auth);
		}
		return auth;
	}

    public Long getExpiration() {
        return expiration;
    }
    
    public static void main(String[] args) {
    	System.out.println(TextCodec.BASE64.encode("xxx"));
    	
	}
}
