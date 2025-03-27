package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.example.DAO.entity.RestBean;
import org.example.DAO.entity.VO.responst.AuthorizeVO;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@SecurityScheme(type = SecuritySchemeType.HTTP,
                scheme = "Bearer",
                in = SecuritySchemeIn.HEADER)
@OpenAPIDefinition(security = { @SecurityRequirement(name = "Authorization")})
public class SwaggerConfiguration {

    @Bean
    public OpenAPI springShopOpenAPI(){
        return new OpenAPI()
                .info(new Info().title("实例 API 文档")
                        .description("当前处于本项目的API测试文档，在此可以快速调试接口")
                        .version("1.0.0")
                        .license(new License()
                                .name("项目开源地址")
                                .url("https://github.com/Bone-Light/Myserver")
                        )
                )
                .externalDocs(new ExternalDocumentation()
                        .description("我们的官网 -- 没有捏")
                        .url("https://baidu.com")
                );
    }

    @Bean
    public OpenApiCustomizer customizerGlobalHeaderOpenApiCustomizer() {
        return api -> this.authorizePathItems();
    }

    private Map<String, PathItem> authorizePathItems(){
        Map<String, PathItem> map = new HashMap<>();
        map.put("api/auth/login", new PathItem()
                .post(new Operation()
                        .tags(List.of("登录验证相关"))
                        .summary("登录验证接口")
                        .addParametersItem(new QueryParameter()
                                .name("username")
                                .required(true)
                        )
                        .addParametersItem(new QueryParameter()
                                .name("password")
                                .required(true)
                        )
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("OK")
                                        .content(new Content().addMediaType("*/*", new MediaType()
                                                        .example(RestBean.success(new AuthorizeVO()).asJsonString())
                                        ))
                                )
                        )
                )

    }
}
