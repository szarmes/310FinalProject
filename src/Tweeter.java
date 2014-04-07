
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * The purpose of this class is to get tweets related to a specific search query
 */
public final class Tweeter {
	/**
	 * Usage: java twitter4j.examples.trends.GetAvailableTrends
	 * 
	 * @param args
	 *            message
	 *            
	 *         
	 */
	Twitter twitter;
	public Tweeter(){
		//this sets up the configuration
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("BLJp4OK7XIH1uYIex6tv815LW");
		cb.setOAuthConsumerSecret("yjfiUy2cX2IYE6cUFNOov7poJtU5iSA1YCegRi8t26EFcA3W4e");
		cb.setOAuthAccessToken("2213358944-GdbP1sdYxbKrO9yEtdg5HQiw9PmVWHNsQxjpLzv");
		cb.setOAuthAccessTokenSecret("QROKJsgqO0b9x1BPDUKODrdYXdkoYxDWRhO1d1oXWtEYr");

		twitter = new TwitterFactory(cb.build()).getInstance();
	}
	
	//this method pulls the tweets and adds a small amount to a string, and double spaces them so they're easier to read
	public String getTweets(String q) {
		String str = "Here ya go! \n\n";
		try {

			
			Query query = new Query(q);
			QueryResult result;
			int count = 0;
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				
				for (Status tweet : tweets) {
					if (count>10)
						break;
					str+=("@" + tweet.getUser().getScreenName()
							+ " - " + tweet.getText())+"\n\n";
					count++;
				}
				
			} 
			while ((query = result.nextQuery()) != null&&count<10);
			//System.exit(0);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			System.exit(-1);
		}
		return str;
	}
	
}
