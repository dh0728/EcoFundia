package com.order.client;

import com.order.dto.funding.request.AddCurrentAmountRequestDto;
import com.order.dto.funding.response.FundingResponseDto;
import com.order.dto.funding.response.IsOngoingResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "funding" )
public interface FundingClient {

    @GetMapping("api/funding/is-ongoing/{fundingId}")
    IsOngoingResponseDto isOngoing(@PathVariable("fundingId") int fundingId);

    @GetMapping("api/funding/my/funding")
    List<FundingResponseDto> getMyFunding(@RequestParam("fundingIds") List<Integer> fundingIds);

    @GetMapping("api/funding/total-fund")
    Long getTotalFund();

    @PostMapping("api/funding/current/amount")
    void addCurrentAmount(@RequestBody AddCurrentAmountRequestDto addCurrentAmountRequestDto);
}
