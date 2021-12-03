package com.qa.ims;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Runner {

	public static final Logger LOGGER = LogManager.getLogger();

	public static void runCommand(String command){
		try {
			String line;
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader input =
					new BufferedReader
							(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
		}
		catch (Exception err) {
			err.printStackTrace();
		}
	}

	public static void main(String[] args) {

		//runCommand("mysql -U root -d dbname -h serverhost -f sql-schema.sql");
		//runCommand("mysql -U root -d dbname -h serverhost -f sql-data.sql");

		IMS ims = new IMS();
		ims.imsSystem();
		LOGGER.info("SO LONG!");
	}

}
