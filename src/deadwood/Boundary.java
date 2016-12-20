/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deadwood;

/**
 *
 * @author Alex
 */
public class Boundary {
    public float upperLeftX;
    public float upperLeftY;
    public float lowerRightX;
    public float lowerRightY;
    public float centerX;
    public float centerY;
    public float radiusX;
    public float radiusY;
    public float width;
    public float height;
    
    public Boundary(int x1, int y1, int x2, int y2)
    {
        upperLeftX = x1;
        upperLeftY = y1;
        lowerRightX = x2;
        lowerRightY = y2;
        
        width = x2 -x1;
        height = y2 - y1;
        
        radiusX = width/2;
        radiusY = height/2;
        
        centerX = upperLeftX + radiusX;
        centerY = upperLeftY + radiusY;        
    }
    
     public Boundary(float x1, float y1, float radX, float radY)
     {
         centerX = x1;
         centerY = y1;
         
         radiusX= radX;
         radiusY= radY;
         
         width = radX*2;
         height = radY*2;
         
         upperLeftX = centerX - radiusX;
         upperLeftY = centerY - radiusY;
         lowerRightX = centerX + radiusX;;
         lowerRightY = centerY + radiusY;         
     }
     
     
     public boolean isInBoundary(int x, int y)
     {
         upperLeftX = centerX - radiusX;
         upperLeftY = centerY - radiusY;
         lowerRightX = centerX + radiusX;;
         lowerRightY = centerY + radiusY;      
         
         return x>= upperLeftX && x <= lowerRightX &&
             y>= upperLeftY && y <= lowerRightY ? true: false;
     }
}
