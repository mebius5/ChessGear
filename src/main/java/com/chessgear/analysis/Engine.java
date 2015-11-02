package com.chessgear.analysis;

import java.io.*;

/**
 * Class to facilitate engine analysis.
 * Created by Ran on 10/24/2015.
 */
public class Engine {
    Runtime rt;
    Process proc;

    BufferedReader stdInput;
    BufferedReader stdError;

    public Engine(){
    }

    public void executeThis(){
        try {
            rt = Runtime.getRuntime();
            String command[] = {"cd", "./stockfish-6-src"};
            proc = rt.exec(command);

            OutputStreamWriter stdOutput = new OutputStreamWriter(proc.getOutputStream());
            stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            stdOutput.write("ls");
            stdOutput.flush();

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            String s;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            stdInput.close();
            stdError.close();
            stdOutput.close();

        } catch (IOException e){
            System.err.print(e.getMessage());
        }
    }

}
