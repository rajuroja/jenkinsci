package com.dpworld.authentication.service;

import com.dpworld.authentication.model.AuthenticationRequest;
import com.dpworld.persistence.entity.UserDetails;

public interface UserDetailsService {

	UserDetails addOrFetchUserDetail(AuthenticationRequest authenticationRequest);

}
