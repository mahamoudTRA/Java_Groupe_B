import java.util.Arrays;
import java.util.List;

public class MyRunnable implements Runnable {
	
	private List<String[]> lignes;
		
	public List<String[]> getLignes() {
		return lignes;
	}
	
	public MyRunnable(List<String[]> lignes) {
		this.lignes = lignes;
	}
	
	public void run() {
		System.out.println(this.toString() + " start");
		int colonne; 
		
		for (String[] ligne : lignes) {
			colonne =  Arrays.asList(ligne).size()-1;
			ligne[colonne] = String.valueOf(Integer.valueOf(ligne[colonne])*2);
		}
		
		System.out.println(this.toString() + " end");
	}
}
