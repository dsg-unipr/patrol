package it.unipr.ce.dsg.p2pgame.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * This class is useful for split and select output incoming from different sources
 * (e.g. thread or class).
 *
 * For configure sources by which you want or don't want log use configuration file.
 * Configuration file take a list of wanted sources and unwanted sources will be
 * preceded with #. If a source isn't specified in configuration file
 * message will be printed to standard output.
 *
 * Source identifier can't contain any space.
 *
 * The class is static.
 *
 * @author Stefano Sebastio (stefano.sebastio@studenti.unipr.it)
 *
 */
public class MultiLog {

	private static boolean multifileBool = false;
	private static boolean fileOutBool = false; //sysout on default

	private final String fileName = "MultiLog.log";

	private static ArrayList<String> sourceToLog;
	private static ArrayList<String> sourceToNotLog = new ArrayList<String>();;
	private static ArrayList<PrintStream> outStream = null;
	private static PrintStream out = null;
	private static MultiLog MultiLog;


	/**
	 *
	 * Constructor takes in input configuration file and option for file
	 * or multiple - file
	 *
	 * @param configFile path of configuration file for MultiLog
	 * @param fileOutBool set true if you want output file otherwise message will
	 * 			printed to standard output
	 * @param multiFile set true, in conjunction with fileOutBool parameter, to
	 * 			specify if you want one file or source (set to true) or a single file
	 *
	 */
	public MultiLog(String configFile, boolean fileOut, boolean multiFile){

		fileOutBool = fileOut;
		multifileBool = multiFile;

		FileReader reader = null;
		try {
			reader = new FileReader(configFile);
		} catch (FileNotFoundException e) {
			System.err.println("Configuration file for MultiLog not found ! ");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Configuration file found");

		//load class to log
		BufferedReader input = new BufferedReader(reader);
		sourceToLog = new ArrayList<String>();
		//classToNotLog = new ArrayList<String>();

		String line;
		try {
			while ( (line = input.readLine()) != null ){
				if ( !line.contains("#") )
					sourceToLog.add(line);
				else{
					sourceToNotLog.add(line.split("#")[0]);
				}
			}
		} catch (IOException e) {
			System.err.println("Configuration file for MultiLog malformed !");
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Configuration file read");

		//create file writer/s
		if (fileOut){

			//every output on a single file
			if (!multiFile){

				FileOutputStream file = null;
				try {
					file = new FileOutputStream(this.fileName);
				} catch (FileNotFoundException e) {
					System.err.println("Impossible to create " + this.fileName);
					e.printStackTrace();
					System.exit(1);
				}

				out = new PrintStream(file);
			}
			//must be create a file for every class to log
			else {

				outStream = new ArrayList<PrintStream>();

				for (int i = 0; i < sourceToLog.size(); i++){
					FileOutputStream file = null;
					try {
						file = new FileOutputStream(sourceToLog.get(i) + ".log");
					} catch (FileNotFoundException e) {
						System.err.println("Impossible to create " + sourceToLog.get(i) + ".log");
						e.printStackTrace();
						System.exit(1);
					}


					outStream.add(new PrintStream(file));
				}
			}
		}
		System.out.println("MultiLog constructor finished");
	}

	/**
	 *
	 * Return instance of MultiLog
	 *
	 * @return MultiLog
	 *
	 */
	public static MultiLog getInstance(){
		return MultiLog;
	}

	/**
	 *
	 * For print message passed.
	 *
	 * @param id source identifier
	 * @param text message to print
	 *
	 */
	public static void println(String id, String text){
		if (id.contains(" "))
			id = id.split(" ")[1];

		if (sourceToNotLog.contains(id)){
			return;
		}

		if (!fileOutBool){
			//System.out.println(id + " : " + text);
		}
		else if (fileOutBool){

			if (!multifileBool && sourceToLog.contains(id)){
				out.print(id + " : " + text);
			}
			else if (multifileBool && sourceToLog.contains(id)){
				int i = sourceToLog.indexOf(id);
				Calendar calendar = new GregorianCalendar();
				outStream.get(i).println(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + "_: " + text);
			}
		}
	}
}
