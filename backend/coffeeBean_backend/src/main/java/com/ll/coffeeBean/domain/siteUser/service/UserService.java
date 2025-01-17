package com.ll.coffeeBean.domain.siteUser.service;

import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import com.ll.coffeeBean.domain.siteUser.repository.UserRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public long count() {
        return userRepository.count();
    }

    public SiteUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
