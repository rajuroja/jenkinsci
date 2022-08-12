package com.dpworld.authentication.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dpworld.authentication.model.AuthenticationRequest;

/**
 * @author INTECH Creative Services Pvt Ltd
 * 
 *         This class is used to get UserDetails from userName
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Inside loadUserbyUserName");
		UserDetails userDetail = new User(username, "", getUserRoleByName(username));

		Collection<? extends GrantedAuthority> role = userDetail.getAuthorities();
		for (GrantedAuthority userRoleDetails : role) {

			logger.info("Role is:- {}", userRoleDetails.getAuthority());

		}
		return userDetail;
	}

	public boolean checkUserCredential(AuthenticationRequest user) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return false;
	}

//	public String encryptPassword(String passwordToHash) {
//		String  securePassword = sha512Encryption(passwordToHash);
//		return securePassword;
//	}
//
//	private String sha512Encryption(String passwordToHash)
//    {
//        String generatedPassword = null;
//        try {
//        	MessageDigest md = MessageDigest.getInstance("SHA-512");
//           // md.update(salt);
//            byte[] bytes = md.digest(passwordToHash.getBytes());
//            StringBuilder sb = new StringBuilder();
//            for(int i=0; i< bytes.length ;i++)
//            {
//                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
//            }
//            generatedPassword = sb.toString();
//        } 
//        catch (NoSuchAlgorithmException e) 
//        {
//            e.printStackTrace();
//        }
//        return generatedPassword;
//    }

	public List<GrantedAuthority> getUserRoleByName(String userName) {
		// List<EUserRoleDetails> role=
		// userRoleDetailsRepository.findByUserName(userName);
		return new ArrayList<GrantedAuthority>();
	}

	public UserDetails getUserByFacilityId(String userName, String facilityId) {
		return null;

	}

}
