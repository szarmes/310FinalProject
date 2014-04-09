import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import com.gtranslate.Language;
import com.gtranslate.Translator;


public class Codebot {
        /*
         * This class is responsible for handling all user interaction. It is the central class.
         */

        /*
         * This is where our library gets stored into memory for fast access
         */
        private ArrayList<String> greetings;
        private ArrayList<String> closures;
        private ArrayList<String> affirmations;
        private ArrayList<String> negations;
        private ArrayList<String> prompts;
        private ArrayList<String> reprompts;
        private ArrayList<String> topicprompts;
        private ArrayList<String> inquiries;
        private ArrayList<String> compliments;
        private ArrayList<String> acknowledgements;
        private ArrayList<String> adverbs;
        private ArrayList<String> verbs;
        private ArrayList<String> pronouns;
        private HashMap<String,String> topics;
        private HashMap<String,String> details;
        private Scanner scan;
        private String lastSaid;
        private String lastSaidType;
        private String searchTerms;
        private String lastSaidByUser;
        private String lastUserTutor;
        private String lastSaidNoSpellCheck;
        private Matcher match;
        Socket mySkt;
        PrintStream myPS;
        BufferedReader myBR;
        private String language = "english";
        private Translator translate;
        private Tweeter tweeter;
        private Wolfy wolfy;
        
        /*
         * This is our constructor. It populates the library and begins the session
         */
        public Codebot(){
        	
				
                greetings = Populate.greetings();
                closures = Populate.closures();
                prompts = Populate.prompts();
                reprompts = Populate.reprompts();
                topicprompts = Populate.topicprompts();
                affirmations = Populate.affirmations();
                negations = Populate.negations();
                inquiries = Populate.inquiries();
                compliments = Populate.compliments();
                acknowledgements = Populate.acknowledgements();
                adverbs = Populate.adverbs();
                verbs = Populate.verbs();
                pronouns = Populate.pronouns();
                topics = Populate.topics();
                scan = new Scanner(System.in);
                lastSaid="";
                lastSaidType="";
               lastSaidByUser = "";
               lastSaidNoSpellCheck="";
               lastUserTutor = "array";  //stores the last subject the user was tutored on
                match = new Matcher();
                translate = Translator.getInstance();
                tweeter = new Tweeter();
                wolfy = new Wolfy();
                //beginSession();
               
                
        }
        
        /*
         * This method gets the ball rolling. It will greet the user and then scan for their response
         */
        private void beginSession() {
                Random rand = new Random();
                String greeting = greetings.get(rand.nextInt(greetings.size()));
                lastSaid = greeting;
                lastSaidType = "greeting";
                System.out.println(greeting.substring(1));
                String response = scan.nextLine() ;
                respond(response);
                
        }
        
        /*
        This method selects a random greeting and returns it
        */
        public String greet(){
            Random rand = new Random();
                String greeting = greetings.get(rand.nextInt(greetings.size()));
                lastSaid = greeting;
                lastSaidType = "greeting";
                //translate back to desired language
                if (language.equals("spanish")){
                	greeting = translate.translate(greeting, Language.ENGLISH, Language.SPANISH);
                }
                if (language.equals("french")){
                	greeting = translate.translate(greeting, Language.ENGLISH, Language.FRENCH);
               }
                return greeting;
        
        }

        /*
         * This method takes a string as an input and selects a valid response
         */
        public String respond(String response) {
        	boolean confused = false;
            String codebotResponse = "";
                if (response.isEmpty()){                        
                        codebotResponse = prompt();
                }
                else if (response.length()>5&&response.substring(0,6).toLowerCase().equals("who is")){
                	codebotResponse = wolfy.searchWolfram(response) + "\n" + reprompt();
                }
                else if (Comparison.containsOperation(response)){
                	codebotResponse = wolfy.searchWolfram(response) + "\n" + reprompt();
                }
                else{
                	
                	//translate to english for processing
                	  if (language.equals("spanish")){
                     	 response = translate.translate(response, Language.SPANISH, Language.ENGLISH);
                     }
                     if (language.equals("french")){
                    	 response = translate.translate(response, Language.FRENCH, Language.ENGLISH);
                    }
                     	lastSaidNoSpellCheck = response;
                
                      //  response = match.fixSentence(response,lastUserTutor); //passing in lastUserTutor will allow the spell check to check against more specifc phrases
                        response = Punctuation.space(response);        //correctly format their response for searching through libraries
                        
                        if (Comparison.contains(greetings,response)){
                                /*
                                 * If they greet codebot, codebot will reply with a prompt
                                 */
                                 codebotResponse = prompt();
                        } 
                        else if (Comparison.contains(affirmations, response)&&(lastSaidType.equals("prompt")||lastSaidType.equals("reprompt"))){
                                /*
                                 * If codebot prompted them, i.e. "Do you want help?" and they respond with yes (or any other affirmation)
                                 * then codebot inquires as to what they need help with
                                 */
                                 codebotResponse = inquire();
                        }
                         else if (lastSaidType.equals("googleprompt")){ //if we prompted to search and they affirmed, google it
                            if (Comparison.contains(affirmations,response)){
                            performGoogle(searchTerms);
                            }
                            codebotResponse = reprompt(); //prompt regardless of what they say
                        }
                         else if (lastSaidType.equals("twitterprompt")){ //if we prompted to get tweets and they affirmed, get tweets
                             if (Comparison.contains(affirmations,response)){
                             codebotResponse = getTweets(searchTerms);
                             }
                             codebotResponse += "\n"+reprompt(); //prompt regardless of what they say
                         }
                        else if (Comparison.contains(negations, response)&&lastSaidType.equals("prompt")){
                                /*
                                 * If codebot prompted them, i.e. "Do you want help?" and they respond with no (or any other negation)
                                 * then they are done and codebot ends the session. Might want to confirm they are done before ending
                                 * the session
                                 */
                                codebotResponse =  endSession();
                        }
                        else if (Comparison.contains(negations, response)&&(lastSaidType.equals("tutor")||lastSaidType.equals("reprompt"))){
                                /*
                                 * If they negate our topicprompt (dont need additional help for a topic), prompt them
                                 */
                        		details = null;
                                if (lastSaidType.equals("tutor")){
                                        codebotResponse =  reprompt();}
                                else {
                                      codebotResponse =    endSession();}
                        }
                        else if (details != null&&Comparison.contains(details, response)&&lastSaidType.equals("tutor")){
                                /*
                                 * If they affirm our topicprompt (do need additional help for a topic), instruct them
                                 */
                                 codebotResponse = instruct(response);
                        }
                        else if(details != null&&lastSaidType.equals("tutor")){
                        	codebotResponse = googlePrompt(response);
                        }
                        else if (details == null&&Comparison.contains(topics, response)){
                            /*
                             * Regardless of what was previously said, if they type a response that has a topic
                             * in our library, codebot responds with the basic information about that topic
                             */
                             codebotResponse = tutor(response);
                        }
                        else if (Comparison.contains(compliments, response)){
                                /*
                                 * If they compliment codebot, codebot acknowledges the compliment
                                 */
                                codebotResponse =  acknowledge();
                        }
                       
                        else if (Comparison.contains(closures,response)){
                                /*
                                 * If they say bye, or any other closure, codebot ends the session
                                 */
                                codebotResponse =  endSession();
                        }
                        else{
                                /*
                                 * In the worst case, codebot asks if they want it to look up the answer for them
                                 */
                        		confused = true;
                        		Random rand = new Random();
                        		int m = rand.nextInt(2);
                        		if(m==0){
                                codebotResponse = googlePrompt(response);}
                        		else
                        		{
                        			codebotResponse = twitterPrompt(response);
                        		}
                        }
                }
                lastSaidByUser = response;
                //translate back to desired language
                if (language.equals("spanish")){
                    codebotResponse = Punctuation.compress(codebotResponse);
                    System.out.println(codebotResponse);
                	 codebotResponse = translate.translate(codebotResponse, Language.ENGLISH, Language.SPANISH);
                	 if (confused==true)
                     	codebotResponse+="?";
                }
                if (language.equals("french")){
                    codebotResponse = Punctuation.compress(codebotResponse);
                    System.out.println(codebotResponse);
                    codebotResponse = translate.translate(codebotResponse, Language.ENGLISH, Language.FRENCH);
                    if (confused==true)
                    	codebotResponse+="?";
               }
               
                return codebotResponse;
        }
        
        /*
         * This method takes the user question and performs a google search in their default browser
         * This is the worst case scenario of codebot not knowing what to do :(
         */
        private String googlePrompt(String response) {
            String codebotResponse;
            lastSaidType = "googleprompt";
            details = null;
        	writeSearch(response,lastSaid);
                searchTerms = lastSaidNoSpellCheck;
                String[] options = {"Sorry, I don't understand. ","I am not that smart... yet. ","I am confused. ", "I'm not familiar with this. ", "I have no knowledge on this matter. "};
                Random rand = new Random();
                int choice = rand.nextInt(5);
                codebotResponse = options[choice]+ "Want me to search that for you?";
                return codebotResponse;
                
        }
        
        private String twitterPrompt(String response) {
            String codebotResponse;
            lastSaidType = "twitterprompt";
            details = null;
        	writeSearch(response,lastSaid);
                searchTerms = lastSaidNoSpellCheck;
                String[] options = {"Sorry, I don't understand. ","I am not that smart... yet. ","I am confused... ", "I'm not familiar with this. ", "I have no knowledge on this matter. "};
                Random rand = new Random();
                int choice = rand.nextInt(5);
                codebotResponse = options[choice]+ "Some tweets about this might be of help to you, wanna see some? ";
                return codebotResponse;
                
        }
        
        
        
        /*
        this method performs the google search
        */
        /** This method takes a query as a string and performs a google search with the desktop browser
         * 
         * @param query
         * 
         *
         */
        public void performGoogle(String query){
            String q = query.replace(' ', '+'); //correctly formats the search terms
             try {
                                Desktop desktop = java.awt.Desktop.getDesktop();
                                URL oURL = new URL("https://www.google.com/#q="+q);
                                desktop.browse(oURL.toURI()); //opens the url on the default browser
                                }
                        catch (Exception e) {
                                e.printStackTrace();
                                }
        }
        
        private String getTweets(String q){
        	String str = "No tweets found.";
        	String tweets = tweeter.getTweets(q);
        	if(tweets!=null){
        		str = tweets;
        	}
        	return str;
        }

        
        //this method will store the google search in a file so we will be able to see what people are asking us to search
        //that way we will know what topics to add in
        private void writeSearch(String q, String lastSaid2) {
        	String storethis = q+",~"+lastSaid+",~\n"; //their response is the key, and the value is what we said last
        	try {
     
    			File file = new File("SearchStorage.txt");
     
    			// if file doesnt exists, then create it
    			if (!file.exists()) {
    				file.createNewFile();
    			}
    			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
    			BufferedWriter bw = new BufferedWriter(fw);
    			//bw.write(storethis);
    			bw.append(storethis);
    			bw.close();
     
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
		}

		/*
         * This method provides further instruction based on the input topic
         */
        private String instruct(String topic) {
                lastSaidType = "detail";
                boolean result = false;
                Iterator<String> keySet = details.keySet().iterator();        // returns an iterable list of topics from the hashmap
                String currentKey = null;
                Scanner topicscan;
                while(keySet.hasNext() && !result){                //This will return each individual key to search through since some are comprise of multiple keywords
                        currentKey = keySet.next();
                        topicscan = new Scanner(currentKey);
                        topicscan.useDelimiter(", *");
                        while(topicscan.hasNext()){                //Once codebot has the whole key with all keywords for the topic, it looks for matches from what the user inputed
                                String currentString = topicscan.next().toLowerCase();
                                currentString = Punctuation.space(currentString);
                                if(topic.toLowerCase().contains(currentString)){
                                        result = true;                //if coedbot finds a match, it now knows the topic they were searching for and can use the key to find the instructions
                                        break;
                                }
                        }
                }
                String value = details.get(currentKey);        //this will look up the instructions to return to them to the user
                lastSaid = currentKey;
                lastSaidType = "tutor";
                return value + topicprompt();
        }

        /*
         * This method acknowledges compliments
         */
        private String acknowledge() {
                Random rand = new Random();
                String acknowledgement = acknowledgements.get(rand.nextInt(acknowledgements.size()));
                lastSaid = acknowledgement;
                lastSaidType = "acknowledgement";
               return acknowledgement.substring(1);
                
                
        }

        /*
         * Provides the user help when given a topic
         */
        private String tutor(String topic) {
                boolean result = false;
                Iterator<String> keySet = topics.keySet().iterator();        // returns an iterable list of topics from the hashmap
                String currentKey = null;
                Scanner topicscan;
                while(keySet.hasNext() && !result){                //This will return each individual key to search through since some are comprise of multiple keywords
                        currentKey = keySet.next();
                        topicscan = new Scanner(currentKey);
                        topicscan.useDelimiter(", *");
                        while(topicscan.hasNext()){                //Once codebot has the whole key with all keywords for the topic, it looks for matches from what the user inputed
                                String currentString = topicscan.next().toLowerCase();
                                currentString = Punctuation.space(currentString);
                                if(topic.toLowerCase().contains(currentString)){
                                        result = true;                //if coedbot finds a match, it now knows the topic they were searching for and can use the key to find the instructions
                                        break;
                                }
                        }
                }
                String value = topics.get(currentKey);        //this will look up the instructions to return to them to the user
                lastSaid = currentKey;
                Scanner first = new Scanner(currentKey);
                String firstword = first.next();
                details = Populate.details(firstword);
                lastUserTutor = firstword;
                lastSaidType = "tutor";
               
                return value+" "+topicprompt();
        }
        
        /*
         * This method picks a random reprompt and prints it
         */
        private String topicprompt() {
                Random rand = new Random();
                String topicprompt = topicprompts.get(rand.nextInt(topicprompts.size()));
                return topicprompt;
        }
        

        /*
         * This method asks if the user has any other questions
         */
        private String reprompt() {
                Random rand = new Random();
                String reprompt = reprompts.get(rand.nextInt(reprompts.size()));
                lastSaidType="reprompt";
               return reprompt.substring(1);        
               
        }


        /*
         * This method stops the ball from rolling
         */
        private String endSession() {
                Random rand = new Random();
                String closure = closures.get(rand.nextInt(closures.size()));
                return closure.substring(1);        
        }

        /*
         * This method prompts the user to ask a question.
         */
        private String prompt(){
                Random rand = new Random();
                String prompt = prompts.get(rand.nextInt(prompts.size()));
                lastSaid = prompt;
                lastSaidType = "prompt";
               return prompt.substring(1);
        }
        
        /*
         * This method asks the user what he/she needs help with
         */
        private String inquire() {
                Random rand = new Random();
                String inquiry = inquiries.get(rand.nextInt(inquiries.size()));
                lastSaid = inquiry;
                lastSaidType = "inquiry";
                return inquiry.substring(1);
                
        }
        
        public void setLanguage(String str){
        	language = str;
        }
        
        public String getLanguage(){
        	return language;
        }

}

