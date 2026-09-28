package kz.iitu.springlab.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Scope("prototype") // <-- Говорим Spring, что каждый раз нужен новый объект
public class Ticket {
    private final String ticketId = UUID.randomUUID().toString().substring(0, 8);

    public String getTicketId() {
        return ticketId;
    }
}