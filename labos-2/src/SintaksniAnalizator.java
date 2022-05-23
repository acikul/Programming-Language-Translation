import java.io.*;
import java.util.*;


public class SintaksniAnalizator {
	
	public static class Par{
		public final int brRazmak;
		public final String s;
		
		public Par(int br, String tekst) {
			this.brRazmak = br;
			this.s = tekst;
		}
		
		@Override
		public String toString() {
			String pomocniZaIzlaz = "";
			for (int i=0; i<brRazmak; i++) {
				pomocniZaIzlaz += " ";
			}
			return pomocniZaIzlaz+s;
		}
	}
	
	public static List<String> ulazniNiz = new ArrayList<String>();
	public static List<Par> izlaz = new ArrayList<Par>();
	public static int ulPozicija = 0;
	

	public static void main(String[] args) throws IOException {
		
		//ucitavanje ulaza
		BufferedReader citac = new BufferedReader(new InputStreamReader(System.in));
		
		for (String redak = citac.readLine(); redak != null; redak = citac.readLine()){
				ulazniNiz.add(redak);
		}
		ulazniNiz.add("krajUlNiz");
		
		//System.out.println(ulazniNiz);
		
		//poziv defaultnog potprograma
		program();
		if (ulazniNiz.get(ulPozicija).strip().equals("krajUlNiz")) {
			for (Par p : izlaz) {
				System.out.println(p);
			}
		} else {
			greska("main");
		}
	}


	public static void program() {
		int razina = 0;
		Par parZaIzlaz = new Par(razina, "<program>");
		izlaz.add(parZaIzlaz);
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
				
		if (temp.equals("IDN") || temp.equals("KR_ZA") || temp.equals("krajUlNiz")) {
			lista_naredbi(razina+1);
		} else {
			greska("program");
		}
	}
	
	public static void lista_naredbi(int lvl) {
		
		int razina = lvl;
		Par parZaIzlaz = new Par(razina, "<lista_naredbi>");
		izlaz.add(parZaIzlaz);
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("IDN") || temp.equals("KR_ZA")) {
			naredba(razina+1);
			lista_naredbi(razina+1);
		} else if (temp.equals("KR_AZ") || temp.equals("krajUlNiz")) {
			epsilon(razina+1);
		} else {
			greska("lista naredbi");
		}
	}
	
	public static void naredba(int lvl) {

		int razina = lvl;
		izlaz.add(new Par(razina, "<naredba>"));
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("IDN")) {
			naredba_pridruzivanja(razina+1);
		} else if(temp.equals("KR_ZA")) {
			za_petlja(razina+1);
		} else {
			greska("naredba");
		}
	}
	
	public static void naredba_pridruzivanja(int lvl) {
		int razina = lvl;
		izlaz.add(new Par(razina, "<naredba_pridruzivanja>"));
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("IDN")) {
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			
			if (ulazniNiz.get(ulPozicija).split(" ")[0].strip().equals("OP_PRIDRUZI")) {
				izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
				ulPozicija++;
			} else {
				greska("naredba pridruzivanja op pridruzi");
			}
			
			E(razina+1);
		} else {
			greska("naredba pridruzivanja idn");
		}
	}
	
	public static void za_petlja(int lvl) {
		int razina = lvl;
		izlaz.add(new Par(razina, "<za_petlja>"));
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("KR_ZA")){
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			
			if (ulazniNiz.get(ulPozicija).split(" ")[0].strip().equals("IDN")) {
				izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
				ulPozicija++;
			} else {
				greska("za petlja idn");
			}
			
			if (ulazniNiz.get(ulPozicija).split(" ")[0].strip().equals("KR_OD")) {
				izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
				ulPozicija++;
			} else {
				greska("za petlja kr od");
			}
			
			E(razina+1);
			
			if (ulazniNiz.get(ulPozicija).split(" ")[0].strip().equals("KR_DO")) {
				izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
				ulPozicija++;
			} else {
				greska("za petlja kr do");
			}
			
			E(razina+1);
			lista_naredbi(razina+1);
			
			if (ulazniNiz.get(ulPozicija).split(" ")[0].strip().equals("KR_AZ")) {
				izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
				ulPozicija++;
			} else {
				greska("za petlja kr az");
			}
			
		} else {
			greska("za petlja kr za");
		}
	}
	
	public static void E(int lvl) {
		int razina = lvl;
		izlaz.add(new Par(razina, "<E>"));
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("IDN") || temp.equals("BROJ") || temp.equals("OP_PLUS") || temp.equals("OP_MINUS") || temp.equals("L_ZAGRADA")) {
			T(razina+1);
			E_lista(razina+1);
		} else {
			greska("greska e");
		}
	}
	
	public static void E_lista(int lvl) {
		int razina = lvl;
		izlaz.add(new Par(razina, "<E_lista>"));
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("OP_PLUS")) {
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			E(razina+1);
			
		} else if (temp.equals("OP_MINUS")){
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			E(razina+1);
			
		} else if (temp.equals("IDN") || temp.equals("KR_ZA") || temp.equals("KR_DO") || temp.equals("KR_AZ") || temp.equals("D_ZAGRADA") || temp.equals("krajUlNiz")) {
			epsilon(razina+1);
			
		} else {
			greska("E lista");
		}
	}
	
	public static void T(int lvl) {
		int razina = lvl;
		izlaz.add(new Par(razina, "<T>"));
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("IDN") || temp.equals("BROJ") || temp.equals("OP_PLUS") || temp.equals("OP_MINUS") || temp.equals("L_ZAGRADA")){
			P(razina+1);
			T_lista(razina+1);
		} else {
			greska("t");
		}
	}

	public static void T_lista(int lvl) {
		int razina = lvl;
		izlaz.add(new Par(razina, "<T_lista>"));
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("OP_PUTA")) {
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			T(razina+1);
			
		} else if (temp.equals("OP_DIJELI")){
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			T(razina+1);
			
		} else if (temp.equals("IDN") || temp.equals("KR_ZA") || temp.equals("KR_DO") || temp.equals("KR_AZ") || temp.equals("OP_PLUS") || temp.equals("OP_MINUS") || temp.equals("D_ZAGRADA") || temp.equals("krajUlNiz")) {
			epsilon(razina+1);
			
		} else {
			greska("greska T lista");
		}
	}
	
	public static void P(int lvl) {
		int razina = lvl;
		izlaz.add(new Par(razina, "<P>"));
		String temp = ulazniNiz.get(ulPozicija).split(" ")[0].strip();
		
		if (temp.equals("OP_PLUS")) {
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			P(razina+1);
			
		} else if (temp.equals("OP_MINUS")){
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			P(razina+1);
			
		} else if (temp.equals("L_ZAGRADA")) {
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			
			E(razina+1);
			
			if (ulazniNiz.get(ulPozicija).split(" ")[0].strip().equals("D_ZAGRADA")) {
				izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
				ulPozicija++;
			} else {
				greska("greska p d zagrada");
			}
		} else if (temp.equals("IDN")) {
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			
		} else if (temp.equals("BROJ")) {
			izlaz.add(new Par(razina+1, ulazniNiz.get(ulPozicija).strip()));
			ulPozicija++;
			
		} else {
			greska("greska p");
		}
	}
	
	public static void epsilon(int lvl) {
		int razina = lvl;
		izlaz.add(new Par(razina, "$"));
	}
	
	public static void greska(String opis) {
		//System.out.println(opis);
		
		if (ulazniNiz.get(ulPozicija).strip().equals("krajUlNiz")) {
			System.out.println("err kraj");
		} else {
			System.out.println("err " + ulazniNiz.get(ulPozicija).strip());
		}
		System.exit(1);
	}
	
}

