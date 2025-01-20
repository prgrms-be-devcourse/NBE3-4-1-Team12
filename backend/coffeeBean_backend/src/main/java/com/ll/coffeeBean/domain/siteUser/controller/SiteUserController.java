package com.ll.coffeeBean.domain.siteUser.controller;

import com.ll.coffeeBean.domain.order.dto.GetResPastOrderDto;
import com.ll.coffeeBean.domain.order.service.PastOrderService;
import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.service.SiteUserService;
import com.ll.coffeeBean.global.rsData.RsData;
import com.ll.coffeeBean.standard.PageDto.PageDto;
import jakarta.validation.constraints.Email;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SiteUserController {

    private final PastOrderService pastOrderService;
    private final SiteUserService siteUserService;

    record LoginDto(
            @NonNull
            @Email
            String email
    ) {
    }

    /**
     * 회원 별 지난 주문 내역 조회
     */
    @GetMapping("/history")
    @Transactional(readOnly = true)
    public RsData<PageDto<GetResPastOrderDto>> items(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                     @RequestBody LoginDto loginDto) {
        String email = loginDto.email;
        Optional<SiteUser> opSiteUser = siteUserService.findByEmail(email);
        SiteUser siteUser;

        if (opSiteUser.isEmpty()) {
            return new RsData<>(
                    "404-1",
                    "해당 이메일에 해당하는 주문 내역이 존재하지 않습니다."
            );
        }

        siteUser = opSiteUser.get();

        PageDto<GetResPastOrderDto> pageDto = pastOrderService.getList(siteUser, page, pageSize);

        return new RsData<>(
                "200-1",
                "모든 주문 내역이 조회되었습니다.",
                pageDto
        );
    }
}
