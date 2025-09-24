package com.fpt_be.fpt_be.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto {
    private Long id;
    private String title;
    private String content_short;
    private String content_long;
    private String image;
    private String status;
    private String author;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayCapNhat;
}
