import java.io.*;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.io.FilenameUtils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class FileProcessing {
	private List<Thread> MesThreads = null;
	private boolean isEndProcess() {
		boolean reponse = true;
		for (Thread thread : MesThreads) {
			if (thread.isAlive()) {
				reponse = false;
				break;
			}
		}
		return reponse;
	}
	public void processFile(String pathFile, int pas) {
		File fileIn = new File(Paths.get(pathFile).toUri());
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(fileIn);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		CSVReader csvReader = new CSVReader(fileReader, ';');
		
		String nameFile = fileIn.getName();
		String SimpleName = (nameFile != null) ? nameFile.substring(0,nameFile.indexOf('.')) : "";
		String extension = FilenameUtils.getExtension(nameFile);
		StringBuffer sb = new StringBuffer();
		sb.append(SimpleName).append("_output.").append(extension);
		String nameFileOut = sb.toString();
		System.out.println("Génération du fichier de sortie " + nameFileOut);
		
		File fichierOut = new File(fileIn.getParentFile(), nameFileOut);
	    FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fichierOut);
		} catch (IOException e) {
			e.printStackTrace();
		}

	    CSVWriter cvsWriter = new CSVWriter(fileWriter,';');
	    
	    try {
	    	System.out.println("Découpage du fichier...");
	    	int i = 1;
	    	Thread thread = null;
	    	MyRunnable myRunnable = null;
	    	List<String[]> lignes = new ArrayList<String[]>();
	    	MesThreads = new ArrayList<Thread>();

	    	Map<Thread, MyRunnable> mapDesRunnableThread = new HashMap<Thread, MyRunnable>();
	    	String[] nextLine = null;

	    	while ((nextLine = csvReader.readNext()) != null) {
				lignes.add(nextLine);
				if ( i == pas ) { 
					myRunnable = new MyRunnable(lignes);
					thread = new Thread(myRunnable);
					MesThreads.add(thread);
					mapDesRunnableThread.put(thread, myRunnable);
					lignes = new ArrayList<String[]>(); 
					i = 1;
				} else {
					i++;
				}
			}
	    	
			System.out.println("Threads préparés.. On les lance");
			for (Thread leThread : MesThreads) {
				leThread.start();
			}
			System.out.println("Demarrage de mes threads");
			
			System.out.println("On attends la fin des threads");
			while(!isEndProcess());
			System.out.println("traitement achevé");


			for (Thread leThread : MesThreads) {
				List<String[]> mesLignesModifiee = mapDesRunnableThread.get(leThread).getLignes();
				for (String[] ligneModifiee : mesLignesModifiee) {
					cvsWriter.writeNext(ligneModifiee);
				}
			}
			System.out.println("Le fichier est fourni des lignes traitées");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    try {
	    	fileReader.close();
	    	csvReader.close();
	    	fileWriter.close();
			cvsWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
