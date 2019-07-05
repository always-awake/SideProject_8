package me.alwaysawake.eventservice.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("test name")
                .description("test description")
                .build();
        assertThat(event).isNotNull();

    }

    @Test
    public void javaBean() {
        // Given
        String eventname = "Test Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(eventname);
        event.setDescription(description);

        // Then
        assertThat(event.getName()).isEqualTo(eventname);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}