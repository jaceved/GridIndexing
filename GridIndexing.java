/*
 *
 Description: This class simulates the sorting of spatial data coordinates. It generates a 6 X 6 grid layout and reads in binary coordinates. 
			  The coordinates are then respectfully mapped to their grid in the form of an output file. The program will prompt user to enter 
			  a specific X , Y coordinate. Once the user enters the coordinate it will preform a closestNeighborSearch and find the nearest 
			  coordinate. 
			  
  Author: Jaime Acevedo
	
  Date: 8/05/2014
 
 
 */

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

/**
 *
 * @author Acevedo
 */
public class GridIndexing {

    private final static int numberOfGrids = 36;
    int boundarys[] = new int[7];
    int Grids[][];
    int numOfGridFiles;

    GridIndexing() throws IOException {
        Grids = new int[(int) Math.sqrt(numberOfGrids)][(int) Math.sqrt(numberOfGrids)];
        numOfGridFiles = numberOfGrids;
        this.setBoundarys();
        this.createFiles();

    }

    void sortToGridFiles() throws IOException {
        int offset = 0;
        Coordinate point;
        byte x[];
        byte y[];
        ByteBuffer xBuffer;
        ByteBuffer yBuffer;
        RandomAccessFile zorder = new RandomAccessFile("zorder.bin", "r");
        while (offset < zorder.length()) {
            x = new byte[4];
            y = new byte[4];
            //Set Position for point X
            zorder.seek(offset);
            zorder.read(x);
            offset += 8;
            //Read in X in binary and Convert to int
            xBuffer = ByteBuffer.wrap(x);
            xBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int xPoint = xBuffer.getInt();
            //Set Position for point Y
            zorder.seek(offset);
            zorder.read(y);
            offset += 8;
            //Read in Y in binary and Convert to int 
            yBuffer = ByteBuffer.wrap(y);
            yBuffer.order(ByteOrder.LITTLE_ENDIAN);
            int yPoint = yBuffer.getInt();
            //Create new coordinate Point
            point = new Coordinate(xPoint, yPoint);
            //Append to appropiate GridFile
            this.appendToGridFile(point);
        }
    }

    void setBoundarys() {
        int increment = 16666667;
        int boundary = 0;
        for (int i = 0; i < 7; i++) {
            boundarys[i] = boundary;
            boundary += increment;
            //System.out.println(boundarys[i]);
        }
    }

    void appendToGridFile(Coordinate point) throws IOException {
        int tempX = point.getX();
        //System.out.println(tempX);
        int tempY = point.getY();
        // System.out.println(tempY);
        //Find which GridFile to append to
        int x = this.determineXCell(tempX);
        //System.out.println(x);
        int y = this.determineYCell(tempY);

        //System.out.println(y);
        int GridFile = (int) Math.ceil((Math.sqrt(numberOfGrids) * y) + x);        
        String Coordinate = point.print();
        File f = new File("GridFiles/GridFile_" + GridFile + ".bin");
        if (f.exists() && !f.isDirectory()) {
            FileWriter writer = new FileWriter(f, true);
            writer.append(Coordinate);
            writer.append("\r\n");
            writer.close();
        }

    }

    void createFiles() throws IOException {

        try {
            File dir = new File("GridFiles");
            dir.mkdir();
            for (int i = 0; i < numOfGridFiles; i++) {
                String filename = "GridFiles/GridFile_" + i + ".bin";
                String IndexFile = "Index.bin";
                File file = new File(filename);
                File file2 = new File(IndexFile);
                if (file.createNewFile()) {
                   // System.out.println("File Created");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    int determineXCell(int X) {

        for (int i = 0; i < 7; i++) {
            if (i == 6 && X > boundarys[6]) {
                X = i;
            } else if (X > boundarys[i] && X < boundarys[i + 1]) {
                X = i;
            }
        }

        return X;
    }

    int determineYCell(int Y) {

        for (int i = 0; i < 7; i++) {
            if (i == 6 && Y > boundarys[6]) {
                Y = i;
            } else if (Y > boundarys[i] && Y < boundarys[i + 1]) {
                Y = i;
            }
        }

        return Y;
    }

    double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    void closestNeighborSearch(double x, double y) throws IOException {
        int Result = -1;
        String print = "N/A";
        int xCell = this.determineXCell((int) x);
        int yCell = this.determineYCell((int) y);        
        int GridFile = (int) Math.ceil((Math.sqrt(numberOfGrids) * yCell) + xCell);

        BufferedReader br = new BufferedReader(new FileReader("GridFiles/GridFile_" + GridFile + ".bin"));
        String line;
        String original;
        while ((line = br.readLine()) != null) {
            // process the line.
            original = line;
            String newString = line.replaceAll("\\s+", "");
            String split[] = new String[2];
            split = newString.split(",");
            String tX = split[0];
            String tY = split[1];
            int iX = Integer.parseInt(tX);
            int iY = Integer.parseInt(tY);
            double tResult = this.distance(x, y, iX, iY);

            if (Result == -1) {
                Result = (int) tResult;
                print = original;
            } else if (tResult < Result) {
                Result = (int) tResult;
                print = original;
            }

        }
        br.close();

        
        
        System.out.println(x+", "+y+" closests neighbor is "+print+" in Cell:" + GridFile);

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic her         
        String GridFileDir = "GridFiles";
        FileMgr mgr = new FileMgr();
        File file = new File(GridFileDir);
        //make sure directory exists
    	if(file.exists()){
           try{
               //insert code here
               mgr.delete(file);                       
           }catch(IOException e){
               e.printStackTrace();
               System.exit(0);
           }
        }
        
        
        System.out.println("Creating Grid File's and Index.. Please wait");
        GridIndexing test = new GridIndexing();
        test.sortToGridFiles();
        Scanner in = new Scanner(System.in);
        double x;
        double y;
        
        System.out.println("Please Enter an X Value:");
        x = in.nextDouble();
        while(x>100000000){
            System.out.println("Please Re-enter a Value 0 < X < 100,000,000:");
            x = in.nextDouble();
        }
        
        System.out.println("Please Enter an Y Value:");
        y = in.nextDouble();
        while(y>100000000){
            System.out.println("Please Re-enter a Value 0 < Y < 100,000,000:");
            y = in.nextDouble();
        }
        System.out.println("Please Wait While We Perform a Closest Neighbor Search..");
        test.closestNeighborSearch(x, y);

 

}

    
}
