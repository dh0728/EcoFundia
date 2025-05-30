package com.ssafy.funding.service;

import com.ssafy.funding.dto.funding.request.FundingCreateRequestDto;
import com.ssafy.funding.dto.funding.request.FundingCreateSendDto;
import com.ssafy.funding.dto.funding.request.FundingUpdateRequestDto;
import com.ssafy.funding.dto.funding.request.FundingUpdateSendDto;
import com.ssafy.funding.dto.funding.response.FundingResponseDto;
import com.ssafy.funding.dto.funding.response.FundingWishCountResponseDto;
import com.ssafy.funding.dto.funding.response.GetFundingResponseDto;
import com.ssafy.funding.dto.funding.response.MyFundingResponseDto;
import com.ssafy.funding.dto.review.response.ReviewResponseDto;
import com.ssafy.funding.dto.seller.SellerDetailResponseDto;
import com.ssafy.funding.dto.seller.response.*;
import com.ssafy.funding.entity.Funding;
import com.ssafy.funding.entity.enums.Status;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    FundingResponseDto getFunding(int fundingId);
    Funding createFunding(int sellerId, FundingCreateSendDto dto);
    Funding updateFunding(int fundingId, FundingUpdateSendDto dto);
    void deleteFunding(int fundingId);
    Status getFundingStatus(int fundingId);

    // 전체 펀딩 금액 조회
    Long getTotalFund();

    // Top 펀딩 리스트 조회
    List<GetFundingResponseDto> getTopFundingList();

    // 최신 펀딩 리스트 조회
    List<GetFundingResponseDto> getLatestFundingList(int page);

    // 카테고리별 펀딩 리스트 조회
    List<GetFundingResponseDto> getCategoryFundingList(String category, int page);

    // 펀딩 페이지 펀딩 리스트 조회
    List<GetFundingResponseDto> getFundingPageList(String sort, int page, List<String> categories);

    // 펀딩 키워드 검색 조회
    List<GetFundingResponseDto> getSearchFundingList(String sort, String keyword, int page);

    // 펀딩 검색페이지 토픽 검색 (오늘의 펀딩 마감임박, 오늘의 검색어)
    List<FundingWishCountResponseDto> getSearchSpecialFunding(String sort , String topic, int page);

    // 펀딩 상세 페이지
    GetFundingResponseDto getFundingDetail(int fundingId);

    // 브랜드 만족도 조회
    ReviewResponseDto getFundingReview(int sellerId, int page);

    // 판매자 상세페이지 판매자 정보 요청 조회
    SellerDetailResponseDto getSellerDetail(int sellerId);

    // 내가 주훔한 펀딩 조회
    List<MyFundingResponseDto> getMyFunding(List<Integer> fundingIds);
    GetSellerTotalAmountResponseDto getSellerTotalAmount(int sellerId);

    GetSellerTotalFundingCountResponseDto getSellerTotalFundingCount(int sellerId);
    GetSellerTodayOrderCountResponseDto getSellerTodayOrderCount(int sellerId);
    List<GetSellerOngoingTopFiveFundingResponseDto> getSellerOngoingTopFiveFunding(int sellerId);
    List<GetSellerOngoingFundingListResponseDto> getSellerOngoingFundingList(int sellerId, int page);
    List<GetSellerEndFundingListResponseDto> getSellerEndFundingList(int sellerId, int page);
    GetSellerFundingDetailResponseDto getSellerFundingDetail(int fundingId);
    List<GetSellerMonthAmountStatisticsResponseDto> getSellerMonthAmountStatistics(int sellerId);
    List<GetSellerFundingDetailStatisticsResponseDto> getSellerFundingDetailStatistics(int fundingId);
    List<GetSellerBrandStatisticsResponseDto> getSellerBrandStatistics(int sellerId);
    List<GetSellerTodayOrderTopThreeListResponseDto> getSellerTodayOrderTopThree(int sellerId);

    // SUCCESS 상태이며 아직 정산 완료되지 않은 펀딩 목록 조회
    List<Funding> getSuccessFundingsNotSent();

    // 펀딩 ID로 펀딩 정보 조회
    Funding getFundingById(int fundingId);

    // settlement_completed 플래그 업데이트
    void updateSettlementCompleted(int fundingId, Boolean eventSent);

    List<GetCompletedFundingsResponseDto> getCompletedFundings(int sellerId);

    GetExpectedSettlementsResponseDto getExpectedSettlements(int sellerId);

    // 주문시 currentAmount 반영 API
    void addCurrentAmount(int fundingId, int amount);


}
