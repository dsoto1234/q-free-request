package com.conduent.ibts.alpr.process.vectorlogger;

import java.io.IOException;
import java.util.logging.FileHandler;


public class VectorFileHandler extends FileHandler
{

	public VectorFileHandler( ) throws IOException, SecurityException
	{
		super( );
	}

	public VectorFileHandler(String pattern) throws IOException, SecurityException
	{
		super( pattern);
	}
	
	public VectorFileHandler(String pattern, boolean append) throws IOException, SecurityException
	{
		super( pattern, append);
	}
	
	public VectorFileHandler(String pattern, int limit, int count) throws IOException, SecurityException
	{
		super( pattern, limit, count);
	}
	
	public VectorFileHandler(String pattern, int limit, int count, boolean append)  throws IOException, SecurityException
	{
		super( pattern, limit, count, append);
	}	
}
