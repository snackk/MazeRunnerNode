import BIT.highBIT.*;
import BIT.lowBIT.*;
import java.io.*;
import java.util.*;
import java.lang.Thread;


public class Tool {

	private static int b_count = 0;

	public static void main(String argv[]) {

		File file_in = new File(argv[0]);
		String filelist[] = file_in.list();
		
		for (int i = 0; i < filelist.length; i++) {
			String filename = filelist[i];
			if(filename.endsWith(".class")) {
				ClassInfo ci = new ClassInfo(argv[0] + System.getProperty("file.separator") + filename);

				for (Enumeration e = ci.getRoutines().elements(); e.hasMoreElements(); ) {
					Routine routine = (Routine) e.nextElement();
					if (ci.getClassName().equals("pt/ulisboa/tecnico/meic/cnv/mazerunner/maze/strategies/AStarStrategy")||
					    ci.getClassName().equals("pt/ulisboa/tecnico/meic/cnv/mazerunner/maze/strategies/BreadthFirstSearchStrategy")) {
						if (routine.getMethodName().equals("run")) {
							for (Enumeration b = routine.getBasicBlocks().elements(); b.hasMoreElements(); ) {
								BasicBlock bb = (BasicBlock) b.nextElement();
								bb.addBefore("Tool", "countBB", new Integer(1));
							}
							routine.addAfter("Tool", "register", ci.getClassName());
						}
					}
					else if (ci.getClassName().equals("pt/ulisboa/tecnico/meic/cnv/mazerunner/maze/strategies/DepthFirstSearchStrategy")) {
						if (routine.getMethodName().equals("solveAux")) {
							for (Enumeration b = routine.getBasicBlocks().elements(); b.hasMoreElements(); ) {
								BasicBlock bb = (BasicBlock) b.nextElement();
								bb.addBefore("Tool", "countBB", new Integer(1));
							}
						}
						else if (routine.getMethodName().equals("run")) {
							routine.addAfter("Tool", "register", ci.getClassName());
							
						}
					}	
		
				}
				ci.write(argv[1] + System.getProperty("file.separator") + filename);
			}
		}	
	}

	public static synchronized void register(String foo) {
		String pathToFile = "/home/ec2-user/";
		String outputFilename = "metrics.txt";
		try{
			FileWriter fw = new FileWriter(pathToFile + outputFilename,true );
			BufferedWriter bw = new BufferedWriter(fw);
			bw.append("===Thread ID = " + Thread.currentThread().getId() + "===\n");
			bw.append("BBL's = " + b_count + "\n");	
			bw.close();
		} catch(IOException ioe){
			ioe.printStackTrace();	

		}
	}	
	
	public static synchronized void countBB(int incr) {
		b_count += incr;
	}
	
}
