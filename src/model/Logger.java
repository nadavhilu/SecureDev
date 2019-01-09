package model;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class Logger 
{
	private static Logger instance = null;
	public final static String DEFAULT_FILE_NAME ="logs/log";
	static Integer count=0;
	private FileHandler handler;

	private Logger() 
	{
		try 
		{
			handler = new FileHandler(DEFAULT_FILE_NAME+count+".txt");
		} 
		catch (SecurityException | IOException e) 
		{
			e.printStackTrace();
		}

	}

	public static Logger getInstance ()
	{
		if(instance==null) 
			instance = new Logger();
		return instance ;
	}

	public synchronized void write (String command , Level level)
	{
		handler.setFormatter(new OnlyMessageFormatter());
		handler.publish(new LogRecord(level,command+System.lineSeparator()));
	}

	public class OnlyMessageFormatter extends Formatter
	{
		public OnlyMessageFormatter() 
		{
			super();
		}

		@Override
		public String format(final LogRecord record) 
		{
			return record.getMessage();
		}
	}

	public void newFile() 
	{
		
		try 
		{
			count++;
			handler = new FileHandler(DEFAULT_FILE_NAME+count+".txt");
		} 
		catch (SecurityException | IOException e) 
		{
			e.printStackTrace();
		}

	} 
	public static String getFileName()
	{
		return DEFAULT_FILE_NAME+count+".txt";
	}
}
