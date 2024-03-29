package me.alwaysawake.eventservice.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor // 자바빈 규약을 지키기 위해 기본 생성자와 AllArgs 생성자를 만들어 준다.
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id @GeneratedValue
    private Long id;
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
    private boolean offline;
    private boolean free;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;

}
