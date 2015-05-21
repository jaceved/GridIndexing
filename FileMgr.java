/*
 *
 *
	Description: This class organizes the grid index files and creates the directory if needed.
			  
	Author: Jaime Acevedo

	Date: 8/05/2014
 
 *
 */

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Acevedo
 */
public class FileMgr {

    
    
        
    public void delete(File file) throws IOException{
 
    	if(file.isDirectory()){
 
    		//directory is empty, then delete it
    		if(file.list().length==0){
 
    		   file.delete();
    		   System.out.println("Previous Directory is deleted : " 
                                                 + file.getAbsolutePath());
 
    		}else{
 
    		   //list all the directory contents
        	   String files[] = file.list();
 
        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);
 
        	      //recursive delete
        	     delete(fileDelete);
        	   }
 
        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
        	     System.out.println("Previous Directory is deleted : " 
                                                  + file.getAbsolutePath());
        	   }
    		}
 
    	}else{
    		//if file, then delete it
    		file.delete();
    		System.out.println("Previous File is deleted : " + file.getAbsolutePath());
    	}
    }
}
