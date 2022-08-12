package com.dpworld.masterdataapp.service;

import com.dpworld.persistence.entity.Authority;
import com.dpworld.persistence.entity.HsCode;
import com.dpworld.persistence.entity.HsCodeAuthority;

public interface HsCodeAuthorityService {

	HsCodeAuthority findByHsCodeAndAuthority(HsCode hsCode, Authority authority);

	void save(HsCodeAuthority hsCodeAuthority);

}
