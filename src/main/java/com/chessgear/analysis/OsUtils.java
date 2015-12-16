package com.chessgear.analysis;

/**
 * Modified version of code found on http://stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
 * Used to determine the OS of the system and find location of the Stockfile binary file that corresponds to the OS system
 */
public final class OsUtils {

    private boolean isWindows; //Determines whether the system is a Windows computer
    private boolean is64; //Determines whether the system is a 32-bit or 64-bit architecture
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
        if(checkIsWindows()){ //Windows
            if(checkIs64()){
                this.binaryLocation= "./stockfish-6-src/src/./stockfish-6-64.exe";
            } else{
                this.binaryLocation= "./stockfish-6-src/src/./stockfish-6-32.exe";
            }
        } else{ //Mac or Unix
            if(checkIs64()){
                this.binaryLocation= "./stockfish-6-src/src/./stockfish";
            } else{
                this.binaryLocation= "./stockfish-6-src/src/./stockfish-32";
            }
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
     * Mutator and Accessor for isWindows
     * @return True if the system is a Windows machine or else False
     */
    public boolean checkIsWindows(){
        if(binaryLocation==null){
            String os = System.getProperty("os.name");
            this.isWindows = os.startsWith("Windows");
        }
        return this.isWindows;
    }

    /**
     * Mutator and Accessor for is64
     * @return True if the system is a 64-bit architecture or else False
     */
    public boolean checkIs64(){
        if(binaryLocation==null){
            String arch = System.getProperty("os.arch");
            this.is64 = arch.endsWith("64");
        }
        return this.is64;
    }
}
