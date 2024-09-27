package com.fluffy.SharingCalendar.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /* 400 BAD_REQUEST 잘못된 요청 */
    INVALID_PARAMETER(BAD_REQUEST, "파라미터 값을 확인해주세요."),

    /* 403 FORBIDDEN 권한 없음 */
    NO_PERMISSION_FOR_MODIFICATION(FORBIDDEN, "해당 작업의 수정/삭제 권한이 없습니다."),

    /* 404 NOT_FOUND 잘못된 리소스 접근 */
    CALENDAR_NOT_FOUND(NOT_FOUND, "해당 캘린더를 찾을 수 없습니다."),
    EVENT_NOT_FOUND(NOT_FOUND, "해당 일정을 찾을 수 없습니다."),
    POST_NOT_FOUND(NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(NOT_FOUND, "해당 댓글을 찾을 수 없습니다."),

    /* 409 CONFLICT 중복된 리소스 */
    ALREADY_SAVED_DISPLAY(CONFLICT, "이미 존재하는 닉네임입니다."),

    /* 500 INTERNAL SERVER ERROR */
    UNSUCCESSFUL_UPLOAD(INTERNAL_SERVER_ERROR, "이미지 파일 업로드에 실패했습니다."),
    SERVER_ERROR(INTERNAL_SERVER_ERROR, "서버 에러입니다. 관리자 이메일로 연락주세요!");

    private final HttpStatus httpStatus;
    private final String message;
}

