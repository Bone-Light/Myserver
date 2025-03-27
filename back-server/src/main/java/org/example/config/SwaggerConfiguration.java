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

/**
 * Swagger API文档配置类
 * 用于配置API文档的展示内容、安全认证方式和自定义接口信息
 */
@Configuration
/**
 * 配置JWT Bearer Token认证方式
 * type: 使用HTTP认证
 * scheme: 使用Bearer token
 * in: token在请求头中
 */
@SecurityScheme(type = SecuritySchemeType.HTTP,
                scheme = "Bearer",
                in = SecuritySchemeIn.HEADER)
/**
 * 定义API文档的全局安全要求
 * 所有接口默认需要Authorization认证
 */
@OpenAPIDefinition(security = { @SecurityRequirement(name = "Authorization")})
public class SwaggerConfiguration {

    /**
     * 配置OpenAPI基本信息
     * @return OpenAPI配置对象
     */
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

    /**
     * 自定义OpenAPI配置
     * 用于添加额外的认证相关接口
     * @return OpenApiCustomizer 自定义配置器
     */
    @Bean
    public OpenApiCustomizer customizerGlobalHeaderOpenApiCustomizer() {
        return api -> this.authorizePathItems().forEach(api.getPaths()::addPathItem);
    }

    /**
     * 创建认证相关接口的PathItem配置
     * 包括登录和退出登录接口的详细信息
     * @return 包含认证接口配置的Map
     */
    private Map<String, PathItem> authorizePathItems(){
        Map<String, PathItem> map = new HashMap<>();
        // 配置登录接口
        map.put("api/auth/login", new PathItem()
                .post(new Operation()  // 设置HTTP方法为POST
                        .tags(List.of("登录验证相关"))  // 设置接口所属的标签分组
                        .summary("登录验证接口")  // 接口的概要说明
                        .addParametersItem(new QueryParameter()  // 添加用户名参数
                                .name("username")  // 参数名
                                .required(true)  // 设置为必填参数
                        )
                        .addParametersItem(new QueryParameter()  // 添加密码参数
                                .name("password")  // 参数名
                                .required(true)  // 设置为必填参数
                        )
                        .responses(new ApiResponses()  // 配置接口响应
                                .addApiResponse("200", new ApiResponse()  // 添加200成功响应
                                        .description("OK")  // 响应说明
                                        .content(new Content().addMediaType("*/*", new MediaType()  // 设置响应内容类型
                                                        .example(RestBean.success(new AuthorizeVO()).asJsonString())  // 响应示例
                                        ))
                                )
                        )
                )
        );

        // 配置退出登录接口
        map.put("api/auth/logout", new PathItem()
                .get(new Operation()  // 设置HTTP方法为GET
                        .tags(List.of("登录验证相关"))  // 设置接口所属的标签分组
                        .summary("退出登录接口")  // 接口的概要说明
                        .responses(new ApiResponses()  // 配置接口响应
                                .addApiResponse("200", new ApiResponse()  // 添加200成功响应
                                        .description("OK")  // 响应说明
                                        .content(new Content().addMediaType("*/*", new MediaType()  // 设置响应内容类型
                                                .example(RestBean.success(new AuthorizeVO()).asJsonString())  // 响应示例
                                        ))
                                )
                        )
                )
        );
        return map;
    }
}
