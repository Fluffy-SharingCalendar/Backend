package com.fluffy.SharingCalendar.calendar.dto.resquest;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class UpdateEventRequestDto {

    @Size(min = 1, max = 25, message = "제목 길이는 1~25자까지 입니다.")
    private String title;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate endDate;

    private String color;

    private List<Integer> participantsIds;

    @AssertTrue(message = "시작일은 종료일보다 앞서거나 같아야 합니다.")
    public boolean isValidDateRange() {
        if(startDate != null && endDate != null) {
            return !startDate.isAfter(endDate);
        }

        return true;
    }
}
