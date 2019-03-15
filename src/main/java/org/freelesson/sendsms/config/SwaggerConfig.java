package org.freelesson.sendsms.config;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	private static String CLIENT_ID= "";
	private static String CLIENT_SECRET= "";
	private static String AUTH_SERVER= "http://localhost:8080";
	@Bean
	public Docket api() {
		List<ResponseMessage> responses = new ArrayList<>();
		responses.add(new ResponseMessageBuilder().code(500).message("500 message")
				.responseModel(new ModelRef("JaxRsErrorResponse")).build());
		responses.add(new ResponseMessageBuilder().code(403).message("Forbidden!!!!").build());
		responses.add(new ResponseMessageBuilder().code(401).message("Unauthorized!!!!").build());
		Docket docket = new Docket(DocumentationType.SWAGGER_2).groupName("barba-api").select()
				.apis(RequestHandlerSelectors.basePackage("org.freelesson.sendsms.domain.controller"))
				.paths(PathSelectors.any())
				.build().useDefaultResponseMessages(false).globalResponseMessage(RequestMethod.GET, responses)
				.securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContext()));
		docket.directModelSubstitute(LocalDate.class, java.sql.Date.class);
		docket.directModelSubstitute(LocalDateTime.class, java.util.Date.class);
		docket.directModelSubstitute(OffsetDateTime.class, java.util.Date.class);

		docket.ignoredParameterTypes(AuthenticationPrincipal.class);
		docket.apiInfo(apiInfo());
		return docket;
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Barba API")
				.description("Barba API.")
				.termsOfServiceUrl("").contact(new Contact("Free Lesssons Limited", "https://github.com/muraguri2005", "muraguri2005@gmail.com")).version("0.0.1").build();
		
	}
	
	private SecurityContext securityContext() {
	    return SecurityContext.builder()
	      .securityReferences(
	        Arrays.asList(new SecurityReference("spring_oauth", scopes())))
	      .forPaths(PathSelectors.any())
	      .build();
	}
	private AuthorizationScope[] scopes() {
	    AuthorizationScope[] scopes = { 
	      new AuthorizationScope("read", "for read operations"), 
	      new AuthorizationScope("write", "for write operations"), 
	      new AuthorizationScope("foo", "Access foo API") };
	    return scopes;
	}
	
	@Bean
	public SecurityConfiguration security() {
	    return SecurityConfigurationBuilder.builder()
	        .clientId(CLIENT_ID)
	        .clientSecret(CLIENT_SECRET)
	        .scopeSeparator(" ")
	        .useBasicAuthenticationWithAccessCodeGrant(true)
	        .build();
	}
	private SecurityScheme securityScheme() {
	    GrantType grantType = new AuthorizationCodeGrantBuilder()
	        .tokenEndpoint(new TokenEndpoint(AUTH_SERVER + "/oauth/token", "token"))
	        .tokenRequestEndpoint(
	          new TokenRequestEndpoint(AUTH_SERVER + "/authorize", CLIENT_ID, CLIENT_ID))
	        .build();
	 
	    SecurityScheme oauth = new OAuthBuilder().name("spring_oauth")
	        .grantTypes(Arrays.asList(grantType))
	        .scopes(Arrays.asList(scopes()))
	        .build();
	    return oauth;
	}

}