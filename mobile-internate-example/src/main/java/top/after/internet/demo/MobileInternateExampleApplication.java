package top.after.internet.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import top.after.internet.security.web.UaaWebSecurityConfig;

@SpringBootApplication
@Import({ UaaWebSecurityConfig.class})
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class MobileInternateExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MobileInternateExampleApplication.class, args);
	}
}
