package com.greece.titan.redis.loader.dto;


import lombok.Data;

@Data
public class AppVersionDTO {
    private String osType      ;//적용OS구분코드
    private String imgId        ;//이미지 순번
    private String width        ;//해상도너비값
    private String height        ;//해상도높이값
    private String imgSrc   ;//이미지파일경로명
    private String startDt   ;//게시기간-시작일
    private String endDt   ;//게시기간-종료일
}
