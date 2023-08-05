package com.korotin.tasker.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@Configuration
public class BeanConfiguration {

    @Bean(name = "adminHttpBasic")
    public RequestPostProcessor adminHttpBasic() {
        return httpBasic("test@test.com", "qwerty");
    }

}
