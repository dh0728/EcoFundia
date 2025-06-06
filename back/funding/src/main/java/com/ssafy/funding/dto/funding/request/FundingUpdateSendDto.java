package com.ssafy.funding.dto.funding.request;

import java.io.Serializable;
import java.time.LocalDateTime;

public record FundingUpdateSendDto(
        String title,
        String description,
        int price,
        int quantity,
        int targetAmount,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String category,
        String status,
        String storyFileUrl,
        String imageUrls
) implements Serializable {}