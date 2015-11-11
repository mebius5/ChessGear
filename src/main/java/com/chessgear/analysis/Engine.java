package com.chessgear.analysis;

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

    EngineResult engineResult; //Results from the engine analysis

    /***
     * Default constructor. Starts the Stockfish engine
     */
    public Engine(){
        try {
            rt = Runtime.getRuntime();
            proc = rt.exec(new String[]{"./stockfish-6-src/src/./stockfish"});

            stdOutput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            engineResult = new EngineResult();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***
     * Analyses the FEN string with moveTime in ms as input. Sets the cp, pv, and bestMove values.
     * @param fen the FEN string passed in
     * @param moveTime the time for the engine to analyse for in ms
     * @return the EngineResult object containing the result of the analysis
     */
    public EngineResult analyseFEN(String fen,int moveTime){
        try {
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
                    scanner.findInLine("cp (\\d+)");
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
            stdInput.close();
            stdOutput.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return this.engineResult;
    }
}
