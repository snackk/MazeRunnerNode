import BIT.highBIT.*;
import BIT.lowBIT.*;
import java.io.*;
import java.util.*;

public class OpcodeMix {
	private static Map<String, Integer> instructionsMap = new HashMap<String,Integer>();
	
	public static void main (String argv[]) {
		File file_in = new File(argv[0]);
		String infilenames[] = file_in.list();

		for(int j = 0; j < infilenames.length; j++) {
			String infilename = infilenames[j];
			if (infilename.endsWith(".class")) {

				ClassInfo ci = new ClassInfo(argv[0] + System.getProperty("file.separator") + infilename);
			
				for (Enumeration e = ci.getRoutines().elements(); e.hasMoreElements(); ) {
					Routine routine = (Routine) e.nextElement();
					if(routine.getMethodName().equals("run")){
						for (Enumeration i = routine.getInstructionArray().elements(); i.hasMoreElements();){
							Instruction instr = (Instruction) i.nextElement();
							String instrType = InstructionTable.InstructionTypeName[InstructionTable.InstructionTypeTable[instr.getOpcode()]]; 
							instr.addBefore("OpcodeMix", "registerInstrType", instrType);
						}
						routine.addAfter("OpcodeMix", "printMix", "whatever");
					}
				}
				ci.write(argv[1] + System.getProperty("file.separator") + infilename);
			}
		}
	}

	public static synchronized void registerInstrType(String instrType) { 
		Integer count = new Integer(0);
		if(instructionsMap.containsKey(instrType)){
			count = instructionsMap.get(instrType) + new Integer(1);
		}
		
		instructionsMap.put(instrType, count);
	}

	public static synchronized void printMix(String foo){
		String pathToFile = "/home/ec2-user/";
		String outputFilename = "metrics.txt";
		try{
			FileWriter fw = new FileWriter(pathToFile + outputFilename,true );
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append("===================\n");
			Iterator it = instructionsMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				bw.append(pair.getKey() + " = " + pair.getValue() + "\n");
				it.remove();
			}
			bw.close();
		} catch(IOException ioe){
			ioe.printStackTrace();	
		}
	}
}
