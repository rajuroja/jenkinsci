package com.dpworld.common.config;

import static com.dpworld.common.constants.DateFormates.DDMMMYYYYHHMMSS_HYPHEN;
import static com.dpworld.common.constants.DateFormates.DDMMMYYYYHHMMSS_SLASH;
import static com.dpworld.common.constants.DateFormates.DDMMYYYYHHMMSSA_HYPHEN;
import static com.dpworld.common.constants.DateFormates.DDMMYYYYHHMMSSA_SLASH;
import static com.dpworld.common.constants.DateFormates.DDMMYYYYHHMMSS_HYPHEN;
import static com.dpworld.common.constants.DateFormates.DDMMYYYYHHMMSS_SLASH;
import static com.dpworld.common.constants.DateFormates.DDMMYYYYHHMM_HYPHEN;
import static com.dpworld.common.constants.DateFormates.DDMMYYYYHHMM_SLASH;
import static com.dpworld.common.constants.DateFormates.DDMMYYYY_HYPHEN;
import static com.dpworld.common.constants.DateFormates.DDMMYYYY_SLASH;
import static com.dpworld.common.constants.DateFormates.HHMM;
import static com.dpworld.common.constants.DateFormates.YYYYMMDDHHMMSS_HYPHEN;
import static com.dpworld.common.constants.DateFormates.YYYYMMDDHHMMSS_SLASH;
import static com.dpworld.common.constants.DateFormates.YYYYMMDD_HYPHEN;
import static com.dpworld.common.constants.DateFormates.YYYYMMDD_SLASH;

import java.text.SimpleDateFormat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomConfiguration {

	@Bean(name = "simpleDateFormatDDMMYYYYHHMMSS_HYPHEN")
	public SimpleDateFormat simpleDateFormatDDMMYYYYHHMMSS_HYPHEN() {
		return new SimpleDateFormat(DDMMYYYYHHMMSS_HYPHEN);
	}

	@Bean(name = "simpleDateFormatDDMMYYYYHHMMSS_SLASH")
	public SimpleDateFormat simpleDateFormatDDMMYYYYHHMMSS_SLASH() {
		return new SimpleDateFormat(DDMMYYYYHHMMSS_SLASH);
	}

	@Bean(name = "simpleDateFormatDDMMYYYYHHMM_HYPHEN")
	public SimpleDateFormat simpleDateFormatDDMMYYYYHHMM_HYPHEN() {
		return new SimpleDateFormat(DDMMYYYYHHMM_HYPHEN);
	}

	@Bean(name = "simpleDateFormatDDMMYYYYHHMM_SLASH")
	public SimpleDateFormat simpleDateFormatDDMMYYYYHHMM_SLASH() {
		return new SimpleDateFormat(DDMMYYYYHHMM_SLASH);
	}

	@Bean(name = "simpleDateFormatDDMMYYYYHHMMSSA_HYPHEN")
	public SimpleDateFormat simpleDateFormatDDMMYYYYHHMMSSA_HYPHEN() {
		return new SimpleDateFormat(DDMMYYYYHHMMSSA_HYPHEN);
	}

	@Bean(name = "simpleDateFormatDDMMYYYYHHMMSSA_SLASH")
	public SimpleDateFormat simpleDateFormatDDMMYYYYHHMMSSA_SLASH() {
		return new SimpleDateFormat(DDMMYYYYHHMMSSA_SLASH);
	}

	@Bean(name = "simpleDateFormatDDMMMYYYYHHMMSS_HYPHEN")
	public SimpleDateFormat simpleDateFormatDDMMMYYYYHHMMSS_HYPHEN() {
		return new SimpleDateFormat(DDMMMYYYYHHMMSS_HYPHEN);
	}

	@Bean(name = "simpleDateFormatDDMMMYYYYHHMMSS_SLASH")
	public SimpleDateFormat simpleDateFormatDDMMMYYYYHHMMSS_SLASH() {
		return new SimpleDateFormat(DDMMMYYYYHHMMSS_SLASH);
	}

	@Bean(name = "simpleDateFormatYYYYMMDDHHMMSS_HYPHEN")
	public SimpleDateFormat simpleDateFormatYYYYMMDDHHMMSS_HYPHEN() {
		return new SimpleDateFormat(YYYYMMDDHHMMSS_HYPHEN);
	}

	@Bean(name = "simpleDateFormatYYYYMMDDHHMMSS_SLASH")
	public SimpleDateFormat simpleDateFormatYYYYMMDDHHMMSS_SLASH() {
		return new SimpleDateFormat(YYYYMMDDHHMMSS_SLASH);
	}

	@Bean(name = "simpleDateFormatDDMMYYYY_HYPHEN")
	public SimpleDateFormat simpleDateFormatDDMMYYYY_HYPHEN() {
		return new SimpleDateFormat(DDMMYYYY_HYPHEN);
	}

	@Bean(name = "simpleDateFormatDDMMYYYY_SLASH")
	public SimpleDateFormat simpleDateFormatDDMMYYYY_SLASH() {
		return new SimpleDateFormat(DDMMYYYY_SLASH);
	}

	@Bean(name = "simpleDateFormatHHMM")
	public SimpleDateFormat simpleDateFormatHHMM() {
		return new SimpleDateFormat(HHMM);
	}

	@Bean(name = "simpleDateFormatYYYYMMDD_HYPHEN")
	public SimpleDateFormat simpleDateFormatYYYYMMDD_HYPHEN() {
		return new SimpleDateFormat(YYYYMMDD_HYPHEN);
	}

	@Bean(name = "simpleDateFormatYYYYMMDD_SLASH")
	public SimpleDateFormat simpleDateFormatYYYYMMDD_SLASH() {
		return new SimpleDateFormat(YYYYMMDD_SLASH);
	}

}
