import com.gtranslate.Language;
import com.gtranslate.Translator;


public class Translate {
	
	public Translate(){
		
	}
	
	public static void main(String[] args){
		Translator translate = Translator.getInstance();
		String text = translate.translate("Hello how are you", Language.ENGLISH, Language.FRENCH);
		System.out.println(text); 
	}

}
