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
<<<<<<< HEAD


    private final SiteUserRepository siteUserRepository;

    public Optional<SiteUser> findByEmail(String email) {
        return siteUserRepository
                .findByEmail(email);

=======
    private final SiteUserRepository siteUserRepository;

    public Optional<SiteUser> findByEmail(String email) {
        return siteUserRepository.findByEmail(email);
>>>>>>> 942f7bb4dda87e5b85e542f3d40ab73c89a88b1e
    }

    @Transactional
    public SiteUser create(String address,String email,String postCode) {
<<<<<<< HEAD

        SiteUser siteUser = new SiteUser();

=======
        SiteUser siteUser = new SiteUser();
>>>>>>> 942f7bb4dda87e5b85e542f3d40ab73c89a88b1e
        siteUser.setAddress(address);
        siteUser.setEmail(email);
        siteUser.setPostCode(postCode);
        siteUserRepository.save(siteUser);

        return siteUser;
    }
}