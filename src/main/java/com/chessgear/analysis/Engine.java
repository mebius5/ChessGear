package com.chessgear.analysis;

import java.io.*;
import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * Class to facilitate engine analysis.
 * Created by Ran on 10/24/2015.
 */
public class Engine {
    Runtime rt;
    Process proc;

    BufferedReader stdInput;
    BufferedWriter stdOutput;

    int cp; //Stores the cp score returned by the Stockfish engine
    String pv; //Stores the pv moves returned by the Stockfish engine
    String bestMove; //Stores the best move returned by the Stockfish engine

    /***
     * Default constructor. Starts the Stockfish engine
     */
    public Engine(){
        try {
            rt = Runtime.getRuntime();
            proc = rt.exec(new String[]{"./stockfish-6-src/src/./stockfish"});

            stdOutput = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));
            stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /***
     * Analyses the FEN string with moveTime in ms as input. Sets the cp, pv, and bestMove values.
     * @param fen the FEN string passed in
     * @param moveTime the time for the engine to analyse for in ms
     */
    public void analyseFEN(String fen,int moveTime){
        try {
            String command;
            command="position fen "+fen+"\n";
            stdOutput.write(command);
            stdOutput.flush();

            command="go movetime "+moveTime+"\n";
            stdOutput.write(command);
            stdOutput.flush();

            /***
            command="quit"+"\n";
            stdOutput.write(command);
            stdOutput.flush();
            ****/

            // read the output from the command
            //System.out.println("Here is the standard output of the command:\n");
            String s="";
            Scanner scanner=new Scanner(s);
            MatchResult matchResult;
            while ((s = stdInput.readLine()) != null) {
                //System.out.println(s); //
                if(s.contains("cp")){
                    scanner = new Scanner(s);
                    scanner.findInLine("cp (\\d+)");
                    matchResult = scanner.match();
                    cp = Integer.parseInt(matchResult.group(1));
                }
                if(s.contains("pv")){
                    pv=s.substring(s.indexOf(" pv ")+4);
                }
                if(s.contains("bestmove")) {
                    scanner = new Scanner(s);
                    scanner.findInLine("bestmove (\\w+)");
                    matchResult = scanner.match();
                    bestMove = matchResult.group(1);
                    break;
                }
            }

            /***
             * //Display results
            System.out.println("Last cp: "+cp);
            System.out.println("Last pv: "+pv);
            System.out.println("Best move was "+bestMove);
             ***/

            scanner.close();
            stdInput.close();
            stdOutput.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /***
     * Accessor for the best move
     * @return bestMove found by the Stockfish engine
     */
    public String getBestMove(){
        return this.bestMove;
    }

}
