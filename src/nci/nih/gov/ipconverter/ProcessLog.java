package nci.nih.gov.ipconverter;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessLog {
	
	public List<String> readLogLine(String fileName) {
	
	         
	    Path path;
	    Stream<String> lines = null;
		try {
			//System.out.println(Paths.get(fileName).toString());
			path = Paths.get(fileName);
		    lines = Files.lines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 

	    List<String> ipList = lines.map(x->getIpFromLine(x))
	    		.distinct()
	    		.filter(x -> x != null)
	    		.map(y -> getTLDString(y))
	    		.collect(Collectors.toList());
	    return ipList;
	}
	
	public List<String> readLogLineToTestOutput(String fileName) {
		
	    Path path;
	    Stream<String> lines = null;
		try {
			//System.out.println(Paths.get(fileName).toString());
			path = Paths.get(fileName);
		    lines = Files.lines(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 

	    lines.map(x->getIpFromLineTest(x))
	    		.distinct()
	    		.filter(x -> x != null)
//	    		.map(y -> getTimeStampFromLine(y))
	    		.forEach(z -> System.out.println(z));
//	    return ipList;
//		
//		lines.forEach(x -> System.out.println(x));
		return null;
	}

	
	public List<String> readLogLineWhois(String fileName) {
		
        
	    Path path;
	    Stream<String> lines = null;
		try {
			path = Paths.get(fileName);
		    lines = Files.lines(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
 

	    List<String> ipList = lines.map(x->getIpFromLine(x))
	    		.distinct()
	    		.filter(x -> x != null)
	    		.map(y -> whois(y))
	    		.collect(Collectors.toList());
	    return ipList;
	}
    
    public String getIpFromLine( String line) {
    	String ip = null;
        final String regex = "^(\\S+) (\\S+) (\\S+) " + 
                "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)" + 
                " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)";
   
        final Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		final Matcher matcher = pattern.matcher(line);
		if(matcher.find()) {
			ip = matcher.group(1);
		}
    	return ip;
    }
    
    public String getTimeStampFromLine( String line) {
    	String ip = null;
        final String regex =  "(\\[\\d{2}\\/[a-z,A-Z]+\\/\\d{4}:\\d{2}:\\d{2}:\\d{2} \\+\\d{4}\\])";
   
        final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(line);
		if(matcher.find()) {
			ip = matcher.group(1);
		}
    	return ip;
    }
    
    
    public String getLengthFromLine( String line) {
    	String ip = null;
        final String regex =  "[\\d\\.]+ - - \\[\\d+\\/\\w+\\S+ \\S+\\] \"[\\S ]+ \\d{3} (\\d+)";
   
        final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(line);
		if(matcher.find()) {
			ip = matcher.group(1);
		}
    	return ip;
    }

    
    public String getIpFromLineTest( String line) {
    	String ip = null;
        final String regex = 
        		"^([\\d\\/.]+)";
//        		(\\S+) (\\S+) " 
//    	+ 
//                "\\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+)" 
//               + 
//          " (\\S+)\\s*(\\S+)?\\s*\" (\\d{3}) (\\S+)"
        		;
   
        final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(line);
		if(matcher.find()) {
			ip = matcher.group(1);
		}
    	return ip;
    }
    
    public String getTLDString(String IP) {

    		StringBuffer output = new StringBuffer();
    		
    		//Create ProcessBuilder instance
            java.lang.ProcessBuilder processBuilder = new java.lang.ProcessBuilder("dig","-x", IP, "+short");
            
            Process process;
			try {
				process = processBuilder.start();

            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
            	output.append(line + "\n");
            }
            
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return output.toString();
    	}
    
	public String whois(String host) {
		String serverName = System
				.getProperty("WHOIS_SERVER", "whois.arin.net");
		InetAddress server = null;
		try {
			server = InetAddress.getByName(serverName);
			Socket theSocket = new Socket(server, 43);
			Writer out = new OutputStreamWriter(theSocket.getOutputStream(),
					"8859_1");
			
			//pass in the current host to the whois
			out.write(host + "\r\n");
			out.flush();

			InputStream in = new BufferedInputStream(theSocket.getInputStream());
			int c;
			StringBuffer response = new StringBuffer();
			while ((c = in.read()) != -1) {
				// System.out.write(c);
				response.append((char) c);
				// response.append("\r\n");
			}
			return response.toString();
			// whois_parser(response.toString());
		} catch (UnknownHostException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}
    
}
