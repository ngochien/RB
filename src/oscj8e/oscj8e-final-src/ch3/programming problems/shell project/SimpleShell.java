import java.io.*;

public class SimpleShell
{
	public static void main(String[] args) throws java.io.IOException {
		String commandLine;
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

		// we break out with <control><C>
		while (true) {
			// read what they entered
			System.out.print("jsh>");
			commandLine = console.readLine();

			// if they entered a return, just loop again
			if (commandLine.equals("")) 
				continue;

			/** 
			  The steps are:
			  (1) parse the input to obtain the command
			      and any parameters
			  (2) create a ProcessBuilder object
			  (3) start the process
			  (4) obtain the output stream
			  (5) output the contents returned by the command
			 */ 
	 	}
	}
}
