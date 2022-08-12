package com.dpworld.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.Authority;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.HsCodeAuthority;

@Repository
public interface HsCodeAuthorityRepository extends JpaRepository<HsCodeAuthority, Long> {

	Optional<HsCodeAuthority> findById(Long id);

	HsCodeAuthority findByHsCodeAndAuthority(HsCode hsCode, Authority authority);

}
