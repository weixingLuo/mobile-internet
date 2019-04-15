package top.after.internet.security.core.jwt;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import top.after.internet.security.core.user.UserAccount;

public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static String BEARER_TYPE = "Bearer";
    
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	public JwtAuthenticationProcessingFilter(JwtAccessTokenConverter jwtAccessTokenConverter) {
		this.jwtAccessTokenConverter = jwtAccessTokenConverter;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.debug("processing authentication for '{}'", request.getRequestURL());
		String token = extractHeaderToken(request);
        if (token == null) {
        	logger.warn("couldn't find bearer string, will ignore the header");
        	filterChain.doFilter(request, response);
        	return;
        } 
        Claims claims=null ;
        try {
        	claims = jwtAccessTokenConverter.getAllClaimsFromToken(token);
        } catch (IllegalArgumentException e) {
        	logger.error("an error occured during getting username from token", e);
        } catch (ExpiredJwtException e) {
        	logger.warn("the token is expired and not valid anymore", e);
        }
        if(claims != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        	Collection<? extends GrantedAuthority> authorities=jwtAccessTokenConverter.getAuthoritiesFromClaims(claims);
			UserAccount userDetails = jwtAccessTokenConverter.getUserFormClaims(claims, authorities);
        	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            logger.info("authorizated user '{}', setting security context", authentication.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
	}

	protected String extractHeaderToken(HttpServletRequest request) {
		Enumeration<String> headers = request.getHeaders("Authorization");
		while (headers.hasMoreElements()) { // typically there is only one (most servers enforce that)
			String value = headers.nextElement();
			if ((value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase()))) {
				String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();
				int commaIndex = authHeaderValue.indexOf(',');
				if (commaIndex > 0) {
					authHeaderValue = authHeaderValue.substring(0, commaIndex);
				}
				return authHeaderValue;
			}
		}

		return null;
	}

}
