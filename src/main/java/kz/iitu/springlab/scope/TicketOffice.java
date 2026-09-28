package kz.iitu.springlab.scope;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class TicketOffice {

    // Внедряем провайдер вместо самого объекта напрямую
    private final ObjectProvider<Ticket> ticketProvider;

    public TicketOffice(ObjectProvider<Ticket> ticketProvider) {
        this.ticketProvider = ticketProvider;
    }

    public String getNewTicketId() {
        // Каждый вызов .getObject() создает абсолютно новый билет (Prototype)
        Ticket ticket = ticketProvider.getObject();
        return ticket.getTicketId();
    }

    // Метод для эндпоинта /scopes, используемый в Lab2Controller
    public Map<String, Object> demo() {
        Ticket t1 = ticketProvider.getObject();
        Ticket t2 = ticketProvider.getObject();
        return Map.of(
                "ticket1", t1.getTicketId(),
                "ticket2", t2.getTicketId(),
                "areTheyEqual", t1.equals(t2)
        );
    }
}