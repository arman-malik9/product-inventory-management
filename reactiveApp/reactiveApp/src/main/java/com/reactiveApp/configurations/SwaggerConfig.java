package com.reactiveApp.configurations;
 
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
 
@OpenAPIDefinition(info = @Info(title = "Product API with Inventory",
								description = "These APIs are for products availabilties",
								termsOfService =  "Terms and conditions applied",
								contact = @Contact(name = "Arman",
											email = "arman_a@hcltech.com",
											url = "arman.com"
										),
								license = @License(name = "arman License"),
								version = "api/v1"
								),
					servers = {
								@Server(description = "devServer", url = "http://localhost:8090"),
								@Server(description = "testServer", url = "http://localhost:8090")
								},
					security = @SecurityRequirement(name = "productSecurity")
 
)
@SecurityScheme(name = "productSecurity", 
				type = SecuritySchemeType.HTTP, 
				in = SecuritySchemeIn.HEADER,
				scheme = "bearer", 
				bearerFormat = "JWT",
				description = "This is basic secuirty")
 
public class SwaggerConfig {
 
//    @Bean
//    GroupedOpenApi api() {
//        return GroupedOpenApi.builder()
//                .group("webflux-api")
//                .pathsToMatch("/api/**")
//                .build();
//    }
}
