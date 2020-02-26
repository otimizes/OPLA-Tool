package br.ufpr.dinf.gres.architecture.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @author Fernando
 *
 */
public class DateUtil {

	private static final String PATTERN_MM_SS = "mm:ss";

	public static String toMinutesAndSeconds(Date date) {
		return new SimpleDateFormat(PATTERN_MM_SS).format(date);
	}

}
