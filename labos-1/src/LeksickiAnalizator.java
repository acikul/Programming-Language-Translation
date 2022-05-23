import java.io.*;
import java.util.*;

public class LeksickiAnalizator {
	
	public static Integer LN;
	
	//uniformni znakovi s kojima leksicki analizator radi
	public static enum UniformZnak {
		IDN, BROJ, OP_PRIDRUZI, OP_PLUS, 
		OP_MINUS, OP_PUTA, OP_DIJELI, L_ZAGRADA,
		D_ZAGRADA, KR_ZA, KR_OD, KR_DO, KR_AZ
	}
	
	//leksicka jedinka, toString override za uredeni ispis
	public static class Jedinka{
		public final UniformZnak u;		//uniformni znak te lex jedinke
		public final Integer ln;		//broj retka u kojoj se nalazi
		public final String lexJed;		//sama lex jedinka
		
		public Jedinka(UniformZnak u, Integer ln, String lexJed) {
			this.u = u;
			this.ln = ln;
			this.lexJed = lexJed;
		}

		@Override
		public String toString() {
			return u + " " + ln + " " + lexJed;
		}
	}
	
	//funkcija u koju se ulazi citanjem leksicke jedinke koja pocinje znamenkom
	public static String getBroj(String s, int i) {
		int j = i;
		for (; j < s.length() ;) {
			if (Character.isDigit(s.charAt(j))) {
				j++;
			} else {
				return s.substring(i, j);
			}
		}
		return s.substring(i, j);
	}
	
	//funkcija u koju se ulazi citanjem leksicke jedinke koja pocinje slovom,
	public static String getIDN(String s, int i) {
		
		int j = i;
		for (; j < s.length() ;) {
			if (Character.isLetterOrDigit(s.charAt(j))) {
				j++;
			} else {
				return s.substring(i, j);
			}
		}
		return s.substring(i, j);
	}
	
	
	//glavna analizatorska funkcija koju ce se pozivati za svaki procitani redak
	public  static List<Jedinka> analiza(String redak){
		
		List<Jedinka> rezultat = new ArrayList<LeksickiAnalizator.Jedinka>();
		
		for (int i=0; i<redak.length(); ) {
			
			switch(redak.charAt(i)){
			case '=':
				rezultat.add(new Jedinka(UniformZnak.OP_PRIDRUZI, LN, "="));
				i++;
				break;
			case '+':
				rezultat.add(new Jedinka(UniformZnak.OP_PLUS, LN, "+"));
				i++;
				break;
			case '-':
				rezultat.add(new Jedinka(UniformZnak.OP_MINUS, LN, "-"));
				i++;
				break;
			case '*':
				rezultat.add(new Jedinka(UniformZnak.OP_PUTA, LN, "*"));
				i++;
				break;
			case '/':
				if(i+1<redak.length() && redak.charAt(i+1) == '/') {
					i=redak.length();
					break;
				} else {
					rezultat.add(new Jedinka(UniformZnak.OP_DIJELI, LN, "/"));
					i++;
					break;
				}
			case '(':
				rezultat.add(new Jedinka(UniformZnak.L_ZAGRADA, LN, "("));
				i++;
				break;
			case ')':
				rezultat.add(new Jedinka(UniformZnak.D_ZAGRADA, LN, ")"));
				i++;
				break;
				
			default:
				if (Character.isWhitespace(redak.charAt(i))) {
					i++;
				} else if (Character.isDigit(redak.charAt(i))) {
					String var = getBroj(redak, i);
					i += var.length();
					rezultat.add(new Jedinka(UniformZnak.BROJ, LN, var));
				} else {
					String var = getIDN(redak, i);
					i += var.length();
					switch(var) {
					case "za":
						rezultat.add(new Jedinka(UniformZnak.KR_ZA, LN, "za"));
						break;
					case "az":
						rezultat.add(new Jedinka(UniformZnak.KR_AZ, LN, "az"));
						break;
					case "do":
						rezultat.add(new Jedinka(UniformZnak.KR_DO, LN, "do"));
						break;
					case "od":
						rezultat.add(new Jedinka(UniformZnak.KR_OD, LN, "od"));
						break;
					default:
						rezultat.add(new Jedinka(UniformZnak.IDN, LN, var));
						break;
					}
				}
				break;
			}
		}
		
		return rezultat;
	}
	

	//main funkcija u kojoj se citaju retci 
	//i za svaki poziva funkcija analize
	public static void main(String[] args) throws IOException {
		BufferedReader citac = new BufferedReader(new InputStreamReader(System.in));
		
		LN = 1;
		List<Jedinka> analizirani = new ArrayList<Jedinka>();
		
		for (String redak = citac.readLine(); redak != null; redak= citac.readLine()) {
			analizirani.addAll(analiza(redak));
			LN++;
		}
		
		for (Jedinka lj : analizirani) {
			System.out.println(lj);
		}
	}

}
