package com.sonata.generic.automation.library;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * The runner class is used to run our own batch or executable files. These are
 * executables or batch files that more likely run in the background and don't
 * show a user interface or run in the command screen.
 * 
 * This class was created to run a PowerShell script through a batch file but
 * can be used to run applications created in C# for example.
 * 
 * @author lanro08
 * 
 */
public final class Runner {
	/**
	 * The constructor was made private so it can never be instantiated.
	 */
	private Runner() {
	}

	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * No arguments are pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}

	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * One arguments is pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @param arg1
	 *            First argument being passed to the executable or batch file
	 * 
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file, final String arg1) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file, arg1);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}

	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * One arguments is pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @param arg1
	 *            First argument being passed to the executable or batch file
	 * @param arg2
	 *            Second argument being passed to the executable or batch file
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file, final String arg1,
			final String arg2) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file, arg1, arg2);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}

	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * One arguments is pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @param arg1
	 *            First argument being passed to the executable or batch file
	 * @param arg2
	 *            Second argument being passed to the executable or batch file
	 * @param arg3
	 *            Third argument being passed to the executable or batch file
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file, final String arg1,
			final String arg2, final String arg3) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file, arg1,
					arg2, arg3);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}

	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * One arguments is pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @param arg1
	 *            First argument being passed to the executable or batch file
	 * @param arg2
	 *            Second argument being passed to the executable or batch file
	 * @param arg3
	 *            Third argument being passed to the executable or batch file
	 * @param arg4
	 *            Fourth argument being passed to the executable or batch file
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file, final String arg1,
			final String arg2, final String arg3, final String arg4) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file, arg1,
					arg2, arg3, arg4);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}

	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * One arguments is pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @param arg1
	 *            First argument being passed to the executable or batch file
	 * @param arg2
	 *            Second argument being passed to the executable or batch file
	 * @param arg3
	 *            Third argument being passed to the executable or batch file
	 * @param arg4
	 *            Fourth argument being passed to the executable or batch file
	 * @param arg5
	 *            Fifth argument being passed to the executable or batch file
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file, final String arg1,
			final String arg2, final String arg3, final String arg4,
			final String arg5) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file, arg1,
					arg2, arg3, arg4, arg5);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}

	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * One arguments is pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @param arg1
	 *            First argument being passed to the executable or batch file
	 * @param arg2
	 *            Second argument being passed to the executable or batch file
	 * @param arg3
	 *            Third argument being passed to the executable or batch file
	 * @param arg4
	 *            Fourth argument being passed to the executable or batch file
	 * @param arg5
	 *            Fifth argument being passed to the executable or batch file
	 * @param arg6
	 *            Sixth argument being passed to the executable or batch file
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file, final String arg1,
			final String arg2, final String arg3, final String arg4,
			final String arg5, final String arg6) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file, arg1,
					arg2, arg3, arg4, arg5, arg6);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}

	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * One arguments is pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @param arg1
	 *            First argument being passed to the executable or batch file
	 * @param arg2
	 *            Second argument being passed to the executable or batch file
	 * @param arg3
	 *            Third argument being passed to the executable or batch file
	 * @param arg4
	 *            Fourth argument being passed to the executable or batch file
	 * @param arg5
	 *            Fifth argument being passed to the executable or batch file
	 * @param arg6
	 *            Sixth argument being passed to the executable or batch file
	 * @param arg7
	 *            Sixth argument being passed to the executable or batch file
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file, final String arg1,
			final String arg2, final String arg3, final String arg4,
			final String arg5, final String arg6, final String arg7) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file, arg1,
					arg2, arg3, arg4, arg5, arg6, arg7);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}
	
	/**
	 * Runs an executable or batch file and waits for it to execute.
	 * 
	 * One arguments is pass to the executable or batch file
	 * 
	 * @param file
	 *            Complete path and filename of the executable or batch file
	 * @param arg1
	 *            First argument being passed to the executable or batch file
	 * @param arg2
	 *            Second argument being passed to the executable or batch file
	 * @param arg3
	 *            Third argument being passed to the executable or batch file
	 * @param arg4
	 *            Fourth argument being passed to the executable or batch file
	 * @param arg5
	 *            Fifth argument being passed to the executable or batch file
	 * @param arg6
	 *            Sixth argument being passed to the executable or batch file
	 * @param arg7
	 *            Sixth argument being passed to the executable or batch file
	 * @param arg8
	 *            Eight argument being passed to the executable or batch file
	 * @return <li><code>true</code> if the file was executed</li> <li>
	 *         <code>false</code> otherwise.</li>
	 */
	public static boolean run(final String file, final String arg1,
			final String arg2, final String arg3, final String arg4,
			final String arg5, final String arg6, final String arg7, final String arg8) {
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(file, arg1,
					arg2, arg3, arg4, arg5, arg6, arg7, arg8);
			// merge stdout, stderr of process
			processBuilder.redirectErrorStream(true);

			Process process = processBuilder.start();
			OutputStream output = process.getOutputStream();
			InputStream input = process.getInputStream();

			new Thread(new Receiver(input)).start();
			new Thread(new Sender(output)).start();

			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			process.getErrorStream().close();

			return true;
		} catch (Exception e) {
			e.printStackTrace(); // or log it, or otherwise handle it
			return false;
		}
	}

	/**
	 * Re-starts Tomcat using a PowerShell script.
	 * 
	 * @return
	 * 	<li><code>True </code>if restarting Tomcat is success</li>
	 * 	<li>False <code>otherwise</code></li>
	 */
	public static boolean restartTomcat() {
		String batchFile = "C:\\Workspace\\AutoLibrary\\PSScripts\\ReStartTomCat.bat";
		
		return run(batchFile);		
	}

	/**
	 * Stops Tomcat using a PowerShell script.
	 * 
	 * @return
	 * 	<li><code>True </code>if stopping Tomcat is success</li>
	 * 	<li>False <code>otherwise</code></li>
	 */
	public static boolean stopTomcat() {
		String stopBatchFile = "C:\\Workspace\\AutoLibrary\\PSScripts\\StopTomCat.bat";
		
		return run(stopBatchFile);		
	}

	/**
	 * Starts Tomcat using a PowerShell script.
	 * 
	 * @return
	 * 	<li><code>True </code>if starting Tomcat is success</li>
	 * 	<li>False <code>otherwise</code></li>
	 */
	public static boolean startTomcat() {
		String startBatchFile = "C:\\Workspace\\AutoLibrary\\PSScripts\\StartTomCat.bat";
		
		return run(startBatchFile);
		
	}

	/**
	 * Stops services using Ant script.
	 * 
	 * @return
	 * 	<li><code>True </code>if stopping services is success</li>
	 * 	<li>False <code>otherwise</code></li>
	 */
	public static boolean stopServices() {
		String stopBatchFile = "C:\\Workspace\\AutoBrowser\\Project Files\\stopServices.bat";
		
		return run(stopBatchFile);		
	}
}

/**
 * Thread to send output to the child.
 */
final class Sender implements Runnable {
	// CONSTANTS
	/**
	 * e.g. \n \r\n or \r, whatever system uses to separate lines in a text
	 * file. Only used inside multiline fields. The file itself should use
	 * Windows format \r \n, though \n by itself will alsolineSeparator work.
	 */
	private static final String lineSeparator = System
			.getProperty("line.separator");

	// FIELDS
	/**
	 * Stream to send output to child on.
	 */
	private final OutputStream outputStream;

	// PUBLIC INSTANCE METHODS
	/**
	 * method invoked when Sender thread started. Feeds dummy data to child.
	 */
	public void run() {
		try {
			final BufferedWriter buffer = new BufferedWriter(
					new OutputStreamWriter(outputStream), 50 /*
															 * keep small for
															 * tests
															 */);
			for (int i = 99; i >= 0; i--) {
				buffer.write("There are " + i
						+ " bottles of beer on the wall, " + i
						+ " bottles of beer.");
				buffer.write(lineSeparator);
			}
			buffer.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException sending data to child process.");
		}
	}

	// CONSTRUCTOR
	/**
	 * Constructor.
	 * 
	 * @param output
	 *            stream to use to send data to child.
	 */
	Sender(final OutputStream output) {
		this.outputStream = output;
	}
}

/**
 * Thread to read output from child.
 */
class Receiver implements Runnable {
	// FIELDS
	/**
	 * Stream to receive data from child.
	 */
	private final InputStream inputStream;

	// PUBLIC INSTANCE METHODS
	/**
	 * Method invoked when Receiver thread started. Reads data from child and
	 * displays in on System.out.
	 */
	public void run() {
		try {
			final BufferedReader buffer = new BufferedReader(
					new InputStreamReader(inputStream), 50 /*
															 * keep small for
															 * testing
															 */);
			String line;
			while ((line = buffer.readLine()) != null) {
				System.out.println(line);
			}
			buffer.close();
		} catch (IOException e) {
			throw new IllegalArgumentException(
					"IOException receiving data from child process.");
		}
	}

	// CONSTRUCTORS
	/**
	 * Constructor.
	 * 
	 * @param input
	 *            stream to receive data from child
	 */
	Receiver(final InputStream input) {
		this.inputStream = input;
	}
}
