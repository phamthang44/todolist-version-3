package com.greenwich.todo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> content;
    private int page;          // trang hiện tại (1-based)
    private int size;          // kích thước trang
    private long totalElements;
    private int totalPages;
    private boolean last;      // có phải trang cuối không
}
