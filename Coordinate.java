/*
 *
 *
	Description: Coordinate class responsible for storing it's information.
			  
	Author: Jaime Acevedo

	Date: 8/05/2014
 
 *
 */

/**
 *
 * @author Acevedo
 */
public class Coordinate {

    int x;
    int y;

   public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    void setX(int newX) {
        x = newX;
    }

    void setY(int newY) {
        y = newY;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }
    
    String print(){
        String out;
        
        out = x +", "+ y;
        return out;
    }

}
