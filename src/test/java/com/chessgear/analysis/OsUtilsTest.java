package com.chessgear.analysis; /**
 * Created by GradyXiao on 12/2/15.
 */
import static org.junit.Assert.*;
import com.chessgear.analysis.OsUtils;
import org.junit.Test;

public class OsUtilsTest {
    private final String unix32BinaryLocation = "./stockfish-6-src/src/./stockfish-32"; //Location for 32 bit mac or unix binary file
    private final String unix64BinaryLocation = "./stockfish-6-src/src/./stockfish"; //Location for 64 bit mac or unix binary file
    private final String windows32BinaryLocation = "./stockfish-6-src/src/./stockfish-6-32.exe"; //Location for 32 bit windows binary file
    private final String windows64BinaryLocation = "./stockfish-6-src/src/./stockfish-6-64.exe"; //Location for 64 bit windows binary file

    @Test
    public void testOsUtils(){
        OsUtils osUtils = new OsUtils();
        if(osUtils.checkIsWindows()){
            if(osUtils.checkIs64()){ //64-bit Windows
                assertEquals(osUtils.getBinaryLocation(),windows64BinaryLocation);
            }
            else { //32-bit Windows
                assertEquals(osUtils.getBinaryLocation(),windows32BinaryLocation);
            }
        } else {
            if(osUtils.checkIs64()){ //64-bit Mac or Unix
                assertEquals(osUtils.getBinaryLocation(),unix64BinaryLocation);
            }
            else{ //32-bit Mac or Unix
                assertEquals(osUtils.getBinaryLocation(),unix32BinaryLocation);
            }
        }
    }

}
