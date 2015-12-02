package com.chessgear.analysis;

/**
 * Modified version of code found on http://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
 * Used to determine the OS of the system and find location of the Stockfile binary file that corresponds to the OS system
 */
public class OsUtils {

    private boolean isWindows; //Determines whether the system is a Windows computer
    private String binaryLocation; //The location of the binary file of the system type

    /**
     * Default constructor
     * Finds the OS of the system and determine its corresponding binaryLocation
     */
    public OsUtils(){
        determineBinaryLocation();
    }

    /***
     * Determines the binary location of the file based on whether the system isWindows or not.
     */
    private void determineBinaryLocation(){
        if(checkIsWindows()){
            this.binaryLocation= "./stockfish-6-src/src/./stockfish2";
        } else{
            this.binaryLocation= "./stockfish-6-src/src/./stockfish";
        }
    }

    /**
     * Accessor for binaryLocation
     * @return the location of the binary file that corresponds to the System's OS
     */
    public String getBinaryLocation(){
        return this.binaryLocation;
    }

    /**
     * Accessor for isWindows
     * @return True if the system is a Windows machine or else False
     */
    public boolean checkIsWindows(){
        if(binaryLocation==null){
            String OS = System.getProperty("os.name");
            isWindows = OS.startsWith("Windows");
        }
        return this.isWindows;
    }
}
