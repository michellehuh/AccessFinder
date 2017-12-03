package spider.copy;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg
{
    // We'll use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;


    /**
     * This performs all the work. It makes an HTTP request, checks the response, and then gathers
     * up all the links on the page. Perform a searchForWord after the successful crawl
     *
     * @param url
     *            - The URL to visit
     * @return whether or not the crawl was successful
     */
    public boolean crawl(String url)
    {
        try
        {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if(connection.response().statusCode() == 200) // 200 is the HTTP OK status code
            // indicating that everything is great.
            {
              //  System.out.println("\n**Visiting** Received web page at " + url);
            }
            if(!connection.response().contentType().contains("text/html"))
            {
                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            //System.out.println("Found (" + linksOnPage.size() + ") links");
            for(Element link : linksOnPage)
            {
                this.links.add(link.absUrl("href"));
            }
            return true;
        }
        catch(IOException ioe)
        {
            // We were not successful in our HTTP request
            return false;
        }
    }


    /**
     * Performs a search on the body of on the HTML document that is retrieved. This method should
     * only be called after a successful crawl.
     *
     * @param searchWord
     *            - The word or string to look for
     * @return whether or not the word was found
     */
    public String read()
    {
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return null;
        }
        String bodyText = this.htmlDocument.body().text();


        //Elements showAccessElement = this.htmlDocument.getElementByClassName("showAccessTable");
        Element element = this.htmlDocument.getElementById("showAccessTable");
        Element h1 = this.htmlDocument.getElementsByTag("h1").get(1);
        
        if (element == null || h1.toString().contains("<h1> <span") ) 
        	return null;
        
        //if (showAccessElement.size() == 0)
            //return null;

        //return showAccessElement.get(0).toString();
        String h1String = h1.toString().substring(4, h1.toString().indexOf("<span"));
        Data.ubcMap.put(h1String, element.toString().replaceAll("src=\"images", "src=\"http://maps.ubc.ca/PROD/images"));
        System.out.println(h1String);
        return "";
    }
    
    public boolean possible(String searchWord)
    {
        // Defensive coding. This method should only be used after a successful crawl.
        if(this.htmlDocument == null)
        {
            System.out.println("ERROR! Call crawl() before performing analysis on the document");
            return false;
        }
        String bodyText = this.htmlDocument.body().text();

        if (!bodyText.toLowerCase().contains(searchWord.toLowerCase()))
            return false;

        return true;
    }


    public List<String> getLinks()
    {
        return this.links;
    }

}