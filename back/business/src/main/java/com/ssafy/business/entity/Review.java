package com.ssafy.business.entity;

import lombok.Getter;

@Getter
public class Review {

    private int reviewId;
    private int fundingId;
    private int userId;
    private int rating; //1~5점
    private String content;

}

