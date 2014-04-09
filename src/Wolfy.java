import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAImage;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class Wolfy {

	private static String myurl = "http://api.wolframalpha.com/v2/query?input=";
	private String charset = "UTF-8";
	//private String appid = "KH2TXA-P884RH5W7G";
	private WAEngine en;
	private WAQuery qu;
	private String in;

	public Wolfy() {
		en = makeEngine();
	}

	private WAEngine makeEngine() {
		WAEngine en = new WAEngine();
		en.setAppID("KH2TXA-P884RH5W7G");
		en.addFormat("plaintext");
		en.addFormat("image");
		en.createQuery();
		return en;
	}

	private WAQuery makeQuery(String str) {
		qu = en.createQuery(str);
		return qu;
	}
	
	private boolean sortaMatch(String s1, String s2){
		String[] ss1 = s1.split(" ");
		String[] ss2 = s2.split(" ");
		for (int i = 0; i < ss1.length; i++){
			for (int k = 0; k< ss2.length; k++){
				if (ss1[i].contains(ss2[k])){
					return true;
				}
			}
		}
		return false;
	}
	
	private String printQ(WAQuery q, String in) {
		String toReturn = "";
		try {
			WAQueryResult res = en.performQuery(qu);
			if (res.isError()) {
				
	                return "Sorry, an error has occured. The world's smartest monkey's are trying to resolve the problem as we speak.";

			} else if (!res.isSuccess()) {
				return "You have confused Wolfram Alpha as well.. Congratulations";
			} else {
				for (WAPod p : res.getPods()) {
					//if (p.getTitle().equals("Description") ||p.getTitle().equals("Results") || Comparison.contains(in.split(" "), p.getTitle())|| sortaMatch(p.getTitle(), in)) {
						
						System.out.println(p.getTitle());
						for (WASubpod sp : p.getSubpods()) {
							for (Object el : sp.getContents()) {								
								if (el instanceof WAPlainText) {
									System.out.println(((WAPlainText) el)
											.getText());
									toReturn+=p.getTitle();
									toReturn +=" "+((WAPlainText) el).getText()+"\n";
								}
							}
						//}
					}
				}
			}
		} catch (WAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!toReturn.isEmpty())
			return "Here is what Wolfram has to say on the matter: \n\n"+toReturn;
		else if (!in.toLowerCase().contains("definition")){
			String newin = "definition "+in;
			WAQuery tryagain = makeQuery(newin);
			return printQ(tryagain, newin);
		}
		else
			return "I couldn't find anything that I think will help you.";
	}
	
	public String searchWolfram(String in){
		WAQuery wq = makeQuery(in);
		return printQ(wq,in);
	}
}

/*
 * private String makeQuery(String str) { try { String query =
 * URLEncoder.encode(str, charset); String qurl = url + query + "&appid=" +
 * appid; return qurl; } catch (UnsupportedEncodingException e) {
 * e.printStackTrace(); return "COULDNT MAKE QUERY"; } }
 * 
 * private void wolfCon(String url){ URLConnection con = new
 * URL(url).openConnection(); con.setRequestProperty("Accept-Charset", charset);
 * InputStream reply = con.getInputStream();
 * 
 * }
 * 
 * public getSyn(String str){ String q = makeQuery(str);
 * 
 * }
 */

