package com.fluffy.SharingCalendar.calendar.dto.resquest;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterEventRequestDto {

    @NotNull
    private Integer calendarId;

    @NotBlank
    @Size(min = 1, max = 25, message = "제목 길이는 1~25자까지 입니다.")
    private String title;

    @NotBlank(message = "시작일은 필수입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate startDate;

    @NotBlank(message = "종료일은 필수입니다.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    private LocalDate endDate;

    @NotBlank(message = "색상코드는 필수입니다.")
    private String color;

    private List<Integer> participantsIds;

    @AssertTrue(message = "시작일은 종료일보다 앞서거나 같아야 합니다.")
    public boolean isValidDateRange() {
        return startDate == null || endDate == null || !startDate.isAfter(endDate);
    }
}
