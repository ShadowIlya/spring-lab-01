package kz.iitu.springlab.config;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        @NotBlank String owner,
        @NotBlank String group,
        Mail mail
) {
    public record Mail(
            @Email String from,
            @Max(5) int retryCount,
            String timeout,
            boolean enabled
    ) {}
}