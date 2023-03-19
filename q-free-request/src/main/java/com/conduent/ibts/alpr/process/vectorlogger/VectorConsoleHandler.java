package com.conduent.ibts.alpr.process.vectorlogger;

import java.io.OutputStream;
import java.util.logging.ConsoleHandler;

/**
 * Class <code>VectorConsoleHandler</code> extends <code>java.util.logging</code> 
 * suggested {@link java.util.logging.ConsoleHandler ConsoleHandler} class.  
 * 
 * @author arebo
 * @version 1.1, 06/10/06
 * @see     java.util.logging.FileHandler
 * @since   1.0	
 */
public class VectorConsoleHandler extends ConsoleHandler {
	/**
	 * 
	 * @author arebo
	 *
	 * TODO Really nothing to do here now, while I'm OK with
	 * the default {@link java.util.logging.ConsoleHandler ConsoleHandler} 
	 * implementation.
	 * 
	 * Formatting is taken care of by 
	 * {@link com.acs.ts.vector.logging.VectorFormatter VectorFormatter}
	 */
    
    protected void setOutputStream( OutputStream out) {
        super.setOutputStream(out);
    }
}
