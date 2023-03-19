package com.conduent.ibts.alpr.process.vectorlogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class VectorFormatter extends java.util.logging.Formatter {

    static private int stackTraceDepth = -1;

    static private boolean verboseStackDump = true;

    static final private String PROPERTIES_FILES = "./VectorFormatter.properties";

    static final String EMPTY = "", SPACE = " ", ZERO = "0", COLON = ":",
            COLON_ZERO = ":0", DOT = ".", DOT_ZERO = ".0",
            DOT_ZEROZERO = ".00", TRACE = "TRACE", DEBUG = "DEBUG",
            ERROR = "ERROR", TAB = "\t";
    
    private TimeZone localTimeZone = null;

    public VectorFormatter() {
        super();

        if (stackTraceDepth == -1) {
            VectorFormatter.loadProperties();
        }
        
        localTimeZone = TimeZone.getDefault();
    }

    /**
     * @see java.util.logging.Formatter#format(java.util.logging.LogRecord)
     */
    public String format(LogRecord record) {

        Level level = record.getLevel();
        String newLevelName = level.getName();
        if (Level.FINER.equals(level) || Level.FINEST.equals(level)) {
            newLevelName = TRACE;
        } else if (Level.FINE.equals(level)) {
            newLevelName = DEBUG;
        } else if (Level.SEVERE.equals(level)) {
            newLevelName = ERROR;
        }

        long seqNum = record.getSequenceNumber();
        CharSequence message = getMessage(record);
        String srcClassName = record.getSourceClassName();
        String srcMethName = record.getSourceMethodName();

        String justClassName = EMPTY;
        if ((null != srcClassName) && (0 != srcClassName.length())) {
            justClassName = srcClassName.substring(srcClassName.lastIndexOf(DOT) + 1);
        }

        /*
         * compute the time stamp. The Calendar based routines were very slow,
         * so for now use modulo and division to compute it, adjusting for the
         * timezone as the system time is GMT.
         */
        long tz = localTimeZone.getOffset(record.getMillis());
        long ms = (record.getMillis() + tz) % 86400000l;
        long hh = ms / 3600000l;
        ms = ms % 3600000l;
        long mm = ms / 60000l;
        ms = ms % 60000l;
        long ss = ms / 1000;
        ms = ms % 1000;

        /*
         * build up the message
         */
        StringBuffer sb = new StringBuffer(200);
        sb.append(hh < 10 ? ZERO : EMPTY).append(hh).append(
                mm < 10 ? COLON_ZERO : COLON).append(mm).append(
                ss < 10 ? COLON_ZERO : COLON).append(ss).append(
                ms < 10 ? DOT_ZEROZERO : ms < 100 ? DOT_ZERO : DOT).append(ms)
                .append(SPACE).append(seqNum).append(SPACE)
                .append(newLevelName).append(SPACE).append(justClassName)
                .append(".").append(srcMethName).append(SPACE).append(message);

        /*
         * append thrown message if there is one
         */
        Throwable ex = record.getThrown();
        if (null != ex) {
            sb.append(SPACE);
            String msg = ex.getMessage();
            if (msg != null) {
                sb.append(msg);
            }

            StackTraceElement[] trace = ex.getStackTrace();

            sb.append("\n");
            int lineCount = 0;
            for (int i = 0; i < trace.length && lineCount < stackTraceDepth; i++) {
                String className = trace[i].getClassName();
                if (verboseStackDump || className.startsWith("com.conduent")) {
                    ++lineCount;
                    sb.append(TAB);
                    sb.append(className);
                    sb.append(SPACE);
                    sb.append(trace[i].getMethodName());
                    sb.append(SPACE);
                    sb.append(trace[i].getLineNumber());
                    sb.append("\n");
                }
            }
        }

        sb.append("\n");

        return sb.toString();
    }

    //	Messages passed as last argument to entering/exiting can be
    //	obtained through getParameters
    private static CharSequence getMessage(LogRecord record) {
        String retValue = record.getMessage();
        if (retValue.indexOf("{0}") != -1) {
            Object[] params = record.getParameters();
            if (0 != params.length) {
                StringBuffer sb = new StringBuffer(SPACE);
                for (int i = 0; i < params.length; i++) {
                    sb.append(params[i]).append(SPACE);
                }

                retValue = retValue.replaceFirst("{0}", sb.toString());
            }
        }

        return retValue;
    }

    /**
     * @return Returns the stackTraceDepth.
     */
    public static int getStackTraceDepth() {
        if (stackTraceDepth == -1) {
            VectorFormatter.loadProperties();
        }
        return stackTraceDepth;
    }

    /**
     * @param stackTraceDepth
     *            The stackTraceDepth to set.
     */
    public static void setStackTraceDepth(int stackTraceDepth) {
        if (VectorFormatter.stackTraceDepth != stackTraceDepth) {
            VectorFormatter.stackTraceDepth = stackTraceDepth;
            updateProperties();
        }
    }

    /**
     * @return Returns the verboseStackDump.
     */
    public static boolean isVerboseStackDump() {
        if (stackTraceDepth == -1) {
            VectorFormatter.loadProperties();
        }
        return verboseStackDump;
    }

    /**
     * @param verboseStackDump
     *            The verboseStackDump to set.
     */
    public static void setVerboseStackDump(boolean verboseStackDump) {
        if (VectorFormatter.verboseStackDump != verboseStackDump) {
            VectorFormatter.verboseStackDump = verboseStackDump;
            updateProperties();
        }
    }

    /**
     * Write out the properties back to the property file
     */
    private static void updateProperties() {

        File propFile = new File(PROPERTIES_FILES);

        Properties p = new Properties();
        p.put("verboseStackDump", Boolean.toString(verboseStackDump));
        p.put("stackTraceDepth", Integer.toString(stackTraceDepth));

        try {
            FileOutputStream output = new FileOutputStream(propFile);
            p.store(output, "VectorFormatter Properties");
            output.close();

        } catch (FileNotFoundException e) {
            // noop
        } catch (IOException e) {
            // noop
        }

    }

    /**
     * load properties from the file, set default values if no file found
     *  
     */
    private static void loadProperties() {

        //set the default values
        verboseStackDump = true;
        stackTraceDepth = 50;

        File propFile = new File(PROPERTIES_FILES);
        if (propFile.exists()) {
            try {
                FileInputStream input = new FileInputStream(propFile);
                Properties p = new Properties();
                p.load(input);
                input.close();

                verboseStackDump = Boolean.valueOf(p.getProperty(
                        "verboseStackDump", "true")).booleanValue();

                stackTraceDepth = Integer.parseInt(p.getProperty(
                        "stackTraceDepth", "50"));

            } catch (FileNotFoundException e) {
                // noop
            } catch (IOException e) {
                // noop
            } catch (NumberFormatException e) {
                // noop
            }
        }
    }
}
