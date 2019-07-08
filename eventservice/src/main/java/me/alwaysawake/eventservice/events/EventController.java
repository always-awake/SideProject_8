package me.alwaysawake.eventservice.events;

import org.modelmapper.ModelMapper;
import org.springframework.validation.Errors;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Controller
@RequestMapping(value = "/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EventValidator eventValidator;

    // 스프링 4.3부터 생성자가 1개 존재하고, 생성자에서 받아올 매개 변수가 이미 Bean으로 등록되어 있다면, @AutoWired 애노테이션을 생략해도 된다.
    public EventController(EventRepository eventRepository, ModelMapper modelMapper, EventValidator eventValidator) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
        this.eventValidator = eventValidator;
    }

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        // Empty 값을 주는 것에 대한 에러 체크
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        else {
            eventValidator.validate(eventDto, errors);
            if (errors.hasErrors()) {
                return ResponseEntity.badRequest().build();
            }

            // builder()를 이용한 변환(DTO -> Event 객체)이 아닌 modelMapper를 이용한 변환
            // modelMapper를 이용한 변환 후에 반환되는 객체는 event 객체와는 다른 새로운 객체(event와 구조가 같은)이다.
            Event event = modelMapper.map(eventDto, Event.class);
            Event newEvent = eventRepository.save(event);

            // Location 헤더에 생성된 이벤트를 조회할 수 있는 URI가 담겨있는지 확인
            URI createdUri = linkTo(EventController.class).slash(newEvent.getId()).toUri();
            return ResponseEntity.created(createdUri).body(event);
        }
    }
}
