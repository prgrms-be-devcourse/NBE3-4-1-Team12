package com.ll.coffeeBean.domain.siteUser.dto;

package com.ll.coffeeBean.domain.siteUser.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CustomerDto {
    @Email(message = "올바른 이메일 주소를 입력해야 합니다.")
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    private String email; // email

    @NotBlank(message = "주소는 필수 입력 항목입니다.")
    private String address; // 주소

    @NotBlank(message = "우편번호는 필수 입력 항목입니다.")
    private String postcode; // 우편번호
}
