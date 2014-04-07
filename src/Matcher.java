import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Matcher {
	
	private ArrayList<String> greetings;
    private ArrayList<String> closures;
    private ArrayList<String> affirmations;
    private ArrayList<String> negations;
    private ArrayList<String> compliments;
    private ArrayList<String> acknowledgements;
    private ArrayList<String> adverbs;
    private ArrayList<String> verbs;
    private ArrayList<String> pronouns;
    private ArrayList<String> topics;
    private ArrayList<String> master;
    
	private int msize;
	
    public Matcher(){
        greetings = Populate.greetings();
        closures = Populate.closures();
        affirmations = Populate.affirmations();
        negations = Populate.negations();
        compliments = Populate.compliments();
        acknowledgements = Populate.acknowledgements();
        adverbs = Populate.adverbs();
        verbs = Populate.verbs();
        pronouns = Populate.pronouns();
        topics = Populate.topiclist();
        
        master = new ArrayList<String>();
        master.addAll(greetings);
        master.addAll(closures);
        master.addAll(affirmations);
        master.addAll(negations);
        master.addAll(compliments);
        master.addAll(acknowledgements);
        master.addAll(adverbs);
        master.addAll(verbs);
        master.addAll(pronouns);
        master.addAll(topics);
        msize = master.size();
        //System.out.println("master list contains " + msize + " words");
        //System.out.println(master.toString());
        
    }
    
   
    public String fixSentence(String str, String topic){
        master.addAll(Populate.details(topic).keySet());
        msize = master.size();
    	String[] arr = breakSen(str);
    	String[] newArr = fixArr(arr);
    	String newSen = buildSen(newArr);
    	return newSen;
    }
    
    /** This method uses the arrayList master as a dictionary, and spell checks against every word 
     * in it to find the match with the smallest levenshtein distance
     * 
     * @param str - the word you want to check the spelling of
     * @return - the spell-checked string
     */
    public String fixSentence1(String str){
    	String[] arr = breakSen(str);
    	String[] newArr = fixArr(arr);
    	String newSen = buildSen(newArr);
    	return newSen;
    }
    

	/**This method breaks a string into a string array
	 * 
	 * @param str 
	 * @return string array
	 */
	private String[] breakSen(String str){
		String[] sar = str.split(" ");
		for(int i = 0;i<sar.length;i++){
			sar[i].replaceAll("\\s+","");
		}
		return sar;
	}
	
	/** This method computes the levenshtein distance between 2 words, which
	 * tells how similar 2 words are. (the smaller the lev distance the more similar)
	 * 
	 * @param s1
	 * @param len1
	 * @param s2
	 * @param len2
	 * @return
	 */
	private int levDis(String s1, int len1, String s2, int len2){
		int val;
		//case 1: strings are empty
		if(Math.abs(len1-len2)>5)
			return 5;
		if(len1==0)
			return len2;
		if(len2==0)
			return len1;
		//case 2: strings arent empty, check last character
		if(s1.charAt(len1-1)==s2.charAt(len2-1))
			val = 0;
		else
			val = 1;
		//min from delete from s1, delete form s2, delete form both
		int toReturn = Math.min(levDis(s1, len1-1, s2, len2)+1,
				Math.min(levDis(s1, len1, s2, len2-1)+1,
				levDis(s1, len1-1, s2, len2-1)+val));
		if(toReturn>2)
			return 10;
		return toReturn;
	}
	
	/**This method checks the lev distance between all words in the array parameter and the master library
	 * 
	 * @param arr
	 * @return
	 */
	private String[] fixArr(String[] arr){
		int j;
		int currentLD = 6;
		String prospect = "";
		for (int i=0; i<arr.length; i++){
			if (arr[i].length() > 1) {
				for (j = 0; j < msize; j++) {
					String tempSt = master.get(j).toLowerCase().replaceAll("\\s+","");
					int tempLD = levDis(tempSt.toLowerCase(), tempSt.length(), arr[i].toLowerCase(),
							arr[i].length());
					//System.out.println("Levenshtein Distance from "+tempSt+" to "+arr[i]+": "+tempLD);
					if (tempLD < currentLD) {
						currentLD = tempLD;
						prospect = tempSt;
					}
					else if(tempLD == currentLD && tempSt.charAt(0) == arr[i].charAt(0)){
						currentLD = tempLD;
						prospect = tempSt;
					}
				}
				currentLD = 6;
				//System.out.println(arr[i] + "was compared to "+j+" words and was interpreted as " + prospect);
				arr[i] = prospect;
			}
		}
		return arr;
	}
	
	/**This method rebuilds the correct sentence from a string array
	 * 
	 * @param arr
	 * @return
	 */
	private String buildSen(String[] arr){
		String sen = "";
		for (int i = 0 ; i < arr.length; i++){
			sen = sen + arr[i] + " ";
		}
		return sen;
		
	}

}
