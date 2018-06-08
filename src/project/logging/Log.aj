package project.logging;

import java.util.logging.*;
import java.util.Date;
import java.io.IOException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import project.exception.HeaderException;
import project.models.User;
import project.validators.HeaderValidator;

/**
 * Aspekt s³u¿¹cy do logowania zdarzeñ zachodz¹cych na serwerze
 */
public aspect Log {
	/**
	 * Logger s³u¿¹cy do zapisu logów
	 * @see java.util.logging.Logger
	 */
	private static Logger log = Logger.getLogger(Log.class.getName());
	/**
	 * obiekt s³u¿¹cy do konfiguracji loggera, przechowuje œcierzke do pliku, gdzie zapisywane s¹ logi
	 */
	private static FileHandler fh;  

	/**
	 * Konstruktor statyczny s³u¿¹cy do konfiguracji loggera
	 */
	static {
	    try {  

	        fh = new FileHandler("C:/fileServer/serverLogs.log");  
	        log.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }
	}

	/**
	 * punkt przeciêcia dla wszystkich metod zwracaj¹cych Response
	 */
    pointcut exception():
    	execution(public Response project..*(..));
    
    /**
	 * punkt przeciêcia dla wszystkich metod zwracaj¹cych Response
	 */
    pointcut regular():
    	execution(public Response project..*(..));
    
    /**
	 * punkt przeciêcia dla wszystkich metod zwracaj¹cych Response i przyjmuj¹cych jako argument obiekt typu HttpHeaders
	 */
    pointcut withPermissions(HttpHeaders headers):
        execution(public Response project..*(HttpHeaders,..)) && args(headers,..);

    /**
     * Rada uruchamiana jeœli zostanie rzucony wyj¹tek
     * @param e rzucony wyj¹tek
     */
    after() throwing (Exception e): exception() {
    	Date date = new Date( );
        log.log(Level.SEVERE, "[{0}] Exception occured: {1}\n{2}", new Object[] {date, e.getMessage(), e.getStackTrace()});
    }
    
    /**
     * Rada uruchamiana dla zwyk³ych metod zwracaj¹cych typ Response
     */
    after(): regular() {
    	boolean cond = false;
        for (Object arg : thisJoinPoint.getArgs()) {
            if (arg != null && arg instanceof HttpHeaders) {
            	cond = true;
                break;
            }
        }
        if (!cond) {
	    	Date date = new Date();
	        log.log(Level.INFO, "[{0}] calling method: {1}", new Object[] {date, thisJoinPoint.getStaticPart().getSignature()});
        }
    }

    /**
     * Rada uruchamiana dla metod zwracaj¹cych typ Response i przyjmuj¹cych obiekt typu HttpHeaders
     * @param headers nag³ówek rz¹dania
     */
    before(HttpHeaders headers): withPermissions(headers) {
    	Date date = new Date( );
    	User usr = null;
		try {
			usr = HeaderValidator.validate(headers);
		}
		catch(HeaderException e) {
		}
		if (usr != null)
			log.log(Level.INFO, "[{0}] User: {1} is calling method: {2}", new Object[] {date, usr.getLogin(), thisJoinPoint.getStaticPart().getSignature()});
		else
			log.log(Level.INFO, "[{0}] User: {1} tried to call method: {2}", new Object[] {date, "unknown", thisJoinPoint.getStaticPart().getSignature()});
    }
}