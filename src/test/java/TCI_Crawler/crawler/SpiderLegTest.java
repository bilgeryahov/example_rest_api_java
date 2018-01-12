package TCI_Crawler.crawler;

import TCI_Crawler.exceptions.InvalidCategoryException;
import TCI_Crawler.exceptions.InvalidSiteException;
import TCI_Crawler.handlers.LinksHandler;
import TCI_Crawler.handlers.SearchObjectHandler;
import TCI_Crawler.searchObjects.Movie;
import TCI_Crawler.searchObjects.SearchObjectWithLinks;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SpiderLegTest {

    /**
     * verify - does the stubbing - verifies that the method is called x times.
     * Verify is usually used for functions which run under conditions, to make sure that it
     * actually is executed.
     * @throws InvalidSiteException
     * @throws InvalidCategoryException
     */
    @Test
    public void testGetObjectAndLinksVerifyHandlersCalled() throws InvalidSiteException, InvalidCategoryException {
        Document htmlDoc = Jsoup.parse("<html></html>");
        LinksHandler linksHandler = mock(LinksHandler.class);
        SearchObjectHandler searchObjectHandler = mock(SearchObjectHandler.class);
        SpiderLeg leg = new SpiderLeg(linksHandler, searchObjectHandler);
        leg.getObjectAndLinks(htmlDoc);
        verify(linksHandler, times(1)).getValidLinks(htmlDoc);
        verify(searchObjectHandler, times(1)).getSearchObjects(htmlDoc);
    }

    @Test
    public void testGetObjectAndLinksVerifyCorrectObjectAndLinks() throws InvalidSiteException, InvalidCategoryException {
        String htmlString = "<html><body>" +
                "<a href=\"https://test.com/link1.php\"></a>" +
                "<a href=\"https://test.com/link2.php\"></a>" +
                "<div class=\"media-details\">" +
                "<h1>Forrest Gump</h1>" +
                "<table>" +
                "<tr><th>Category</th><td>Movies</td></tr>" +
                "<tr><th>Genre</th><td>Drama</td></tr>" +
                "<tr><th>Format</th><td>DVD</td></tr>" +
                "<tr><th>Year</th><td>1994</td></tr>" +
                "<tr><th>Director</th><td>Robert Zemeckis</td></tr>" +
                "<tr><th>Writers</th><td>Winston Groom, Eric Roth</td></tr>" +
                "<tr><th>Stars</th><td>Tom Hanks, Rebecca Williams, Sally Field</td></tr>" +
                "</table></div></body></html>";
        Document htmlDocument = Jsoup.parse(htmlString);
        SpiderLeg leg = new SpiderLeg(new LinksHandler(), new SearchObjectHandler());
        String[] expectedLinks = {"https://test.com/link1.php", "https://test.com/link2.php"};
        Movie expectedObject = new Movie(
                "Forrest Gump",
                "Drama",
                1995,
                "DVD",
                "Robert Zemeckis",
                new String[]{"Winston Groom", "Eric Roth"},
                new String[]{"Tom Hanks", "Rebecca Williams", "Sally Field"});

        SearchObjectWithLinks objectWithLinks = leg.getObjectAndLinks(htmlDocument);

        // Assert that the retrieved links are equal to the expected links.
        assertArrayEquals(expectedLinks, objectWithLinks.getRetrievedLinks().toArray());
        // Assert that both objects are of the same class.
        assertEquals(expectedObject.getClass(), objectWithLinks.getRetrievedObject().getClass());
        // Assert that both objects have the exact same properties.
        assertThat(expectedObject, samePropertyValuesAs(objectWithLinks.getRetrievedObject()));
    }
}