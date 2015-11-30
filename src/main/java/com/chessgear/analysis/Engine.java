package com.chessgear.analysis;

import com.chessgear.data.PGNParseException;

import java.io.*;
import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * Class to facilitate engine analysis.
 * Created by Ran on 10/24/2015.
 */
public class Engine {
    Runtime rt; //Java runtime
    Process proc; //Java process

    BufferedReader stdInput; //Input from the Stock engine
    BufferedWriter stdOutput; //Output to the Stockfish engine

    /***
     * Default constructor. Starts the Stockfish engine
     */
    public Engine(String binaryLocation){
        startEngine(binaryLocation);
    }

    /****
     * Start the Engine process and get it ready for Engine analysis
     */
    private void startEngine(String binaryLocation) {
        try {
            rt = Runtime.getRuntime();
            proc = rt.exec(binaryLocation);

            stdOutput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /***
     * Analyses the FEN string with moveTime in ms as input. Sets the cp, pv, and bestMove values.
     * @param fen the FEN string passed in
     * @param moveTime the time for the engine to analyse for in ms
     * @return the EngineResult object containing the result of the analysis
     */
    public EngineResult analyseFEN(String fen,int moveTime) throws Exception{
        try {
            EngineResult engineResult = new EngineResult(); //Results from the engine analysis
            boolean print = false; //Set to true to print
            String command;
            command="position fen "+fen+"\n";
            stdOutput.write(command);
            stdOutput.flush();

            command="go movetime "+moveTime+"\n";
            stdOutput.write(command);
            stdOutput.flush();

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            String s="";
            Scanner scanner=new Scanner(s);
            MatchResult matchResult;
            while ((s = stdInput.readLine()) != null) {
                if(print) {
                    System.out.println(s);
                }

                if(s.contains("cp")){
                    scanner = new Scanner(s);
                    scanner.findInLine("cp ([-]*\\d+)");
                    matchResult = scanner.match();
                    engineResult.setCp(Integer.parseInt(matchResult.group(1)));
                }

                if(s.contains("pv")){
                    engineResult.setPv(s.substring(s.indexOf(" pv ")+4));
                }

                if(s.contains("bestmove")) {
                    scanner = new Scanner(s);
                    scanner.findInLine("bestmove (\\w+)");
                    matchResult = scanner.match();
                    engineResult.setBestMove(matchResult.group(1));
                    break;
                }
            }

            if(print) {
                System.out.println("Last cp: " + engineResult.getCp());
                System.out.println("Last pv: " + engineResult.getPv());
                System.out.println("Best move was " + engineResult.getBestMove());
            }
            scanner.close();
            return engineResult;
        } catch (IOException e){
            //e.printStackTrace();
            throw e;
        }
    }

    /***
     * Terminate the Engine process and closes the stream buffers
     * @throws Exception if close is unsuccessful.
     */
    public void terminateEngine() throws Exception{
        try {
            stdInput.close();
            stdOutput.close();
            proc.destroy();
        }catch (Exception e){
            throw e;
        }
    }
}
