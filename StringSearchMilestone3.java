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
