import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The class used to create files with articles from Wikipedia
 *
 */
public class WikiReader {

	// scary array of names of 100 people
	static String[] names = {"Virginia_Clinton_Kelley", "Michael_Jackson", "Bill_Clinton", "Elon_Musk", "Albert_Einstein", 
							 "Nikola_Tesla", "Thomas_Edison", "Marie_Curie", "Pierre_Curie", "Henri_Becquerel", "Isaac_Newton", 
							 "Enrico_Fermi", "Johannes_Kepler", "Voltaire", "Johann_Sebastian_Bach", "Yuri_Gagarin", "Julius_Caesar", 
							 "Sigmund_Freud", "Joseph_Stalin", "Thomas_Jefferson", "Konstantin_Tsiolkovsky", "Francisco_Pizarro", 
							 "Nicolaus_Otto", "Joseph_Lister", "Max_Planck", "Gregor_Mendel", "John_Calvin", "Ernest_Rutherford", 
							 "William_Harvey", "Sergei_Korolev", "Nikolay_Sklifosovsky", "Louis_Daguerre", "Werner_Heisenberg", 
							 "Ludwig_van_Beethoven", "John_Locke", "Alexander_Fleming", "Alexander_Graham_Bell", "Adolf_Hitler", 
							 "John_Dalton", "Adam_Smith", "Karl_Marx", "James_Clerk_Maxwell", "Michael_Faraday", "James_Watt", 
							 "Antoine_Lavoisier", "Nicolaus_Copernicus", "Charles_Darwin", "Aristotle", "Galileo_Galilei", 
							 "Louis_Pasteur", "Christopher_Columbus", "Johannes_Gutenberg", "Elizabeth_I", "Henry_Ford", 
							 "Francis_Bacon", "Vasco_da_Gama", "Vladimir_Lenin", "John_F._Kennedy", "Thomas_Robert_Malthus", 
							 "Louis_Armstrong", "Jean-Jacques_Rousseau", "Leonhard_Euler", "Robert_Bosch", "Isaac_Singer", 
							 "Coco_Chanel", "Daniel_Swarovski", "Christian_Dior", "Pablo_Picasso", "Niels_Bohr", "Archimedes", 
							 "Charles_Babbage", "Benjamin_Franklin", "Mahatma_Gandhi", "Leonardo_da_Vinci", "Ferdinand_Magellan", 
							 "Abraham_Lincoln", "James_Gosling", "Dennis_Ritchie", "Tim_Berners-Lee", "Bill_Gates", "Jeff_Bezos", 
							 "Peter_the_Great", "Sandro_Botticelli", "Dmitri_Mendeleev", "Alan_Turing", "Mark_Twain", 
							 "Alexander_Pushkin", "Leo_Tolstoy", "Marilyn_Monroe", "Elvis_Presley", "Elton_John", "Adele", 
							 "John_Lennon", "Wolfgang_Amadeus_Mozart", "Johann_Sebastian_Bach", "Isidor_Straus", 
							 "James_Prescott_Joule", "Blaise_Pascal", "Marlene_Dietrich", "William_Shakespeare"};

	/**
	 * @param rd
	 * 		   Reader type object
	 * @return string value
	 * @throws IOException
	 */
  private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  /**
   * Reading json from url
   * @param url
   * 			url of an wiki-article
   * @return JSONObject
   * @throws IOException
   * @throws JSONException
   */
  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }
  
  /**
   * This method creates file
   * 
   * @param jsonStr
   * 			a string with formatted text to put in a file
   * @param name
   * 			a string representing the name of the file
   */
  public static void file(String jsonStr, String name) {
	String nameOfFile = name;
	String str = jsonStr;
	try {
		FileWriter writer = new FileWriter(nameOfFile);
		writer.write(str);
		writer.close();
	}
	catch(IOException e) {
		e.printStackTrace();
	}
  }
  
  /**
   * Main method. There information is retrieved, formatted and sent to a method for writing to a file
   * @param args
   * 			command-line arguments
   * @throws IOException
   * @throws JSONException
   */
  public static void main(String[] args) throws IOException, JSONException {
    
    for(int i = 0; i < names.length; i++) {
    	
    	JSONObject json = readJsonFromUrl("https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&explaintext&redirects=1&titles=" + names[i]);
    	
    	String txtPageNumber = json.toString();
    	
    	txtPageNumber = txtPageNumber.substring(txtPageNumber.indexOf("pageid")+8,txtPageNumber.indexOf("\"title\"")-1);
    	
    	String strToFormat = json.getJSONObject("query").getJSONObject("pages").getJSONObject(txtPageNumber).getString("extract");
        
        strToFormat = names[i] + "\n\n" + strToFormat;
        file(strToFormat, names[i]+".txt");
        
        //just check
        //System.out.println(i + " done");
    }
	
    }
}
