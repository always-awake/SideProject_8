package me.alwaysawake.eventservice.events;

import org.springframework.validation.Errors;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors) {
        if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
            errors.rejectValue("basePrice", "wrongValue", "Base Price is Wrong!!");
            errors.rejectValue("maxPrice", "wrongValue", "Max Price is Wrong!!");
        }

        LocalDateTime endDateTime = eventDto.getEndDateTime();
        if (endDateTime.isBefore(eventDto.getBeginDateTime()) ||
        endDateTime.isBefore(eventDto.getBeginDateTime()) ||
        endDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
        endDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())) {
            errors.rejectValue("endDateTime", "wrongValue", "End DateTime is Wrong!!");
        }

        // TODO BeginDateTime
        // TODO CloseEnrollmentDateTime
    }
}
