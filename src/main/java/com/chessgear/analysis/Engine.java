package com.chessgear.analysis;

import java.io.*;
import java.util.Scanner;
import java.util.regex.MatchResult;

/**
 * Class to facilitate engine analysis.
 * Created by Ran on 10/24/2015.
 */
public class Engine {
    private Process proc; //Java process

    private BufferedReader stdInput; //Input from the Stock engine
    private BufferedWriter stdOutput; //Output to the Stockfish engine

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
     * @throws Exception throws an error if something wrong happens during analyseFEN()
     */
    public EngineResult analyseFEN(String fen,int moveTime) throws Exception{
        //System.out.println("Analyzing fen " + fen);
        try {
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
        } catch (IOException e){
            //e.printStackTrace();
            throw e;
        }
    }

    /***
     * Obtains the result from the Stockfish analysis output and store
     * them into EngineResut
     * Maybe print depending on whether print is true or false
     * @param engineResult Stores the engineResult of the Stockfish analysis
     * @param print Prints the output from Stockfish analysis if True
     * @throws IOException if something goes wrong with the scanner
     */
    private void obtainResultAndMaybePrint(EngineResult engineResult, boolean print) throws IOException {
        // read the output from the command
        //System.out.println("Here is the standard output of the command:\n");
        String s="";
        Scanner scanner=new Scanner(s);
        MatchResult matchResult;
        while ((s = stdInput.readLine()) != null) {
            if(print) {
                System.out.println(s);
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
            System.out.println("Last cp: " + engineResult.getCp());
            System.out.println("Last pv: " + engineResult.getPv());
            System.out.println("Best move: " + engineResult.getBestMove());
            System.out.println("");
        }
        scanner.close();
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
            e.printStackTrace();
            throw e;
        }
    }
}
