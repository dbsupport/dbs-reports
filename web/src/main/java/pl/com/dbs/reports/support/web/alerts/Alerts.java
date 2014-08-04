/**
 * 
 */
package pl.com.dbs.reports.support.web.alerts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Uzywane do umieszczania komunikatow roznych typow we flashredirectie;
 * Wyswietlenie w alerts.jsp
 * 
 * ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
 * HttpSession session = attr.getRequest().getSession();
 * 
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2013
 */
@Component("support.web.alerts")
public class Alerts {
	/**
	 * Success messages - green background
	 */
	final static String SUCCESSES_KEY = "successes";
	/**
	 * Error messages - red background
	 */
	final static String ERRORS_KEY = "errors";
	/**
	 * Warning messages - yellow background
	 */
	final static String WARNINGS_KEY = "warnings";
	/**
	 * Info messages - blue background
	 */
	final static String INFOS_KEY = "infos";
	
	@Autowired private MessageSource messageSource;
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem SUCCESSES_KEY
	 */
//	public void addSuccess(RedirectAttributes ra, String code, String... args) {
//		addAlert(ra, SUCCESSES_KEY, code, args);
//	}
//	
//	/**
//	 * Umiesc komunikat spod podanego klucza 
//	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
//	 * w liscie w Redirect Attribute pod kluczem SUCCESSES_KEY
//	 */	
//	public void addSuccess(HttpServletRequest request, String code, String... args) {
//		addAlert(request, SUCCESSES_KEY, code, args);
//	}
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w HttpSession pod kluczem SUCCESSES_KEY
	 */	
	public void addSuccess(HttpSession session, String code, String... args) {
		addAlert(session, SUCCESSES_KEY, code, args);
	}	
	
//	/**
//	 * Umiesc komunikat spod podanego klucza 
//	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
//	 * w liscie w Redirect Attribute pod kluczem ERRORS_KEY.
//	 */
//	public void addError(RedirectAttributes ra, String code, String... args) {
//		addAlert(ra, ERRORS_KEY, code, args);
//	}
//
//	/**
//	 * Umiesc komunikat spod podanego klucza 
//	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
//	 * w liscie w Redirect Attribute pod kluczem ERRORS_KEY.
//	 */
//	public void addError(HttpServletRequest request, String code, String... args) {
//		addAlert(request, ERRORS_KEY, code, args);
//	}
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w HttpSession pod kluczem ERRORS_KEY.
	 */
	public void addError(HttpSession session, String code, String... args) {
		addAlert(session, ERRORS_KEY, code, args);
	}	
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem WARNINGS_KEY.
	 */
//	public void addWarning(RedirectAttributes ra, String code, String... args) {
//		addAlert(ra, WARNINGS_KEY, code, args);
//	}
//	
//	/**
//	 * Umiesc komunikat spod podanego klucza 
//	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
//	 * w liscie w Redirect Attribute pod kluczem WARNINGS_KEY.
//	 */
//	public void addWarning(HttpServletRequest request, String code, String... args) {
//		addAlert(request, WARNINGS_KEY, code, args);
//	}
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w HttpSession pod kluczem WARNINGS_KEY.
	 */	
	public void addWarning(HttpSession session, String code, String... args) {
		addAlert(session, WARNINGS_KEY, code, args);
	}	
	
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem INFOS_KEY.
	 */
//	public void addInfo(RedirectAttributes ra, String code, String... args) {
//		addAlert(ra, INFOS_KEY, code, args);
//	}	
//	
//	/**
//	 * Umiesc komunikat spod podanego klucza 
//	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
//	 * w liscie w Redirect Attribute pod kluczem INFOS_KEY.
//	 */
//	public void addInfo(HttpServletRequest request, String code, String... args) {
//		addAlert(request, INFOS_KEY, code, args);
//	}	
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w HttpSession pod kluczem INFOS_KEY.
	 */
	public void addInfo(HttpSession session, String code, String... args) {
		addAlert(session, INFOS_KEY, code, args);
	}		
	
//	/**
//	 * Adds messages into collectio.
//	 * Only if given msg/code is no empty.
//	 */
//	private void addAlert(RedirectAttributes ra, String key, String code, String... args) {
//		String msg = messageSource.getMessage(code, args, code, null);
//		msg = StringUtils.isBlank(msg)?code:msg;
//		if (!StringUtils.isBlank(msg)) {
//			List<String> alerts = retreiveCollection(ra, key);
//			alerts.add(msg);
//		}
//	}
//	
//	private void addAlert(HttpServletRequest request, String key, String code, String... args) {
//		String msg = messageSource.getMessage(code, args, code, null);
//		msg = StringUtils.isBlank(msg)?code:msg;
//		if (!StringUtils.isBlank(msg)) {
//			List<String> alerts = retreiveCollection(request, key);
//			alerts.add(msg);
//		}
//	}
	
	private void addAlert(HttpSession session, String key, String code, String... args) {
		String msg = messageSource.getMessage(code, args, code, null);
		msg = StringUtils.isBlank(msg)?code:msg;
		if (!StringUtils.isBlank(msg)) {
			List<String> alerts = retreiveCollection(session, key);
			alerts.add(msg);
		}
	}	
	
	/**
	 * Finds given collection key in RedirectAttributes and returns;
	 * If not found returns empty which is already in RedirectAttributes.
	 */
	@SuppressWarnings("unchecked")
	static List<String> retreiveCollection(RedirectAttributes ra, String key) {
		Map<String, ?> attributes = ra.getFlashAttributes();
		if (attributes != null) {
			for (Map.Entry<String, ?> attr : attributes.entrySet()) {
				if (key.equalsIgnoreCase(attr.getKey())) {
					return (List<String>)attr.getValue();
				}
			}
		}
		//..no messages yet? create new..
		List<String> alerts = new ArrayList<String>();
		ra.addFlashAttribute(key, alerts);
		return alerts;
	}
	
	@SuppressWarnings("unchecked")
	static List<String> retreiveCollection(ServletRequest request, String key) {
		List<String> alerts = request.getAttribute(key)==null?new ArrayList<String>():(List<String>)request.getAttribute(key);
		request.setAttribute(key, alerts);
		return alerts;
	}
	
	@SuppressWarnings("unchecked")
	static List<String> retreiveCollection(HttpSession session, String key) {
		List<String> alerts = session.getAttribute(key)==null?new ArrayList<String>():(List<String>)session.getAttribute(key);
		session.setAttribute(key, alerts);
		return alerts;
	}	
	
}
