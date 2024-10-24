package com.tinyengine.it.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * The type Open api config.
 *
 * @since 2024-10-20
 */
@OpenAPIDefinition(
    info = @Info(title = "Tiny Engine API", version = "1.0.0", description = "API介绍", contact = @Contact(name = "")),
    security = @SecurityRequirement(name = "JWT"), externalDocs = @ExternalDocumentation(description = "参考文档",
    url = "https://github.com/swagger-api/swagger-core/wiki/Swagger-2.X---Annotations")

)
@SecurityScheme(type = SecuritySchemeType.HTTP, name = "JWT", scheme = "bearer", in = SecuritySchemeIn.HEADER)
public class OpenAPIConfig {

}
