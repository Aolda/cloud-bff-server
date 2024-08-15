package com.aoldacloud.console.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@OpenAPIDefinition(
        info = @Info(
                title = "AoldaCloud API 문서",
                description = "컴퓨팅, 네트워크, 권한 관리 등을 포함한 AoldaCloud 서비스에 대한 API 문서입니다.",
                version = "v1.0"
        )
)
@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    String cookieAuthSchemeName = "AoldaCloudCookieAuth";
    String headerAuthSchemeName = "AoldaCloudHeaderAuth";

    SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList(cookieAuthSchemeName)
            .addList(headerAuthSchemeName);

    Components components = new Components()
            .addSecuritySchemes(cookieAuthSchemeName, new SecurityScheme()
                    .name(cookieAuthSchemeName)
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.COOKIE)
                    .name("X-AUTH-TOKEN")
                    .description("쿠키에 포함된 X-AUTH-TOKEN을 사용하는 AoldaCloud 인증. 이 토큰은 모든 요청에 자동으로 포함됩니다."))
            .addSecuritySchemes(headerAuthSchemeName, new SecurityScheme()
                    .name(headerAuthSchemeName)
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("X-AUTH-TOKEN")
                    .description("헤더에 포함된 Authorization 토큰을 사용하는 AoldaCloud 인증. 이 토큰은 요청 헤더에 포함되어야 합니다."));

    return new OpenAPI()
            .addSecurityItem(securityRequirement)
            .components(components);
  }

  @Bean
  public OperationCustomizer operationCustomizer() {
    return (operation, handlerMethod) -> {
      this.addResponseBodyWrapperSchemaExample(operation);
      return operation;
    };
  }

  private void addResponseBodyWrapperSchemaExample(Operation operation) {
    operation.getResponses().forEach((responseCode, apiResponse) -> {
      final Content content = apiResponse.getContent();
      if (content != null) {
        content.forEach((mediaTypeKey, mediaType) -> {
          final int status = Integer.parseInt(responseCode);
          Schema<?> originalSchema = mediaType.getSchema();
          Schema<?> wrappedSchema = wrapSchema(originalSchema, status / 200 == 1);
          mediaType.setSchema(wrappedSchema);
        });
      }
    });
  }

  private Schema<?> wrapSchema(Schema<?> originalSchema, boolean success) {
    final Schema<?> wrapperSchema = new Schema<>();

    wrapperSchema.addProperty("success", new Schema<>().type("boolean").example(success));
    wrapperSchema.addProperty("timeStamp", new Schema<>().type("string").format("date-time").example(
            LocalDateTime.now().toString()));
    wrapperSchema.addProperty("data", originalSchema);
    wrapperSchema.addProperty("error", new Schema<>().type("string").example("에러 메세지"));

    return wrapperSchema;
  }

}
