package com.chessgear.analysis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * Class to facilitate engine analysis using Stockfish and Java Runtime
 */
public final class Engine {
    private Process process; //Java process

    private BufferedReader stdInput; //Input from the Stock engine
    private BufferedWriter stdOutput; //Output to the Stockfish engine

    //Logger for Engine
    private static final Logger logger = LoggerFactory.getLogger(Engine.class);

    /***
     * Default constructor. Starts the Stockfish engine.
     * @param binaryLocation the location of the Stockfish binary file
     */
    public Engine(String binaryLocation){
        startEngine(binaryLocation);
    }

    /****
     * Start the Engine process and get it ready for Engine analysis
     * @param binaryLocation the location of the Stockfish binary file
     */
    private void startEngine(String binaryLocation) {
        try {
            Runtime rt = Runtime.getRuntime();
            process = rt.exec(binaryLocation);

            stdOutput = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
            logger.error("IOException during attempt to startEngine");
        }
    }

    /***
     * Analyses the FEN string with moveTime in ms as input. Sets the cp, pv, and bestMove values.
     * @param fen the FEN string passed in
     * @param moveTime the time for the engine to analyse for in ms
     * @return the EngineResult object containing the result of the analysis
     * @throws IOException throws an error if something wrong happens during analyseFEN()
     */
    public EngineResult analyseFEN(String fen,int moveTime) throws IOException{

        //logger.info("Analyzing fen "+fen);

        EngineResult engineResult = new EngineResult(); //Results from the engine analysis

        String command;
        command="position fen "+fen+"\n";
        stdOutput.write(command);
        stdOutput.flush();

        command="go movetime "+moveTime+"\n";
        stdOutput.write(command);
        stdOutput.flush();

        boolean print = false; //Set to true to print
        obtainResultAndMaybePrint(engineResult, print);

        return engineResult;

    }

    /***
     * Obtains the result from the Stockfish analysis output and store
     * them into EngineResult
     * Maybe log all outputs of the engine depending on whether print is true or false
     * @param engineResult Stores the engineResult of the Stockfish analysis
     * @param print Prints the output from Stockfish analysis if True
     * @throws IOException if something goes wrong with the scanner
     */
    private void obtainResultAndMaybePrint(EngineResult engineResult, boolean print) throws IOException {
        // read the output from the command
        String s="";
        Scanner scanner=new Scanner(s);
        MatchResult matchResult;
        while ((s = stdInput.readLine()) != null) {
            if(print) {
                logger.info(s);
            }

            if(s.contains(" cp ")){
                scanner = new Scanner(s);
                scanner.findInLine("cp ([-]*\\d+)");
                matchResult = scanner.match();
                engineResult.setCp(Integer.parseInt(matchResult.group(1)));
            }

            if(s.contains(" pv ")){
                engineResult.setPv(s.substring(s.indexOf(" pv ")+4));
            }

            if(s.contains("bestmove ")) {
                scanner = new Scanner(s);
                engineResult.setBestMove(s.substring(s.lastIndexOf("bestmove")+9));
                break;
            }
        }

        if(print) {
            logger.info("Last cp: " + engineResult.getCp());
            logger.info("Last pv: " + engineResult.getPv());
            logger.info("Best move: " + engineResult.getBestMove());
            logger.info("");
        }
        scanner.close();
    }

    /***
     * Terminate the Engine process and closes the stream buffers
     * @throws IOException if close is unsuccessful.
     */
    public void terminateEngine() throws IOException {
        stdInput.close();
        stdOutput.close();
        process.destroy();
    }
}
