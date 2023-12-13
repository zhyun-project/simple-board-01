package kim.zhyun.board.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }
    
    private Info apiInfo() {
        return new Info()
                .title("Simple Board API")
                .description("제목과 내용만 있는 게시글 CRUD 프로젝트")
                .version("1.0.0");
    }
}
