package me.alwaysawake.eventservice.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginDateTime;
    private LocalDateTime endDateTime;
    private String location; // optional, location이 없을 경우에는 온라인 모임
    private int basePrice; // optional
    private int maxPrice; // optional
    private int limitOfEnrollment;

}
