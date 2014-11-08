/**
 * 
 */
package pl.com.dbs.reports.support.web.handler;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import pl.com.dbs.reports.support.web.alerts.Alerts;

import com.google.inject.internal.Lists;

/**
 * !REMEMBER!  !There should NOT be RedirectAttributes in method arguments!
 * 
 * http://stackoverflow.com/questions/9329741/preserving-model-state-with-post-redirect-get-pattern/11610179#11610179
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Component("preserve.errors.interceptor")
public class PreserveErrorsInterceptor extends ResourceExcludingHandlerInterceptorAdapter {
	public static final String BINDING_RESULT_FLUSH_ATTRIBUTE_KEY = PreserveErrorsInterceptor.class.getName() + ".flashBindingResult";
	public static final String FLASH_FORM = PreserveErrorsInterceptor.class.getName() + ".flashForm.";
	
	@Autowired private Alerts alerts;
 
	@Override
    public void doPostHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (post(request)) {
			Entry<String, Object> bindingResult = getBindingResultEntry(modelAndView, BindingResult.MODEL_KEY_PREFIX);
			FlashMap outFlash = RequestContextUtils.getOutputFlashMap(request);
			if (bindingResult == null || !((BindingResult) bindingResult.getValue()).hasErrors() || outFlash == null) {
				return;
			}
			/**
			 * ..przenies wszystkie globalne bledy formularza do alerts..
			 */
			Iterator<ObjectError> i = ((BeanPropertyBindingResult)bindingResult.getValue()).getGlobalErrors().iterator(); 
			while (i.hasNext()) {
				ObjectError e = i.next();
				alerts.addError(request.getSession(), e.getCode(), (String[])e.getArguments());
			}
			
	        outFlash.put(BINDING_RESULT_FLUSH_ATTRIBUTE_KEY, bindingResult);

	        for (Entry<String, Object> entry :  getFormEntry(modelAndView)) {
	        	Object form = entry.getValue();
	        	String formName = entry.getKey();
	        	if (form == null || StringUtils.isBlank(formName)) {
	        		continue;
	        	}
	            outFlash.put(FLASH_FORM+formName, form);	        	
	        }
	        
            return;
		}
 
		Map<String, ?> inFlash = RequestContextUtils.getInputFlashMap(request);
 
		if (get(request) && isBingindResult(inFlash)) {
			@SuppressWarnings("unchecked")
            Entry<String, Object> flashBindingResult = (Entry<String, Object>) inFlash.get(BINDING_RESULT_FLUSH_ATTRIBUTE_KEY);
 
            Entry<String, Object> bindingResultEntry = getBindingResultEntry(modelAndView, flashBindingResult.getKey());
 
            if (bindingResultEntry == null) {
            	return;
            }
            BindingResult bindingResult = (BindingResult) bindingResultEntry.getValue();
            if (bindingResult == null) {
            	return;
            }
            /**
             * ..dodaj tylko bledy pol (globalne zostaly przepisane do alerts)..
             */
            for (FieldError ferror : ((BeanPropertyBindingResult)flashBindingResult.getValue()).getFieldErrors()) {
            	bindingResult.addError(ferror);	
            }
            
            for (Entry<String, Object> entry : getFormEntry(modelAndView)) {
            	String formName = entry.getKey();
            	Object form = inFlash.get(FLASH_FORM+formName);
            	if (StringUtils.isBlank(formName) || form==null) {
            		continue;
            	}
            	// jeśli formularz jest sesyjny to ten obiekt i tak już jest w tej mapie, więc go nadpisujemy
            	modelAndView.addObject(formName, form);
            }
		}
	}
	
    private boolean isBingindResult(Map<String, ?> inFlash) {
    	return inFlash != null && inFlash.containsKey(BINDING_RESULT_FLUSH_ATTRIBUTE_KEY);
    }
 
    private List<Entry<String, Object>> getFormEntry(ModelAndView modelAndView) {
    	List<Entry<String, Object>> entries = Lists.newArrayList();
    	
    	if (modelAndView == null) {
    		return entries;
    	}
        for (Entry<String, Object> entry : modelAndView.getModel().entrySet()) {
        	if (entry.getValue() != null //&& entry.getValue().getClass().isAnnotationPresent(ZachowajBledyPoPrzekierowaniu.class)
        			&& !(entry.getValue() instanceof BindingResult)
        			&& !entry.getKey().startsWith(FLASH_FORM)) {
        		entries.add(entry);
        	}
        }
        return entries;
    }
 
    public static BindingResult getBindingResult(ModelAndView modelAndView) {
    	if (modelAndView == null) {
    		return null;
        }
       for (Entry<String, ?> key : modelAndView.getModel().entrySet()) {
                       if (key.getKey().startsWith(BindingResult.MODEL_KEY_PREFIX)) {
                                       return (BindingResult) key.getValue();
                       }
       }
       return null;
    }
 
    public static Entry<String, Object> getBindingResultEntry(ModelAndView modelAndView, String klucz) {
    	if (modelAndView == null) {
    		return null;
        }
 
    	for (Entry<String, Object> key : modelAndView.getModel().entrySet()) {
    		if (key.getKey().startsWith(klucz)) {
    			return key;
    		}
    	}
    	return null;
    }
}
