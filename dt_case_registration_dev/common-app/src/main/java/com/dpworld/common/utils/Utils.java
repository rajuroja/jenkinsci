package com.dpworld.common.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.xml.bind.JAXB;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Utils {

//	private static Logger logger = LoggerFactory.getLogger(Utils.class);

	public static Double parseDouble(String val) {
		if (val == null || val.trim().isEmpty())
			return 0d;
		try {
			return Double.parseDouble(val);
		} catch (NumberFormatException e) {
			return 0d;
		}
	}

	public static Float parseFloat(String val) {
		if (val == null || val.trim().isEmpty())
			return 0f;
		try {
			return Float.parseFloat(val);
		} catch (NumberFormatException e) {
			return 0f;
		}
	}

	public static Double doubleRoundUpto(Double val, Integer places) {
		if (val != null)
			return BigDecimal.valueOf(val).setScale(places, RoundingMode.HALF_UP).doubleValue();
		return 0.0;
	}

	public static String doubleToString(Double val) {
		return (val != null) ? ((val % 1 == 0) ? Math.round(val) + "" : val + "") : "0";
	}

	public static String objectToXml(Object object) {
		StringWriter sw = new StringWriter();
		JAXB.marshal(object, sw);
		return sw.toString();
	}

	public static String objectToJson(Object object) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			return ow.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage convertPdfByteArrayToJpgByteArray(List<BufferedImage> bufferedImages) {
		int width = bufferedImages.get(0).getWidth();
		int height = bufferedImages.stream().mapToInt(o -> o.getHeight()).sum();
		BufferedImage page = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = page.createGraphics();
		Color oldColor = g2.getColor();
		g2.setPaint(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		g2.setColor(oldColor);
		int newImageY = 0;
		for (int i = 0; i < bufferedImages.size(); i++) {
			g2.drawImage(bufferedImages.get(i), null, 0, newImageY);
			newImageY = newImageY + bufferedImages.get(i).getHeight();
		}
		g2.dispose();
		return page;
    }

	public static void main(String[] args) {
	}
}
