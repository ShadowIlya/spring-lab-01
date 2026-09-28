package kz.iitu.springlab.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("prod")
public class ProdBanner implements EnvironmentBanner {
    @Override
    public String getBanner() {
        return "PRODUCTION: handle with care";
    }
}