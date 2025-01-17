package com.ll.coffeeBean.domain.siteUser.service;

import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.SiteUserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class SiteUserService {

    private final SiteUserRepository siteUserRepository;

    public Optional<SiteUser> findByEmail(String email) {
        return siteUserRepository
                .findByEmail(email);

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