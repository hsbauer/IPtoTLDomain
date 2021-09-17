package nci.nih.gov.ipconverter;

public class IpConverter {

	public static void main(String[] args) {
		new IpConverter().run(args);

	}
	
	public void run(String ... args){
		//new ProcessLog().readLogLine("/Users/bauerhs/eclipse-workspace/IPtoTLDomain/src/nci/nih/gov/ipconverter/access.sample.log").forEach(x -> System.out.println(x));
		new ProcessLog().readLogLineToTestOutput("/Users/bauerhs/eclipse-workspace/IPtoTLDomain/src/nci/nih/gov/ipconverter/access.sample.log")//.forEach(x -> System.out.println(x));
		;
	}

}
