package org.spacehq.iirc.util;

import org.spacehq.iirc.IrcProtocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PomUtil {
	public static String getVersion() {
		String result = "DEV";
		InputStream stream = IrcProtocol.class.getClassLoader().getResourceAsStream("/META-INF/maven/org.spacehq/iirc/pom.properties");
		Properties properties = new Properties();
		if(stream != null) {
			try {
				properties.load(stream);
				result = properties.getProperty("version");
			} catch(IOException e) {
				System.err.println("Could not retrieve client version.");
				e.printStackTrace();
			}
		}

		return result;
	}
}
