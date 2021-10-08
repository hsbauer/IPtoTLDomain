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
		String accessPath = "/Users/bauerhs/git/IPtoTLDomain/src/nci/nih/gov/ipconverter/access.sample.log";
		String filePath = "myFile.txt";
		ProcessLog processor = new ProcessLog();
		CustomLog log = new CustomLog();
		Stream<String> lines = processor.readLogLine(args.length == 0?accessPath:args[0]);

		Hashtable<String, CustomLog.IpConsolidated> consolidated = new Hashtable<String, CustomLog.IpConsolidated>();
		lines.forEach(x -> log.mapLogLineToConsolidated(consolidated, x));
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(args.length == 0?filePath:args[1])))) {
			consolidated.values().stream().map(x -> log.createLogOutPutLineFromHashTable(x)).forEach(y -> {
				try {
					writer.write(y);
					writer.newLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} 
		catch (IOException ex) {
		   ex.printStackTrace();
		} 
	}

}
