package me.alwaysawake.eventservice.events;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void createEventTest() throws Exception {
        // 입력값들을 전달하면, JSON 응답으로 201이 나오는지 확인.
        mockMvc.perform(
                post("/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)  // post 요청 시, content에 JSON을 담아서 보내고 있다.
                .accept(MediaTypes.HAL_JSON) // Accept 헤더: 나는 HAL_JSON 형식의 응답을 원한다.
                )
                .andExpect(status().isCreated()); // 최종 상태 코드는 201을 받고 싶다.
    }
}
