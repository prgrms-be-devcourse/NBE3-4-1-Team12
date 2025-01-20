package com.ll.coffeeBean.domain.siteUser.service;

import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SiteUserRepository siteUserRepository;


    public long count() {
        return siteUserRepository.count();
    }

    public SiteUser findByEmail(String email) {

        return siteUserRepository.findByEmail(email).orElseThrow();
    }


    @Transactional
    public SiteUser create(String address,String email,String postCode) {
        SiteUser siteUser=new SiteUser();
        siteUser.setAddress(address);
        siteUser.setEmail(email);
        siteUser.setPostCode(postCode);
        siteUserRepository.save(siteUser);

        return siteUser;
    }
}
