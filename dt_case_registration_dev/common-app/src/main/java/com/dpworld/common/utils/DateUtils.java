package com.dpworld.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.dpworld.common.constants.DateFormates.*;

@Component
public class DateUtils {

	@Autowired
	@Qualifier("simpleDateFormatDDMMYYYYHHMMSS_HYPHEN")
	private SimpleDateFormat simpleDateFormatDDMMYYYYHHMMSS_HYPHEN;

	@Autowired
	@Qualifier("simpleDateFormatDDMMYYYYHHMMSS_SLASH")
	private SimpleDateFormat simpleDateFormatDDMMYYYYHHMMSS_SLASH;

	@Autowired
	@Qualifier("simpleDateFormatDDMMYYYYHHMM_HYPHEN")
	private SimpleDateFormat simpleDateFormatDDMMYYYYHHMM_HYPHEN;

	@Autowired
	@Qualifier("simpleDateFormatDDMMYYYYHHMM_SLASH")
	private SimpleDateFormat simpleDateFormatDDMMYYYYHHMM_SLASH;

	@Autowired
	@Qualifier("simpleDateFormatDDMMYYYYHHMMSSA_HYPHEN")
	private SimpleDateFormat simpleDateFormatDDMMYYYYHHMMSSA_HYPHEN;
	
	@Autowired
	@Qualifier("simpleDateFormatDDMMYYYYHHMMSSA_SLASH")
	private SimpleDateFormat simpleDateFormatDDMMYYYYHHMMSSA_SLASH;
	
	@Autowired
	@Qualifier("simpleDateFormatDDMMMYYYYHHMMSS_HYPHEN")
	private SimpleDateFormat simpleDateFormatDDMMMYYYYHHMMSS_HYPHEN;

	@Autowired
	@Qualifier("simpleDateFormatDDMMMYYYYHHMMSS_SLASH")
	private SimpleDateFormat simpleDateFormatDDMMMYYYYHHMMSS_SLASH;

	@Autowired
	@Qualifier("simpleDateFormatYYYYMMDDHHMMSS_HYPHEN")
	private SimpleDateFormat simpleDateFormatYYYYMMDDHHMMSS_HYPHEN;

	@Autowired
	@Qualifier("simpleDateFormatYYYYMMDDHHMMSS_SLASH")
	private SimpleDateFormat simpleDateFormatYYYYMMDDHHMMSS_SLASH;

	@Autowired
	@Qualifier("simpleDateFormatDDMMYYYY_HYPHEN")
	private SimpleDateFormat simpleDateFormatDDMMYYYY_HYPHEN;
	
	@Autowired
	@Qualifier("simpleDateFormatDDMMYYYY_SLASH")
	private SimpleDateFormat simpleDateFormatDDMMYYYY_SLASH;
	
	@Autowired
	@Qualifier("simpleDateFormatHHMM")
	private SimpleDateFormat simpleDateFormatHHMM;
	
	@Autowired
	@Qualifier("simpleDateFormatYYYYMMDD_HYPHEN")
	private SimpleDateFormat simpleDateFormatYYYYMMDD_HYPHEN;
	
	@Autowired
	@Qualifier("simpleDateFormatYYYYMMDD_SLASH")
	private SimpleDateFormat simpleDateFormatYYYYMMDD_SLASH;
	
	private static final Logger logger = LoggerFactory.getLogger(DateUtils.class);

	public ZonedDateTime getZonedDateTime(Instant instant, String timezone) {
		ZoneId zoneIDFacility = ZoneId.of(timezone);
		return ZonedDateTime.ofInstant(instant, zoneIDFacility);
	}

	public String convertZonedDateIntoString(ZonedDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DDMMMYYYYHHMMSS_HYPHEN);
		return date.format(formatter);
	}

	public Date stringToDate(String date, String format) {
		//logger.info("ENTRY :: convertStringToDate : Parse String " + date + " to Date in " + format + " format");
		if (date != null && !date.trim().isEmpty()) {
			try {
				if (format.equals(DDMMYYYYHHMMSS_HYPHEN)) {
					return simpleDateFormatDDMMYYYYHHMMSS_HYPHEN.parse(date);
				}
				if (format.equals(DDMMYYYYHHMMSS_SLASH)) {
					return simpleDateFormatDDMMYYYYHHMMSS_SLASH.parse(date);
				}
				if (format.equals(DDMMYYYYHHMM_HYPHEN)) {
					return simpleDateFormatDDMMYYYYHHMM_HYPHEN.parse(date);
				}
				if (format.equals(DDMMYYYYHHMM_SLASH)) {
					return simpleDateFormatDDMMYYYYHHMM_SLASH.parse(date);
				}
				if (format.equals(DDMMYYYYHHMMSSA_HYPHEN)) {
					return simpleDateFormatDDMMYYYYHHMMSSA_HYPHEN.parse(date);
				}
				if (format.equals(DDMMYYYYHHMMSSA_SLASH)) {
					return simpleDateFormatDDMMYYYYHHMMSSA_SLASH.parse(date);
				}
				if (format.equals(DDMMMYYYYHHMMSS_HYPHEN)) {
					return simpleDateFormatDDMMMYYYYHHMMSS_HYPHEN.parse(date);
				}
				if (format.equals(DDMMMYYYYHHMMSS_SLASH)) {
					return simpleDateFormatDDMMMYYYYHHMMSS_SLASH.parse(date);
				}
				if (format.equals(YYYYMMDDHHMMSS_HYPHEN)) {
					return simpleDateFormatYYYYMMDDHHMMSS_HYPHEN.parse(date);
				}
				if (format.equals(YYYYMMDDHHMMSS_SLASH)) {
					return simpleDateFormatYYYYMMDDHHMMSS_SLASH.parse(date);
				}
				if (format.equals(DDMMYYYY_HYPHEN)) {
					return simpleDateFormatDDMMYYYY_HYPHEN.parse(date);
				}
				if (format.equals(DDMMYYYY_SLASH)) {
					return simpleDateFormatDDMMYYYY_SLASH.parse(date);
				}
				if (format.equals(HHMM)) {
					return simpleDateFormatHHMM.parse(date);
				}
				if (format.equals(YYYYMMDD_HYPHEN)) {
					return simpleDateFormatYYYYMMDD_HYPHEN.parse(date);
				}
				if (format.equals(YYYYMMDD_SLASH)) {
					return simpleDateFormatYYYYMMDD_SLASH.parse(date);
				}
			} catch (ParseException e) {
				logger.error("ERROR :: convertStringToDate : Date ParseException : {}", e);
			}
		}
		return null;
	}

	public String dateToString(Date date, String format) {
		//logger.info("ENTRY :: convertDateToString : Parse Date " + date + " to String in " + format + " format");
		if (date != null) {
			try {
				if (format.equals(DDMMYYYYHHMMSS_HYPHEN)) {
					return simpleDateFormatDDMMYYYYHHMMSS_HYPHEN.format(date);
				}
				if (format.equals(DDMMYYYYHHMMSS_SLASH)) {
					return simpleDateFormatDDMMYYYYHHMMSS_SLASH.format(date);
				}
				if (format.equals(DDMMYYYYHHMM_HYPHEN)) {
					return simpleDateFormatDDMMYYYYHHMM_HYPHEN.format(date);
				}
				if (format.equals(DDMMYYYYHHMM_SLASH)) {
					return simpleDateFormatDDMMYYYYHHMM_SLASH.format(date);
				}
				if (format.equals(DDMMYYYYHHMMSSA_HYPHEN)) {
					return simpleDateFormatDDMMYYYYHHMMSSA_HYPHEN.format(date);
				}
				if (format.equals(DDMMYYYYHHMMSSA_SLASH)) {
					return simpleDateFormatDDMMYYYYHHMMSSA_SLASH.format(date);
				}
				if (format.equals(DDMMMYYYYHHMMSS_HYPHEN)) {
					return simpleDateFormatDDMMMYYYYHHMMSS_HYPHEN.format(date);
				}
				if (format.equals(DDMMMYYYYHHMMSS_SLASH)) {
					return simpleDateFormatDDMMMYYYYHHMMSS_SLASH.format(date);
				}
				if (format.equals(YYYYMMDDHHMMSS_HYPHEN)) {
					return simpleDateFormatYYYYMMDDHHMMSS_HYPHEN.format(date);
				}
				if (format.equals(YYYYMMDDHHMMSS_SLASH)) {
					return simpleDateFormatYYYYMMDDHHMMSS_SLASH.format(date);
				}
				if (format.equals(DDMMYYYY_HYPHEN)) {
					return simpleDateFormatDDMMYYYY_HYPHEN.format(date);
				}
				if (format.equals(DDMMYYYY_SLASH)) {
					return simpleDateFormatDDMMYYYY_SLASH.format(date);
				}
				if (format.equals(HHMM)) {
					return simpleDateFormatHHMM.format(date);
				}
				if (format.equals(YYYYMMDD_HYPHEN)) {
					return simpleDateFormatYYYYMMDD_HYPHEN.format(date);
				}
				if (format.equals(YYYYMMDD_SLASH)) {
					return simpleDateFormatYYYYMMDD_SLASH.format(date);
				}
			} catch (Exception e) {
				logger.error("ERROR :: convertDateToString : Date ParseException : {}", e);
			}
		}
		return "";
	}

	public Long numberOfDaysBetween(Date date1, Date date2) {
		Long diff = date2.getTime() - date1.getTime();
		if (diff < 0)
			diff = diff * (-1);
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}

	public static void main(String[] args) {
//		Date d = convertStringToDate("13-09-2021 05:10", "dd-MM-yyyy HH:mm");
	}
}
