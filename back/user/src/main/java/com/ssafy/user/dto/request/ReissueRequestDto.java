package com.ssafy.user.dto.request;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReissueRequestDto {
    private String refreshToken;
}
