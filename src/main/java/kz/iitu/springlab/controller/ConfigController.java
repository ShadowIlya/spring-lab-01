package kz.iitu.springlab.controller;

import kz.iitu.springlab.config.AppProperties;
import kz.iitu.springlab.config.EnvironmentBanner;
import kz.iitu.springlab.scope.TicketOffice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {

    private final AppProperties appProperties;
    private final EnvironmentBanner environmentBanner;
    private final TicketOffice ticketOffice;

    // Внедряем все зависимости через конструктор
    public ConfigController(AppProperties appProperties,
                            EnvironmentBanner environmentBanner,
                            TicketOffice ticketOffice) {
        this.appProperties = appProperties;
        this.environmentBanner = environmentBanner;
        this.ticketOffice = ticketOffice;
    }

    // Эндпоинт для настроек и баннера (Lab 3)
    @GetMapping("/config")
    public String showConfig() {
        return "Владелец проекта: " + appProperties.owner() +
                "<br>Группа: " + appProperties.group() +
                "<br>Почта для отправки: " + appProperties.mail().from() +
                "<br>Попыток отправки: " + appProperties.mail().retryCount() +
                "<br>Баннер окружения: " + environmentBanner.getBanner();
    }

    // Эндпоинт для проверки Prototype скоупа билетов
    @GetMapping("/scope-test")
    public String testScope() {
        return "Ticket 1: " + ticketOffice.getNewTicketId() + "<br>" +
                "Ticket 2: " + ticketOffice.getNewTicketId() + "<br>" +
                "Ticket 3: " + ticketOffice.getNewTicketId();
    }
}