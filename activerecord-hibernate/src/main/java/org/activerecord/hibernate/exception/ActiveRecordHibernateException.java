package org.activerecord.hibernate.exception;

/**
 * 
 * @author fabricio.conde
 *
 */
public class ActiveRecordHibernateException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ActiveRecordHibernateException() {
	}

	public ActiveRecordHibernateException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ActiveRecordHibernateException(String message) {
		super(message);
	}

	public ActiveRecordHibernateException(Throwable throwable) {
		super(throwable);
	}

}
