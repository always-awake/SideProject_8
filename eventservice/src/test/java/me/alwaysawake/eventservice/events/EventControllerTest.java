package me.alwaysawake.eventservice.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest // MockMvc용 Mocking한 Dispatcherservlet 생성
@AutoConfigureMockMvc // MockMvc를 이용하기 위함
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void createEventTest() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 7, 7, 17, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 7, 8, 17, 00))
                .beginDateTime(LocalDateTime.of(2019, 8, 7, 17, 00))
                .endDateTime(LocalDateTime.of(2019, 8, 7, 20, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("NAVER 그린 팩토리")
                .build();

        // 입력값들을 전달하면, JSON 응답으로 201이 나오는지 확인.
        mockMvc.perform(post("/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)  // post 요청 시, content에 JSON을 담아서 보내고 있다.
                    .accept(MediaTypes.HAL_JSON) // Accept 헤더: 나는 HAL_JSON 형식의 응답을 원한다.
                    .content(objectMapper.writeValueAsString(eventDto))) // Event 객체를 Json 문자열로 바꾸어 요청 본문에 넣어줌
                .andDo(print())
                .andExpect(status().isCreated()) // 최종 상태 코드는 201을 받고 싶다.
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("id").value(Matchers.not(100)))
                .andExpect(jsonPath("free").value(Matchers.not(true)))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()));
    }

    @Test
    public void createEvent_Bad_Request_Test() throws Exception {
        Event event = Event.builder()
                .id(100L) // dto가 받을 수 없는 프로퍼티
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 7, 7, 17, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 7, 8, 17, 00))
                .beginDateTime(LocalDateTime.of(2019, 8, 7, 17, 00))
                .endDateTime(LocalDateTime.of(2019, 8, 7, 20, 00))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("NAVER 그린 팩토리")
                .free(true) // dto가 받을 수 없는 프로퍼티
                .offline(false) // dto가 받을 수 없는 프로퍼티
                .eventStatus(EventStatus.PUBLISHED) // dto가 받을 수 없는 프로퍼티
                .build();

        // 입력값들을 전달하면, JSON 응답으로 201이 나오는지 확인.
        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)  // post 요청 시, content에 JSON을 담아서 보내고 있다.
                .accept(MediaTypes.HAL_JSON) // Accept 헤더: 나는 HAL_JSON 형식의 응답을 원한다.
                .content(objectMapper.writeValueAsString(event))) // Event 객체를 Json 문자열로 바꾸어 요청 본문에 넣어줌
                .andDo(print())
                .andExpect(status().isBadRequest()); // 상태코드 400
    }

    @Test
    public void createEvent_Bad_Request_Empty_Input_Test() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto))) // 요청
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createEvent_Bad_Request_Wrong_Input_Test() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 7, 7, 17, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 7, 8, 17, 00))
                .beginDateTime(LocalDateTime.of(2019, 8, 7, 17, 00))
                .endDateTime(LocalDateTime.of(2019, 8, 6, 20, 00))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("NAVER 그린 팩토리")
                .build();

        this.mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))) // 요청
                .andExpect(status().isBadRequest());
    }
}
