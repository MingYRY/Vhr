package org.javaboy.vhr.web.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.javaboy.vhr.common.config.BaseSwaggerConfig;
import org.javaboy.vhr.common.domain.SwaggerProperties;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig extends BaseSwaggerConfig {

    @Override
    public SwaggerProperties swaggerProperties() {
        return SwaggerProperties.builder()
                .apiBasePackage("org.javaboy.vhr")
                .title("vhr后台管理系统")
                .description("vhr后台管理系统相关接口")
                .contactName("ywy")
                .contactEmail("yuwenyu_jlu@foxmail.com")
                .version("1.0")
                .enableSecurity(true)
                .build();
    }
}
