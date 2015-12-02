/**
 * Created by GradyXiao on 12/2/15.
 */
import static org.junit.Assert.*;
import com.chessgear.analysis.OsUtils;
import org.junit.Before;
import org.junit.Test;

public class OsUtilsTest {
    private final String unixBinaryLocation = "./stockfish-6-src/src/./stockfish"; //Location for mac or unix binary file
    private final String windowsBinaryLocation = "./stockfish-6-src/src/./stockfish.exe"; //Location for windows binary file

    @Test
    public void testOsUtils(){
        OsUtils osUtils = new OsUtils();
        if(osUtils.checkIsWindows()){
            assertEquals(osUtils.getBinaryLocation(),windowsBinaryLocation);
        } else {
            assertEquals(osUtils.getBinaryLocation(),unixBinaryLocation);
        }
    }

}
