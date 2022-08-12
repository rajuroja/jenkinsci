package com.dpworld.persistence.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.dpworld.persistence.entity.UserDetails;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {

	UserDetails findByUsernameAndCompanyIdAndLicenseNumber(String username, String companyId, String licenseNumber);
}
