import org.junit.Test;

import java.util.List;

import static com.chessgear.data.PGNParser.parseTags;
import static com.chessgear.data.PGNParser.Tag;

/**
 * Created by Ran on 10/18/2015.
 */
public class PGNParseTest {

    @Test
    public void getTagsTest() {

        String testString = "[Event \"Hourly Bullet Arena\"]";
        List<Tag> tags = parseTags(testString);
        Tag tag = tags.get(0);

        assert(tag.)


    }

}
