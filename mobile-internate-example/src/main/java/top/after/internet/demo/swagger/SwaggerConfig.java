package top.after.internet.demo.swagger;

import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

//    @Value("${spring.profiles.api-enabled:false}")
//    private String apiEnabled;

    @Bean
    public Docket productApi() {
//        ParameterBuilder tokenPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
//        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("mobile-internate-example")
                .apiInfo(apiInfo())
                //.globalOperationParameters(pars)
                //.enable(Boolean.valueOf(apiEnabled))
                .select()
                .apis(RequestHandlerSelectors.basePackage("top"))
                .build()
                .directModelSubstitute(Date.class, String.class);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("mobile-internate-example")
                .contact(new Contact("david", "", "swordcenter@163.com"))
                .build();
    }

}