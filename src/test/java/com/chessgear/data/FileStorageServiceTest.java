package com.chessgear.data;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static org.junit.Assert.*;

/*
 *	Author:      Gilbert Maystre
 *	Date:        Dec 7, 2015
 */

public class FileStorageServiceTest {

    private static final Random rand = new Random(1995);
    
    private InputStream prepareConstantInputStream(int size, byte content){
        byte[] bytes = new byte[size];
        
        for(int i = 0; i < size; i++)
            bytes[i] = content;
        
        return new ByteArrayInputStream(bytes);
    }
    
    private InputStream prepareRandomInputStream(){        
        int size = rand.nextInt(50)+10; //generates an int in [10; 60[        
        byte[] bytes = new byte[size];
        
        rand.nextBytes(bytes);
        
        return new ByteArrayInputStream(bytes);
    }
    
    @Test
    public void testAddFileAddsFile(){
        //note: this also tests getFileFor()
        
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();
        
        String user1 = DatabaseServiceTestTool.usernames[0];
        String user2 = DatabaseServiceTestTool.usernames[1];
        
        assertTrue(fss.getFilesFor(user1).size() == 0);
        assertTrue(fss.getFilesFor(user2).size() == 0);
        
        try {
            InputStream is = prepareRandomInputStream();
            fss.addFile(user1, "mypgn.pgn", is);
            is.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        
        assertTrue(fss.getFilesFor(user1).size() == 1);
        assertEquals(fss.getFilesFor(user1).get(0), "mypgn.pgn");
        
        //testing for a side effect now
        assertTrue(fss.getFilesFor(user2).size() == 0);
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testRemoveFileRemovesFile(){
        //note: this also tests getFileFor()
        
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();
        
        String user1 = DatabaseServiceTestTool.usernames[0];
        String user2 = DatabaseServiceTestTool.usernames[1];
        
        assertTrue(fss.getFilesFor(user1).size() == 0);
        assertTrue(fss.getFilesFor(user2).size() == 0);
        
        try {
            InputStream is1 = prepareRandomInputStream();
            fss.addFile(user1, "hello.pgn", is1);
            is1.close();
            
            InputStream is2 = prepareRandomInputStream();
            fss.addFile(user2, "hello.pgn", is2);
            is2.close();
            
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        
        assertTrue(fss.getFilesFor(user1).size() == 1);
        assertTrue(fss.getFilesFor(user2).size() == 1);
        
        assertEquals(fss.getFilesFor(user1).get(0), "hello.pgn");
        assertEquals(fss.getFilesFor(user2).get(0), "hello.pgn");
        
        try {
            fss.removeFile(user1, "hello.pgn");
        } catch (Exception e1) {
            e1.printStackTrace();
            fail();
        }
        
        assertTrue(fss.getFilesFor(user1).size() == 0);
        
        //testing for side-effects now
        assertTrue(fss.getFilesFor(user2).size() == 1);
        assertEquals(fss.getFilesFor(user2).get(0), "hello.pgn");
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testDownloadFile(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();
        String user = DatabaseServiceTestTool.usernames[2];
        
        try {
            fss.addFile(user, "suchagoodplay.pgn", prepareConstantInputStream(100, (byte) 2));
            
            InputStream is = fss.downloadFile(user, "suchagoodplay.pgn");
            InputStream cst = prepareConstantInputStream(100, (byte) 2);
            
            //dirty way to compare two streams.
            int token = 0;
            while((token = is.read()) != -1){
                if(cst.read() != token)
                    fail();
            }
            
            if(cst.read() != -1)
                fail();
            
            is.close();
            cst.close();
            
            DatabaseServiceTestTool.destroyFileStorageService(fss);
            
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
    
    @Test
    public void testWithString(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();
        String user = DatabaseServiceTestTool.usernames[2];
        
        try {
            assertTrue(fss.getFilesFor(user).size() == 0);
            fss.addFile(user, "hello.pgn", "blublabli");
            assertTrue(fss.getFilesFor(user).size() == 1);
            
            //check for a side effect
            assertTrue(fss.getFilesFor(DatabaseServiceTestTool.usernames[1]).size() == 0);
            
            //check that we can well fetch back
            assertEquals(fss.fetchFileContent(user, "hello.pgn"), "blublabli");
            
            //and check that we can fetch a representative stream also
            
            InputStream is = fss.downloadFile(user, "hello.pgn");
            
            //tip found on http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String fetched =  s.hasNext() ? s.next() : "";
            s.close();
            is.close();
            
            assertEquals(fetched, "blublabli");
            
            DatabaseServiceTestTool.destroyFileStorageService(fss);
            
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
        
        
    }
    
    @Test
    public void testAddFilesThrowsExceptionOnNonexistentUser(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();
        
        InputStream is = prepareRandomInputStream();
        try {
            fss.addFile("nonexistentuser", "wontbestoredanyway.pgn", is);
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalArgumentException e2){
            
        }
        
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testRemoveFilesThrowsExceptionOnNonexistentUser(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();

        try {
            fss.removeFile("nonexistentuser", "wontbestoredanyway.pgn");
            fail();
        } catch (IllegalArgumentException e2) {
           
        } catch (IOException e) {

        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testRemoveFilesThrowsExceptionOnNonexistentFile(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();

        try {
            fss.removeFile(DatabaseServiceTestTool.usernames[0], "idontexistsmeeh.pgn");
            fail();
        } catch (IllegalArgumentException e2) {
           
        } catch (IOException e) {

        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testGetFilesForThrowsExceptionOnNonexistentUser(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();

        try {
            fss.getFilesFor("nonexistentuser");
            fail();
        } catch (IllegalArgumentException e) {
            
        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testDownloadFileThrowsExceptionOnNonexistentUser(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();

        try {
            fss.downloadFile("nonexistentuser", "meeh.pgn");
            fail();
        } catch (IllegalArgumentException e) {
           
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testDownloadFileThrowsExceptionOnNonexistentFile(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();

        try {
            fss.downloadFile(DatabaseServiceTestTool.usernames[0], "meeh.pgn");
            fail();
        } catch (IllegalArgumentException e) {
           
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    
    @Test
    public void testFetchFileContentThrowsExceptionOnNonexistentUser(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();

        try {
            fss.fetchFileContent("nonexistentuser", "meeh.pgn");
            fail();
        } catch (IllegalArgumentException e) {
           
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testFetchFileContentThrowsExceptionOnNonexistentFile(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();

        try {
            fss.fetchFileContent(DatabaseServiceTestTool.usernames[0], "meeh.pgn");
            fail();
        } catch (IllegalArgumentException e) {
           
        } catch (IOException e) {
            fail();
            e.printStackTrace();
        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    @Test
    public void testAddFileAsStreamThrowsExceptionOnNonexistentUser(){
        FileStorageService fss = DatabaseServiceTestTool.createFileStorageService();
        
        try {
            fss.addFile("nonexistentuser", "wontbestoredanyway.pgn", "blehblehbleh");
            fail();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        } catch (IllegalArgumentException e2){
            
        }
        
        DatabaseServiceTestTool.destroyFileStorageService(fss);
    }
    
    
    
    
}
