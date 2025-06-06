package com.ssafy.funding.dto.funding.response;

import com.ssafy.funding.entity.Funding;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public record UserWishlistFundingDto(
        int fundingId,
        String title,
        String imageUrl,
        int rate,
        int remainingDays,
        int amountGap,
        String sellerName
) {
    public static UserWishlistFundingDto from(Funding funding, String sellerName) {
        int targetAmount = funding.getTargetAmount();
        int rate = targetAmount == 0 ? 0 :
                (int) ((double) funding.getCurrentAmount() / targetAmount * 100);

        int remainingDays = (int) ChronoUnit.DAYS.between(LocalDateTime.now(), funding.getEndDate());
        int amountGap = funding.getCurrentAmount() - targetAmount;

        List<String> imageUrls = funding.getImageUrlList();
        String imageUrl = (imageUrls == null || imageUrls.isEmpty())
                ? "https://ssafy-funding.s3.ap-northeast-2.amazonaws.com/funding/images/1743560407748_%EB%8A%90%EC%A2%8B%EC%9A%B0%EB%A6%AC%EC%A1%B0.png"
                : imageUrls.get(0);

        return new UserWishlistFundingDto(
                funding.getFundingId(),
                funding.getTitle(),
                imageUrl,
                rate,
                remainingDays,
                amountGap,
                sellerName
        );
    }
}
