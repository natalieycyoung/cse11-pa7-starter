/**
 * Programming Assignment 7
 *
 * @author Natalie Young
 * @since 2021-11-11
 */
import java.nio.file.*;
import java.io.IOException;	// error handling

class StringSearch
{
	public static void main(String[] args)
	{
		if (args.length == 1)
		{
			String filepath = "./" + args[0];

			String[] lines = getLines(filepath);

			for (String line : lines)
			{
				System.out.println(line);
			}
		}
		else if (args.length == 2)
		{
			String filepath = "./" + args[0];
			String query = args[1];
			String[] queries = query.split("&");
			String[] lines = getLines(filepath);
			Query qObj;

			for (String line : lines)
			{
				if (queries.length == 1)
				{
					qObj = parseQuery(query);

					if (qObj.matches(line))
					{
						System.out.println(line);
					}
				}
				else if (queries.length > 1)
				{
					Query[] newQArray = new Query[queries.length];

					for (int i = 0; i < queries.length; i++)
					{
						newQArray[i] = parseQuery(queries[i]);
					}

					if (matchesAll(newQArray, line))
					{
						System.out.println(line);
					}
				}
			}
		}
		else if (args.length == 3)
		{
			String filepath = "./" + args[0];
			String query = args[1];
			String transform = args[2];
			String[] queries = query.split("&");
			String[] transformations = transform.split("&");
			String[] lines = getLines(filepath);
			Query qObj;

			for (String line : lines)
			{
				if ((queries.length == 1) && (transformations.length== 1))
				{
					Transform tObj = parseTransform(transform);
					qObj = parseQuery(query);

					if (qObj.matches(line))
					{
						String lineOut = tObj.transform(line);
						System.out.println(lineOut);
					}
				}
				else if ((queries.length) == 1 && (transformations.length > 1))
				{
					Transform[] newTArray = new Transform[transformations.length];

					for (int i = 0; i < transformations.length; i++)
					{
						Transform tObj = parseTransform(transformations[i]);
					}

					qObj = parseQuery(query);

					if (qObj.matches(line))
					{
						String lineOut = applyTransformations(newTArray, line);
						System.out.println(lineOut);
					}
				}
				else if ((queries.length > 1) && (transformations.length > 1))
				{
					Query[] newQArray = new Query[queries.length];
					Transform[] newTArray = new Transform[transformations.length];

					for (int i = 0; i < queries.length; i++)
					{
						qObj = parseQuery(queries[i]);
						newQArray[i] = qObj;
					}

					for (int i = 0; i < transformations.length; i++)
					{
						Transform tObj = parseTransform(transformations[i]);
						newTArray[i] = tObj;
					}

					if (matchesAll(newQArray, line))
					{
						String lineOut = applyTransformations(newTArray, line);

						System.out.println(lineOut);
					}
				}
			}
		}
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

	/**
	 * Parses query and accordingly creates Query object for return
	 *
	 * @param query
	 * @return parsedQuery
	 */
	public static Query parseQuery(String query)
	{
		String[] queryArray = query.split("=");
		String keyword = queryArray[0];
		String searchIndex = queryArray[1];
		Query parsedQuery = null;
		String errorMsg = "Invalid query";

		switch (keyword)
		{
			case "contains":
				parsedQuery = new Contains(searchIndex.substring(1, searchIndex.length() -1));
				break;
			case "length":
				parsedQuery = new Length(Integer.parseInt(searchIndex));
				break;
			case "greater":
				parsedQuery = new GreaterThan(Integer.parseInt(searchIndex));
				break;
			case "less":
				parsedQuery = new LessThan(Integer.parseInt(searchIndex));
				break;
			case "starts":
				parsedQuery = new StartsWith(searchIndex.substring(1, searchIndex.length() - 1));
				break;
			case "ends":
				parsedQuery = new EndsWith(searchIndex.substring(1, searchIndex.length() - 1));
				break;
			case "not":
				parsedQuery = new Not(parseQuery(query.substring(4, query.length() - 1)));
				break;
			default:
				break;
		}

		return parsedQuery;
	}

	/**
	 * Parses transform, returns object accordingly
	 *
	 * @param transform
	 * @return parsedTransform
	 */
	public static Transform parseTransform(String transform)
	{
		Transform parsedTransform = null;
		
		if (transform.contains("="))
		{
			String[] tArray = transform.split("=");
			String transformation = tArray[0];
			String index = tArray[1];

			switch (transformation)
			{
				case "first":
					parsedTransform = new FirstLetters(Integer.parseInt(index));
					break;
				case "last":
					parsedTransform = new LastLetters(Integer.parseInt(index));
					break;
				case "replace":
					parsedTransform = new Replace(transform.substring(9, transform.indexOf(";") - 1), transform.substring(transform.indexOf(";") + 2, transform.length() - 1));
					break;
			}
		}
		else if (transform.equals("upper"))
		{
			parsedTransform = new UpperCase();
		}
		else if (transform.equals("lower"))
		{
			parsedTransform = new LowerCase();
		}

		return parsedTransform;
	}

	/**
	 * Returns false if non-matching element is found, otherwise true
	 *
	 * @param qArray
	 * @param str
	 * @return matchStatus
	 */
	public static boolean matchesAll(Query[] qArray, String str)
	{
		boolean matchStatus = false;

		for (Query query : qArray)
		{
			if (!(query.matches(str)))
			{
				return matchStatus;
			}
		}

		matchStatus = true;

		return matchStatus;
	}

	/**
	 * Applies transformations to string, returns transformed string
	 *
	 * @param transformations
	 * @param str
	 * @return str
	 */
	public static String applyTransformations(Transform[] transformations, String str)
	{
		for (Transform transformation : transformations)
		{
			str = transformation.transform(str);
		}

		return str;
	}



}

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

class Length implements Query
{
	int length;

	Length(int length)
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

