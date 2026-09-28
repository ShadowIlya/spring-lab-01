package kz.iitu.springlab.config;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!dev & !prod") // Активен, когда не выбраны ни dev, ни prod
public class DefaultBanner implements EnvironmentBanner {
    @Override
    public String getBanner() {
        return "no profile is active";
    }
}
