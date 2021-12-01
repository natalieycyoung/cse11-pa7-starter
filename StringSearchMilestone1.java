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
