package com.conduent.ibts.alpr.process.exception;

/**
 * Class used to define the standard exception levels used by the TRCS system.
 * The standard levels are:
 * <ul>
 * <li>UNDEFINED
 * <li>RECOVERABLE
 * <li>CRITICAL
 * <li>FATAL
 * </ul>
 */
public 
class VectorExceptionLevel implements Comparable<Object>
{
	private final 
	String	name;

	private static 
	int		nextOrdinal	= 0;

	private final 
	int			ordinal			= nextOrdinal++;

	private 
	VectorExceptionLevel( String levelName)
	{
		this.name = levelName;
	}

	public 
	int compareTo( Object o)
	{
		return ordinal - ((	VectorExceptionLevel) o).ordinal;
	}

	public 
	String toString()
	{
		return name;
	}

	/**
	 * The level was never set
	 */
	public static final 
	VectorExceptionLevel	UNDEFINED		= new VectorExceptionLevel(	"undefined");

	/**
	 * The object does not know how to deal with the exception and is passing it
	 * back to the caller. The object did not complete the expected action, and is
	 * notifying the caller so that the caller can take corrective action. Object
	 * is self-consistent and stable.
	 */
	public static final 
	VectorExceptionLevel	RECOVERABLE	= new VectorExceptionLevel(	"recoverable");

	/**
	 * The object is not stable, and probably cannot be made so. Object should be
	 * discarded.
	 */
	public static final 
	VectorExceptionLevel	CRITICAL		= new VectorExceptionLevel(	"critical");

	/**
	 * The object is neither stable nor self-consistent. Not only is the object
	 * untrustworthy, but the health of the system is in question.
	 */
	public static final 
	VectorExceptionLevel	FATAL				= new VectorExceptionLevel(	"fatal");

}
