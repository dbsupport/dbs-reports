package pl.com.dbs.reports.security.domain;

/**
 * Wyjatek blednej autentykacji. Typ niepowiazany z spring security przez co obslugiwany przez
 * ogolny mechanizm przechwytywania wyjatkow. 
 * @author krzysztof.kaziura@gmail.com
 *
 */
public class AuthenticationFailureException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public AuthenticationFailureException(String string) {
		super(string);
	}
}
