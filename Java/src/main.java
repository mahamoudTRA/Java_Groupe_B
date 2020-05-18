
import java.time.LocalDateTime;

public class main {
	
	public static void main(String[] args) {
		System.out.println("demarrage programme");
		System.out.println("date time start " + LocalDateTime.now());
		FileProcessing fp = new FileProcessing();
		// On la machine a thread, pour chaque lot de 1000 lignes, on declenche un thread
		fp.processFile("src\\data.csv", 1000);
		System.out.println("date time end " + LocalDateTime.now());
	}
}
