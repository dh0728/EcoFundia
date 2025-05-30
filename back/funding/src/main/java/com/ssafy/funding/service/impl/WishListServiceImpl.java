package com.ssafy.funding.service.impl;

import com.ssafy.funding.client.FundingClient;
import com.ssafy.funding.client.SellerClient;
import com.ssafy.funding.common.exception.CustomException;
import com.ssafy.funding.common.response.ResponseCode;
import com.ssafy.funding.dto.funding.response.UserWishlistFundingDto;
import com.ssafy.funding.entity.Funding;
import com.ssafy.funding.entity.WishList;
import com.ssafy.funding.mapper.FundingMapper;
import com.ssafy.funding.mapper.WishListMapper;
import com.ssafy.funding.service.WishListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListMapper wishListMapper;
    private final FundingMapper fundingMapper;
    private final SellerClient sellerClient;

    @Override
    public void createWish(int userId, int fundingId) {
        if (wishListMapper.existsByUserIdAndFundingId(userId, fundingId)) throw new CustomException(ResponseCode.WISHLIST_ALREADY_EXISTS);
        wishListMapper.createWish(WishList.createWish(userId, fundingId));
    }

    @Override
    public List<UserWishlistFundingDto> getOngoingWishlist(int userId) {
        return getWishlist(userId, this::filterOngoing);
    }

    @Override
    public List<UserWishlistFundingDto> getDoneWishlist(int userId) {
        return getWishlist(userId, this::filterDone);
    }

    @Override
    public void deleteWish(int userId, int fundingId) {
        if (!wishListMapper.existsByUserIdAndFundingId(userId, fundingId))  throw new CustomException(ResponseCode.WISHLIST_NOT_FOUND);
        wishListMapper.deleteWish(userId, fundingId);
    }

    @Override
    public List<Integer> getWishListFundingIds(int userId) {
        return wishListMapper.findFundingIdsByUserId(userId);
    }

    private List<UserWishlistFundingDto> getWishlist(int userId, Function<List<Funding>, List<Funding>> filter) {
        List<Integer> fundingIds = wishListMapper.findFundingIdsByUserId(userId);
        if (fundingIds.isEmpty()) return List.of();

        List<Funding> fundings = fundingMapper.findFundingsByIds(fundingIds);
        List<Funding> filteredFundings = filter.apply(fundings);
        if (filteredFundings.isEmpty()) return List.of();

        Map<Integer, String> sellerNames = getSellerNames(filteredFundings);
        return convertToDtos(filteredFundings, sellerNames);
    }

    private List<Funding> filterOngoing(List<Funding> fundings) {
        return fundings.stream()
                .filter(f -> f.getEndDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    private List<Funding> filterDone(List<Funding> fundings) {
        return fundings.stream()
                .filter(f -> f.getEndDate().isBefore(LocalDateTime.now()) || f.getEndDate().isEqual(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    private Map<Integer, String> getSellerNames(List<Funding> fundings) {
        Set<Integer> sellerIds = fundings.stream()
                .map(Funding::getSellerId)
                .collect(Collectors.toSet());

        return sellerClient.getSellerNames(new ArrayList<>(sellerIds));
    }

    private List<UserWishlistFundingDto> convertToDtos(List<Funding> fundings, Map<Integer, String> sellerNames) {
        return fundings.stream()
                .map(funding -> {
                    String sellerName = sellerNames.getOrDefault(funding.getSellerId(), "알 수 없음");
                    return UserWishlistFundingDto.from(funding, sellerName);
                })
                .collect(Collectors.toList());
    }
}
