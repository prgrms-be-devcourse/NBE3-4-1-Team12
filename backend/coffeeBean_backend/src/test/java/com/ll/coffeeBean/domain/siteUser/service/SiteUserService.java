package com.ll.coffeeBean.domain.siteUser.service;

import com.ll.coffeeBean.domain.order.service.PastOrderService;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class SiteUserService {

    @Autowired
    private PastOrderService pastOrderService;

    @Autowired
    private SiteUserRepository siteUserRepository;
}
