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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SpiderLegTest {


    /**
     *
     * Using `verify` means using indirect testing.
     * In our case we test indirectly if a function gets called. Mocking is used in our case.
     *
     * @throws InvalidSiteException
     * @throws InvalidCategoryException
     */
    @Test
    public void testGetObjectAndLinksVerifyHandlersCalled() throws InvalidSiteException, InvalidCategoryException {

        // Create the HTML document from string....
        Document htmlDoc = Jsoup.parse("<html></html>");

        // Create the mocks...
        LinksHandler linksHandler = mock(LinksHandler.class);
        SearchObjectHandler searchObjectHandler = mock(SearchObjectHandler.class);

        // Use the mocks to create the actual SpiderLeg object.
        SpiderLeg leg = new SpiderLeg(linksHandler, searchObjectHandler);
        leg.getObjectAndLinks(htmlDoc);

        // Verify that the `getValidLinks` function is called one time with parameter the `htmldoc`
        verify(linksHandler, times(1)).getValidLinks(htmlDoc);

        // Verify that the `getSearchObjects` function is called one time with parameter the `htmldoc`
        verify(searchObjectHandler, times(1)).getSearchObjects(htmlDoc);
    }

    /**
     * Example of a test using Matchers.
     * Matchers are the `assertThat` statements.
     * Matchers are actually the parameters inside the method calls.
     * In order to be able to use them, hamcrest is needed as a dependency.
     * @throws InvalidSiteException
     * @throws InvalidCategoryException
     */

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

        // Transform the string into a Document class object.
        Document htmlDocument = Jsoup.parse(htmlString);
        SpiderLeg leg = new SpiderLeg(new LinksHandler(), new SearchObjectHandler());

        // Expected links from the string above...
        String[] expectedLinks = {"https://test.com/link1.php", "https://test.com/link2.php"};

        // Expected object from the string above.
        Movie expectedObject = new Movie(
                "Forrest Gump",
                "Drama",
                1994,
                "DVD",
                "Robert Zemeckis",
                new String[]{"Winston Groom", "Eric Roth"},
                new String[]{"Tom Hanks", "Rebecca Williams", "Sally Field"});

        SearchObjectWithLinks objectWithLinks = leg.getObjectAndLinks(htmlDocument);

        // Assert that the retrieved links are equal to the expected links.
        assertThat(expectedLinks, is(equalTo(objectWithLinks.getRetrievedLinks().toArray())));
        // Assert that both objects are of the same class.
        assertThat(expectedObject.getClass(), is(equalTo(objectWithLinks.getRetrievedObject().getClass())));
        // Assert that both objects have the exact same properties.
        assertThat(expectedObject, samePropertyValuesAs(objectWithLinks.getRetrievedObject()));
    }
}