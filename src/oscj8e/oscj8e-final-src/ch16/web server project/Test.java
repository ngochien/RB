/**
 * This program illustrates how to get the values of
 *
 * 	1. The log file
 * 	2. the document base
 * 	3. the name of the server.
 */

public class Test
{
	public static void main(String[] args) {
		Configuration configutor = null;
	
		try {
			configutor = new Configuration("./config.xml");
		}
		catch (ConfigurationException ce) {
			System.out.println(ce);
			System.exit(0);
		}

		System.out.println(configutor.getLogFile());
		System.out.println(configutor.getDocBase());
		System.out.println(configutor.getServerName());
	}
}
