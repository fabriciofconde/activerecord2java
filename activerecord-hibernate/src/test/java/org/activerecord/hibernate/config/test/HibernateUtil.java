package org.activerecord.hibernate.config.test;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 * 
 * @author fabricio.conde
 * 
 */
public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	/**
	 * 
	 * @return
	 */
	private static SessionFactory buildSessionFactory() {
		try {
			return new AnnotationConfiguration().configure().buildSessionFactory();
			//return new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * 
	 * @return
	 */
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	private static final ThreadLocal<Session> threadSession = new ThreadLocal<Session>();
	private static final ThreadLocal<Transaction> threadTransaction = new ThreadLocal<Transaction>();

	public static Session getCurrentSession() {
		Session s = threadSession.get();
		try {
			if (s == null || !s.isOpen()) {
				/*if (mLogger.isInfoEnabled()) {
					mLogger.info("Opening new Session for this thread.");
				}*/
				s = sessionFactory.openSession();
				threadSession.set(s);
			} else {
				/*if (mLogger.isInfoEnabled()) {
					mLogger.info("Using current session in this thread.");
				}*/
			}
		} catch (HibernateException ex) {
			throw new HibernateException("unable to open hibernate session", ex);
		}
		return s;
	}

	public static void closeSession() {
		try {
			final Session s = threadSession.get();
			if (s != null && s.isOpen()) {
				//mLogger.info("Closing Session of this thread.");
				s.close();
			}
		} catch (HibernateException ex) {
			//Throw.loggable(HBM_CLOSE_SESSION, ex, false);
		} finally {
			threadSession.set(null);
		}
	}

	public static void beginTransaction() {
		Transaction tx = threadTransaction.get();
		try {
			if (tx != null && !tx.isActive()) {
				tx = null;
				threadTransaction.set(null);
			}
			if (tx == null) {
				/*if (mLogger.isInfoEnabled()) {
					mLogger.info("Starting new database transaction in this thread.");
				}*/
				if (threadSession.get() != null && threadSession.get().isOpen()) {
					threadSession.get().close();
					threadSession.set(null);
				}
				tx = getCurrentSession().beginTransaction();
				threadTransaction.set(tx);
			} else {
				/*if (mLogger.isInfoEnabled()) {
					mLogger.info("Using current database transaction in this thread.");
					mLogger.info("Opening new Session for this thread.");
				}*/
			}
		} catch (HibernateException ex) {
			//Throw.loggable(HBM_BEGIN_TRANSACTION, ex, false);
		} finally {
			if (threadSession.get() == null || !threadSession.get().isOpen()) {
				getCurrentSession();
			} else {
				threadSession.get().clear();
			}
		}
	}

	public static void commitTransaction() {
		final Transaction tx = threadTransaction.get();
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				Session s = getCurrentSession();
				s.flush();
				/*if (mLogger.isInfoEnabled()) {
					mLogger.info("Flushing session and committing transaction of this thread.");
				}*/
				tx.commit();
			}
		} catch (HibernateException ex) {
			rollbackTransaction();
			//Throw.loggable(HBM_COMMIT_TRANSACTION, ex, false);
		} finally {
			threadTransaction.set(null);
			closeSession();
		}
	}

	public static void rollbackTransaction() {
		final Transaction tx = threadTransaction.get();
		try {
			if (tx != null && !tx.wasCommitted() && !tx.wasRolledBack()) {
				//mLogger.info("Trying to rollback database transaction of this thread.");
				tx.rollback();
			}
		} catch (HibernateException ex) {
			//Throw.loggable(HBM_ROLLBACK_TRANSACTION, ex, false);
		} finally {
			threadTransaction.set(null);
			closeSession();
		}
	}

}
