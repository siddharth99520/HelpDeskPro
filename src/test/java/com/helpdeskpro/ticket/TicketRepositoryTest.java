package com.helpdeskpro.ticket;

import com.helpdeskpro.shared.Category;
import com.helpdeskpro.shared.CategoryRepository;
import com.helpdeskpro.user.Role;
import com.helpdeskpro.user.User;
import com.helpdeskpro.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicketRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.url", postgres::getJdbcUrl);
        registry.add("spring.flyway.user", postgres::getUsername);
        registry.add("spring.flyway.password", postgres::getPassword);
    }

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void shouldSaveAndLoadTicket() {
        // Given
        User user = new User(UUID.randomUUID().toString(), "Test User", Role.EMPLOYEE);
        userRepository.save(user);

        Category category = new Category(UUID.randomUUID().toString(), "Network");
        categoryRepository.save(category);

        Ticket ticket = new Ticket(UUID.randomUUID().toString(), "Wi-Fi Down", "Can't connect to 5G", user, category, "HIGH");

        // When
        ticketRepository.save(ticket);
        ticketRepository.flush();

        // Then
        Ticket savedTicket = ticketRepository.findById(ticket.getId()).orElseThrow();
        assertThat(savedTicket.getTitle()).isEqualTo("Wi-Fi Down");
        assertThat(savedTicket.getCreatedBy().getName()).isEqualTo("Test User");
        assertThat(savedTicket.getVersion()).isEqualTo(0);
    }
}
