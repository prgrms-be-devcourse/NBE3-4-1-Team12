package com.ll.coffeeBean.domain.siteUser.repository;

import com.ll.coffeeBean.domain.siteUser.entity.SiteUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteUserRepository extends JpaRepository<SiteUser, Long> {
}
