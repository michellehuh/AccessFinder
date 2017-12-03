package spider.copy;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {

    private static final int MAX_PAGES_TO_SEARCH = 10000;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();


    /**
     * Our main launching point for the Spider's functionality. Internally it creates spider legs
     * that make an HTTP request and parse the response (the web page).
     *
     * @param url        - The starting point of the spider
     * @param searchWord - The word or string that you are searching for
     */
    public String search(String url) {
    	String result = null;
    	
        while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
            String currentUrl;
            SpiderLeg leg = new SpiderLeg();

            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);

            } else {
                currentUrl = this.nextUrl();
            }

//            if (currentUrl.contains("=#showAccess") && !currentUrl.contains("show=n,n,y,n,n,y")) {
//            	continue;
//            }
            
            leg.crawl(currentUrl); // Lots of stuff happening here. Look at the crawl method in
            
            if (currentUrl.contains("=#showAccess")) {
            	System.out.println("\n**Visiting** Received web page at " + currentUrl);
                leg.read();
            } 

            List<String> links = leg.getLinks();
            for (String link : links) {
                if (link.contains("search_results.php?") 
                		|| link.contains("index_detail.php?") 
                		|| (link.contains("=#showAccess") && link.contains("show=n,n,y,n,n,y"))) {
                    this.pagesToVisit.add(link);
                }
            }
        }

        System.out.println("\n**Done** Visited " + this.pagesVisited.size() + " web page(s)");
        return result;
    }


    /**
     * Returns the next URL to visit (in the order that they were found). We also do a check to make
     * sure this method doesn't return a URL that has already been visited.
     *
     * @return
     */
    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
}

