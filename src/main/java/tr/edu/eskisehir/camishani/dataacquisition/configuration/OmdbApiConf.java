package tr.edu.eskisehir.camishani.dataacquisition.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OmdbApiConf {

    @Bean
    public RestTemplate omdbApi() {
        RestTemplateBuilder builder = new RestTemplateBuilder();

        builder.rootUri("http://www.omdbapi.com");

        return builder.build();
    }
}
