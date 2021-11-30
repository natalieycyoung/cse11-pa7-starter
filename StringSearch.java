/**
 * Programming Assignment 7
 *
 * @author Natalie Young
 * @since 2021-11-11
 */

import java.nio.file.*;
import java.io.IOException;	// error handling


interface Query
{
	boolean matches(String str);
}

interface Transform
{
	String transform(String str);
}

class Contains implements Query
{
	String keyword;

	Contains(String keyword)
	{
		this.keyword = keyword;
	}

	public boolean matches(String str)
	{
		return (str.contains(this.keyword));
	}
}

class LongerThan implements Query
{
	int length;

	LongerThan(int length)
	{
		this.length = length;
	}

	public boolean matches(String str)
	{
		return (str.length() > length);
	}
}

class GreaterThan implements Query
{
	int value;

	GreaterThan(int value)
	{
		this.value = value;
	}

	public boolean matches(String str)
	{
		return (str.length() > value);
	}
}

class LessThan implements Query
{
	int value;

	LessThan(int value)
	{
		this.value = value;
	}

	public boolean matches(String str)
	{
		return (str.length() < value);
	}
}

class StartsWith implements Query
{
	String keyword;

	StartsWith(String keyword)
	{
		this.keyword = keyword;
	}

	public boolean matches(String str)
	{
		return (str.startsWith(this.keyword));
	}
}

class EndsWith implements Query
{
	String keyword;

	EndsWith(String keyword)
	{
		this.keyword = keyword;
	}

	public boolean matches(String str)
	{
		return (str.endsWith(this.keyword));
	}
}

class Not implements Query
{
	Query query;

	Not(Query query)
	{
		this.query = query;
	}

	public boolean matches(String str)
	{
		return (!(query.matches(str)));
	}
}

class UpperCase implements Transform
{
	public String transform(String str)
	{
		return str.toUpperCase();
	}
}

class LowerCase implements Transform
{
	public String transform(String str)
	{
		return str.toLowerCase();
	}
}

class FirstLetters implements Transform
{
	int nLetters;

	FirstLetters(int nLetters)
	{
		this.nLetters = nLetters;
	}

	public String transform(String str)
	{
		if (str.length() < this.nLetters)
		{
			return str;
		}

		return str.substring(0, this.nLetters);
	}
}

class LastLetters implements Transform
{
	int nLetters;

	LastLetters(int nLetters)
	{
		this.nLetters = nLetters;
	}

	public String transform(String str)
	{
		if (str.length() < this.nLetters)
		{
			return str;
		}

		return str.substring(str.length() - this.nLetters, str.length());
	}
}

class Replace implements Transform
{
	String toReplace;
	String replaceWith;

	Replace(String toReplace, String replaceWith)
	{
		this.toReplace = toReplace;
		this.replaceWith = replaceWith;
	}

	public String transform(String str)
	{
		return str.replace(toReplace, replaceWith);
	}
}

class StringSearch
{
	public static Query parseQuery(String query)
	{
		String[] queryArray = query.split("=");
		String keyword = queryArray[0];
		String searchIndex = queryArray[1];


	}

	public static void main(String[] args)
	{
		String filename = getFilename(args);
		
		String path = "./" + filename;

//		System.out.println("path: " + path);
		
		String query = getQuery(args);

		String transform = getTransform(args);

		String[] lines = getLines(path);

		System.out.println(filename + ":");

		for (String word : lines)
		{
			System.out.println(word);
		}

//		System.out.println("contents: " + contents);
/*
		System.out.println("filename: " + filename);
		System.out.println("query: " + query);
		System.out.println("transform: " + transform);
*/
	}

	static String getFilename(String[] args)
	{
		return args[0];
	}

	static String getQuery(String[] args)
	{
		if (args.length > 1)
		{
			return args[1];
		}
		
		return "";
	}

	static String getTransform(String[] args)
	{
		if (args.length > 2)
		{
			return args[2];
		}

		return "";
	}

	static String[] getLines(String path)
	{
		try
		{
			return Files.readAllLines(Paths.get(path)).toArray(String[]::new);
		}
		catch (IOException ioe)
		{
			System.err.println("Error reading file " + path
					+ ": " + ioe);
			return new String[]{"Error reading file " + path + ": " + ioe};
		}
	}
	
}
/*
class FileHelper
{
	static String
}
*/
