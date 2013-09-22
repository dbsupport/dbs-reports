/**
 * 
 */
package pl.com.dbs.reports.support.web.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Uzywane do umieszczania komunikatow roznych typow we flashredirectie;
 * Wyswietlenie w messages.jsp
 * 
 * ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
 * HttpSession session = attr.getRequest().getSession();
 * 
 * @author krzysztof.kaziura
 *
 */
@Component("support.web.messages")
public class WebMessages {
	/**
	 * Success messages - green background
	 */
	private final static String SUCCESS_KEY = "ebsuccess";
	/**
	 * Error messages - red background
	 */
	private final static String ERRORS_KEY = "eberrors";
	/**
	 * Warning messages - yellow background
	 */
	private final static String WARNINGS_KEY = "ebwarnings";
	/**
	 * Info messages - blue background
	 */
	private final static String INFOS_KEY = "ebinfos";
	
	@Autowired private MessageSource messageSource;
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem SUCCESS_KEY
	 */
	public void addSuccess(RedirectAttributes ra, String code, String... args) {
		addMessage(ra, SUCCESS_KEY, code, args);
	}
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem SUCCESS_KEY
	 */	
	public void addSuccess(HttpServletRequest request, String code, String... args) {
		addMessage(request, SUCCESS_KEY, code, args);
	}
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem ERRORS_KEY.
	 */
	public void addError(RedirectAttributes ra, String code, String... args) {
		addMessage(ra, ERRORS_KEY, code, args);
	}

	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem ERRORS_KEY.
	 */
	public void addError(HttpServletRequest request, String code, String... args) {
		addMessage(request, ERRORS_KEY, code, args);
	}
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem WARNINGS_KEY.
	 */
	public void addWarning(RedirectAttributes ra, String code, String... args) {
		addMessage(ra, WARNINGS_KEY, code, args);
	}
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem WARNINGS_KEY.
	 */
	public void addWarning(HttpServletRequest request, String code, String... args) {
		addMessage(request, WARNINGS_KEY, code, args);
	}	
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem INFOS_KEY.
	 */
	public void addInfo(RedirectAttributes ra, String code, String... args) {
		addMessage(ra, INFOS_KEY, code, args);
	}	
	
	/**
	 * Umiesc komunikat spod podanego klucza 
	 * (lub jesli nie ma takiego klucza to bezposrednio te wartosc)
	 * w liscie w Redirect Attribute pod kluczem INFOS_KEY.
	 */
	public void addInfo(HttpServletRequest request, String code, String... args) {
		addMessage(request, INFOS_KEY, code, args);
	}		
	
	/**
	 * Adds messages into collectio.
	 * Only if given msg/code is no empty.
	 */
	private void addMessage(RedirectAttributes ra, String key, String code, String... args) {
		String msg = messageSource.getMessage(code, args, code, null);
		msg = StringUtils.isEmpty(msg)?code:msg;
		if (!StringUtils.isEmpty(msg)) {
			List<String> messages = retreiveCollection(ra, key);
			messages.add(msg);
		}
	}
	
	private void addMessage(HttpServletRequest request, String key, String code, String... args) {
		String msg = messageSource.getMessage(code, args, code, null);
		msg = StringUtils.isEmpty(msg)?code:msg;
		if (!StringUtils.isEmpty(msg)) {
			List<String> messages = retreiveCollection(request, key);
			messages.add(msg);
		}
	}	
	
	/**
	 * Finds given collection key in RedirectAttributes and returns;
	 * If not found returns empty which is already in RedirectAttributes.
	 */
	@SuppressWarnings("unchecked")
	private List<String> retreiveCollection(RedirectAttributes ra, String key) {
		Map<String, ?> attributes = ra.getFlashAttributes();
		if (attributes != null) {
			for (Map.Entry<String, ?> attr : attributes.entrySet()) {
				if (key.equalsIgnoreCase(attr.getKey())) {//&&attr.getValue() instanceof List<?>) {
					return (List<String>)attr.getValue();
				}
			}
		}
		//..no messages yet? create new..
		List<String> messages = new ArrayList<String>();
		ra.addFlashAttribute(key, messages);
		return messages;
	}
	
	@SuppressWarnings("unchecked")
	private List<String> retreiveCollection(HttpServletRequest request, String key) {
		FlashMap outFlash = RequestContextUtils.getOutputFlashMap(request);
		List<String> messages = new ArrayList<String>();
		if (outFlash!=null) {
			if (outFlash.get(key)==null) outFlash.put(key, new ArrayList<String>());
			messages = (List<String>)outFlash.get(key);
		}
		return messages;
	}
	
}
