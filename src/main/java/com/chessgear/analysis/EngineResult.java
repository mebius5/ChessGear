package com.chessgear.analysis;

/**
 * Created by Grady Xiao on 11/8/15.
 * Class used to store the result returned from Stockfish Engine Analysis
 */

public class EngineResult {
    private double cp; //Stores the score returned by the Stockfish engine in centipawns
    private String pv; //Stores the best line returned by the Stockfish engine
    private String bestMove; //Stores the best move returned by the Stockfish engine

    /***
     * Constructor for EngineResult()
     */
    public EngineResult(){
        this.cp=0;
        this.pv="";
        this.bestMove="";
    }

    /***
     * Function to set the Cp for EngineResult
     * @param cp the result of the engine in centipawns
     */
    public void setCp(double cp){
        this.cp=cp;
    }

    /**
     * Function to set the Pv for EngineResult
     * @param pv  the best line of the analysis
     */
    public void setPv(String pv){
        this.pv=pv;
    }

    /**
     * Function to set the bestMove for EngineResult
     * @param bestMove the best move returned by the engine
     */
    public void setBestMove(String bestMove){
        this.bestMove=bestMove;
    }

    /***
     * Accessor for the Cp
     * @return the score of engine result in centipawns
     */
    public double getCp(){
        return this.cp;
    }

    /**
     * Accessor for the Pv
     * @return the best line of the engine result
     */
    public String getPv(){
        return this.pv;
    }

    /**
     * Accessor for the bestMove
     * @return  the best move of the engine result
     */
    public String getBestMove(){
        return this.bestMove;
    }

}
