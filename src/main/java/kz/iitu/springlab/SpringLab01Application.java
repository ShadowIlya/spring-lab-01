package kz.iitu.springlab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan; // <-- Добавь этот импорт

@SpringBootApplication
@ConfigurationPropertiesScan // <-- Добавь эту аннотацию
public class SpringLab01Application {

	public static void main(String[] args) { SpringApplication.run(SpringLab01Application.class, args); }
}

