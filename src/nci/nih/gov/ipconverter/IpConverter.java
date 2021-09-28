package nci.nih.gov.ipconverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IpConverter {

	public static void main(String[] args) {
		new IpConverter().run(args);

	}
	
	public void run(String ... args){
		ProcessLog processor = new ProcessLog();
		CustomLog log = new CustomLog();
		Stream<String> lines = processor.readLogLine("/Users/bauerhs/git/IPtoTLDomain/src/nci/nih/gov/ipconverter/access.sample.log");
//		lines.map(x -> log.createLogOutPutLineFromHashTable(processor, x)).forEach(y -> System.out.println(y));
		Hashtable<String, CustomLog.IpConsolidated> consolidated = new Hashtable<String, CustomLog.IpConsolidated>();
		lines.forEach(x -> log.mapLogLineToConsolidated(consolidated, x));
//		consolidated.values().stream().map(x -> log.createLogOutPutLineFromHashTable(x)).forEach(y -> System.out.println(y));
//		consolidated.values().stream().forEach(x -> System.out.println("IP: " + x.ip + " TimeStamp: " + x.tmstp + " Size: " + x.size));
//		 new ProcessLog().readLogLineToTestOutput("/Users/bauerhs/eclipse-workspace/IPtoTLDomain/src/nci/nih/gov/ipconverter/access.sample.log");//.forEach(x -> System.out.println(x));
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("myFile.txt")))) {
			consolidated.values().stream().map(x -> log.createLogOutPutLineFromHashTable(x)).forEach(y -> {
				try {
					writer.write(y);
					writer.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		} 
		catch (IOException ex) {
		   ex.printStackTrace();
		} 
	}

}
