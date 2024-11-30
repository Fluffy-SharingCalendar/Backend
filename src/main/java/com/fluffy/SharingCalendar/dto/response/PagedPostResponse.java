package com.fluffy.SharingCalendar.dto.response;

import com.fluffy.SharingCalendar.dto.PostDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class PagedPostResponse {
    private List<PostDetail> posts;
    private int totalPages;
    private long totalElements;
    private int currentPage;
    private int pageSize;

    public PagedPostResponse(Page<PostDetail> page) {
        this.posts = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
    }

}
