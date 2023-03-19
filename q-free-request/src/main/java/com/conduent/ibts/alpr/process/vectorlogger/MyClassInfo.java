package com.conduent.ibts.alpr.process.vectorlogger;

/**
 * This is a helper utility class. It will get the caller's class and method
 * name by using the Throwable class to ask the Java VM to fill in this
 * information.
 * <p>
 * This was added to try and get around the limitations of not hardcoding the
 * class name and not being able to get the class name prior to ctor(). For
 * example <br>
 * <br>
 * static String MY_CLASS_NAME = Foobar.class.getName(); <br>
 * <br>
 * works until somebody cuts and pastes the code into another class, and forgets
 * to change this line. But <br>
 * <br>
 * String MY_CLASS_NAME = this.getClass().getName(); <br>
 * <br>
 * Doesn't work with the static modifier as 'this' does not yet exist.  But<br>
 * <br>
 * static String MY_CLASS_NAME = MyClassInfo.getClassName(); <br>
 * <br>
 * will work, and return the correct class name.
 * 
 * @author JRichon
 *  
 */
public class MyClassInfo {

    /**
     * Should be used at the top of the class.  For example:<br> 
     * static final private String MY_CLASS_NAME = MyClassInfo.getClassName();
     * @return The caller's class name as a string
     */
    public static String getClassName() 
    {
    	return new Throwable().getStackTrace()[2].getClassName();
    }

    /**
     * Should be used at the top of a method.  For example:<br>
     * final String MY_METHOD_NAME = MyClassInfo.getMethodName();
     * @return The caller's method as a string
     */
    public static String getMethodName() 
    {
        return new Throwable().getStackTrace()[1].getMethodName();
    }
    
    private MyClassInfo() 
    {
    	/**
    	 * Default ctor to support static nature of the class
    	 */
    }
}
