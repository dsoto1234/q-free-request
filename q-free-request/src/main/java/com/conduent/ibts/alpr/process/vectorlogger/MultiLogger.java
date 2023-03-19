package com.conduent.ibts.alpr.process.vectorlogger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class MultiLogger {
	
    private Logger logger 						= null;
    private VectorFileHandler vectorFileHandler = null;    
    private static HashMap loggers 				= new HashMap();
    private static final String THIS_CLASS_NAME = MultiLogger.class.getName();

    private static final String TRCS_DEFAULT_LOGGER_NAME = "com.conduent.ibts.alpr.process", TRCS_DEFAULT_LOG_FNAME = "sreeni.log";
    
    public Logger getLogger() {
        return logger;
    }
    
    /**
     * Get the list of loggers
     * 
     * @return loggers
     */
    public static HashMap getLoggers() {
        return loggers;
    }

    /**
     * Sets the logging level if it is parsed by Level.parse method.
     * 
     * @param newLevel
     *            The new level (e.g. "ALL")
     */
    public void setLevel(String newLevel) {
        Level l;
        try {
            l = Level.parse(newLevel);
        } catch (SecurityException e) {
            logger.warning(e.getMessage());
            return;
        } catch (IllegalArgumentException e) {
            logger.warning(e.getMessage());
            return;
        }

        logger.setLevel(l);
    }

    /**
     * Returns the current level as a string. If this logger's level is unset,
     * it will look to the parent. If it is still unset, it will return the
     * string "null".
     * 
     * @return The level as a string
     */
    public String getLevel() {
        Level l = logger.getLevel();
        if (l == null && logger.getParent() != null) {
            l = logger.getParent().getLevel();
        }

        if (l == null) {
            return "null";
        }

        return l.getName();
    }

    /**
     * Retrieves a references to the named logger. Creates one, if necessary.
     * 
     * @return The default logger.
     */
    public static MultiLogger getLogger(final String loggerName) {

    	MultiLogger logger;
        logger = new MultiLogger(loggerName);

        return logger;
    }

    /**
     * If the application is restarted, but the server container is not, then
     * the loggers previously added still exist. Reload them into our copy of
     * known loggers. This is done to avoid creating and adding handlers when
     * the loggers already have them
     */
    private static void reloadLoggers() {

        try {
            LogManager lm = LogManager.getLogManager();
            Enumeration e = lm.getLoggerNames();
            while (e.hasMoreElements()) {
                String knownLoggerName = (String) e.nextElement();
                if (knownLoggerName.startsWith("com.conduent.ibts.alpr.process")) {
                    Logger javaLogger = lm.getLogger(knownLoggerName);
                    MultiLogger trcsLogger = new MultiLogger(javaLogger);
                    loggers.put(knownLoggerName, trcsLogger);
                }

            }
        } catch (Exception e) {
            //noop
        }
    }

    /**
     * used when reloading the loggers after a restart
     */
    private MultiLogger(Logger javaLogger) {
        logger = javaLogger;
    }

    /**
     * Creates new logger.
     * 
     * @param name
     *            name of the new logger; a fully qualified package name is
     *            expected.
     *  
     */
    public MultiLogger(final String name) {

        try {
            /*
             * try to create a named logger. This will fail for unsigned
             * applets, and other security conditions
             */
            logger = Logger.getLogger(name);

            // if to make VectorFormatter scope "package" do:
            // 1. remove default ctor
            // 2. retrieve logger handlers
            // 3. each set formatter
        } catch (SecurityException ex) {
            Logger.getAnonymousLogger().throwing(THIS_CLASS_NAME, "ctor", ex);
        } catch (Exception ex) {
            Logger.getAnonymousLogger().throwing(THIS_CLASS_NAME, "ctor", ex);
            ex.printStackTrace(System.err);
        } finally {
            if (logger == null) {
                logger = Logger.getAnonymousLogger();
            }

            logger.setUseParentHandlers(false);

            VectorFormatter fmt = new VectorFormatter();
            /*
             * The logger's level will control what records are sent to the
             * console and file handlers, so they should output all records they
             * get.
             */
             try {
                //vectorFileHandler = new VectorFileHandler(name, 5000000, 2, true);
            	 vectorFileHandler = new VectorFileHandler(name,  900000000, 20, true);
                
                vectorFileHandler.setFormatter(fmt);
                vectorFileHandler.setLevel(Level.ALL);
                logger.addHandler(vectorFileHandler);

             } catch (Exception ex) {
                logger.throwing(THIS_CLASS_NAME, "ctor", ex);
            }
        }
    } //	ctor

    public static String constructLogFileName(final String loggerName) {
        
        String s = TRCS_DEFAULT_LOG_FNAME;
        CharSequence with = loggerName;

        if (-1 != loggerName.lastIndexOf(".")) {
            //	extract least significant part of the package name
            //	the one after the last dot
            with = loggerName.subSequence(loggerName.lastIndexOf(".") + 1,
                    loggerName.length());
        }

        s = s.replaceFirst("@", with.toString());

        return s;
    }

    /**
     * Writes to the log a debug message qualified with class and method names.
     * 
     * @param className
     * @param methName
     * @param msg
     *            the message to be logged
     * @see java.util.logging.Logger#finer( java.lang.String)
     */
    public void debug(final String className, final String methName, final String msg) {
        
        LogRecord record = new LogRecord(Level.FINER, msg);
        record.setSourceClassName(className);
        record.setSourceMethodName(methName);

        log(record);
    }
    
   
    /**
     * Writes to the log a debug message qualified with class and method names.
     * Specifically requested to handle transactional debugging: this has
     * additional parameter txId.
     * 
     * @param className
     * @param methName
     * @param txId
     *            transaction id as <code>String</code>
     * @param msg
     *            the message to log
     * @see #debug( java.lang.String, java.lang.String, java.lang.String)
     */
    public void debug(final String className, final String methName, final String txId, final String msg) {
        
        StringBuffer sb = new StringBuffer(200).append(txId).append(
                VectorFormatter.COLON).append(VectorFormatter.SPACE).append(msg);
        
        debug(className, methName, sb.toString());
        
        return;
    }

    /**
     * Used to mark entering a method. This and exiting() are useful when
     * analysing threading and locking problems.
     * 
     * @param className
     * @param methName
     * @param msg
     *            Message to log
     * @see #exiting( java.lang.String, java.lang.String, java.lang.String)
     * @see java.util.logging.Logger#entering( java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public void entering(final String className, final String methName, final String msg) {
    	
        logger.entering(className, methName, msg);
        return;
    }
    
    /**
     * Used to mark entering a method. This and exiting() are useful when
     * analysing threading and locking problems.
     * 
     * @param className
     * @param methName
     * @param txId 
     * @param msg
     *            Message to log
     * @see #exiting( java.lang.String, java.lang.String, java.lang.String)
     * @see java.util.logging.Logger#entering( java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public void entering(final String className, final String methName, final String txId , final String msg) {

    	 StringBuffer sb = new StringBuffer(200).append(txId).append(
                VectorFormatter.COLON).append(VectorFormatter.SPACE).append(msg);
    	 
        logger.entering(className, methName, sb.toString());
        
        return;
    }


    /**
     * Method for logging error messages.
     * 
     * @param msg
     *            Message to log
     */
    public void error(final String msg) {
        
        LogRecord record = new LogRecord(Level.SEVERE, msg);
        record.setSourceMethodName("");

        log(record);
        return;
    }

    /**
     * Used to mark when a method is exited
     * 
     * @param className
     * @param methName
     * @param msg
     *            Message to log
     * @see #entering( java.lang.String, java.lang.String, java.lang.String)
     * @see java.util.logging.Logger#exiting( java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public void exiting(final String className, final String methName, final String msg) {
        
        logger.exiting(className, methName, msg);
        return;
    }
    
    /**
     * Used to mark when a method is exited
     * 
     * @param className
     * @param methName
     * @param txId 
     * @param msg
     *            Message to log
     * @see #entering( java.lang.String, java.lang.String, java.lang.String)
     * @see java.util.logging.Logger#exiting( java.lang.String,
     *      java.lang.String, java.lang.Object)
     */
    public void exiting(final String className, final String methName, final String txId,  final String msg) {
        
    	StringBuffer sb = new StringBuffer(200).append(txId).append(
                VectorFormatter.COLON).append(VectorFormatter.SPACE).append(msg);
    	
    	logger.exiting(className, methName, sb.toString());
    	
        return;
    }

    /**
     * Return the name of the logger
     * 
     * @return logger name, provided to
     *         <code>getLogger( java.lang.String)</code>
     */
    public String getName() {
        
        return logger.getName();
    }

    /**
     * This method is used to log general information that would be of interest
     * to users of the system
     * 
     * @param msg
     *            Message to log
     * @see java.util.logging.Logger#info( java.lang.String)
     */
    public void info(final String msg) {
        
        logger.info(msg);
        return;
    }

    /**
     * Same as info(), but includes the class and method logging the message
     * 
     * @param className
     * @param methName
     * @param msg
     */
    public void info(final String className, final String methName, final String msg) {        
    	
        LogRecord record = new LogRecord(Level.INFO, msg);
        record.setSourceClassName(className);
        record.setSourceMethodName(methName);

        log(record);
    }
    
    /**
     * Same as info(), but includes the class and method logging the message
     * 
     * @param className
     * @param methName
     * @param txId
     * @param msg
     */
    public void info(final String className, final String methName, final String txId ,  final String msg) {
        
    	StringBuffer sb = new StringBuffer(200).append(txId).append(
                VectorFormatter.COLON).append(VectorFormatter.SPACE).append(msg);
    	
        LogRecord record = new LogRecord(Level.INFO, sb.toString());
        record.setSourceClassName(className);
        record.setSourceMethodName(methName);

        log(record);
    }


    /**
     * Internal method to log a record
     * 
     * @param rec
     */
    private void log(LogRecord rec) {
        
        logger.log(rec);
        return;
    }

    /**
     * Specifically requested to handle transactional debugging: this has
     * additional parameter txId
     * 
     * Used to log an exception. Note that TrcsException automatically logs
     * itself, so it is not neccessary to log it using this, or other, methods.
     * 
     * @param className
     * @param methName
     * @param txId
     * @param msg
     * @param ex
     *            Throwable associated with log message
     * 
     * @see java.util.logging.Logger#throwing( java.lang.String,
     *      java.lang.String, java.lang.Throwable)
     */
    public void throwing(final String className, final String methName, final String txId, final String msg, Throwable ex) {
        
        StringBuffer sb = new StringBuffer(100).append(txId).append(
                VectorFormatter.COLON).append(VectorFormatter.SPACE).append(msg);

        throwing(className, methName, sb.toString(), ex);

        return;
    }
    
    	

    /**
     * Used to log an exception with a stack trace of the exception. Note that
     * TrcsException automatically logs itself, so it is not neccessary to log a
     * TrcsException using this, or other, methods. Use if method is throwing a
     * non-trcs exception, or it caught an exception and is signalling failure
     * by returning a value, but you still want to log all the details.
     * 
     * @param className
     * @param methName
     * @param msg
     *            Message to log
     * @param ex
     *            Throwable associated with log message
     * 
     * @see java.util.logging.Logger#throwing( java.lang.String,
     *      java.lang.String, java.lang.Throwable)
     */
    public void throwing(final String className, final String methName, final String msg, Throwable ex) {
        
        LogRecord record = new LogRecord(Level.WARNING, msg);
        record.setSourceClassName(className);
        record.setSourceMethodName(methName);
        record.setThrown(ex);

        log(record);
    }

    /**
     * Method to log a warning message, which indicates that there is a problem
     * with the system, but that it can continue running.
     * 
     * @param msg
     *            The message to log
     * @see java.util.logging.Logger#warning( java.lang.String)
     */
    public void warning(final String msg) {
        
        logger.warning(msg);
        return;
    }

    /**
     * Same as warning(), but includes the class and method logging the message
     * 
     * @param className
     * @param methName
     * @param msg
     */
    public void warning(final String className, final String methName, final String msg) {
        
    	
        LogRecord record = new LogRecord(Level.WARNING, msg);
        record.setSourceClassName(className);
        record.setSourceMethodName(methName);

        log(record);
    }
    
    
    /**
     * Same as warning(), but includes the class and method logging the message
     * 
     * @param className
     * @param methName
     * @param txId
     * @param msg
     */
    public void warning(final String className, final String methName, final String txId ,   final String msg) {
        
    	StringBuffer sb = new StringBuffer(100).append(txId).append(
                VectorFormatter.COLON).append(VectorFormatter.SPACE).append(msg);
    	
        LogRecord record = new LogRecord(Level.WARNING, sb.toString());
        record.setSourceClassName(className);
        record.setSourceMethodName(methName);

        log(record);
    }

	public VectorFileHandler getVectorFileHandler() {
		return vectorFileHandler;
	}

	public void setVectorFileHandler(VectorFileHandler vectorFileHandler) {
		this.vectorFileHandler = vectorFileHandler;
	}
    
	public void closeFileHandler() {
		vectorFileHandler.close();
	}

} // VectorLoggerExt
