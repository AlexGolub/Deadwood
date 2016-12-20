/*
 *  File: Game.java 
 */
package deadwood;
import java.util.*;
/**
 *
 * @author Alex
 */
public class Game {
    public static void main(String[] args) {
        
        Sketch.main("deadwood.Sketch");
        
//        Scanner input = new Scanner (System.in);
//        
//        System.out.println("================= Welcome to Deadwood =================");
//        System.out.println();
//        System.out.println("1. NEW GAME");
//        System.out.println("2. QUIT");
//        while (true)
//        {
//            System.out.println();
//            System.out.print("Choose 1 or 2: ");
//            int choice = input.nextInt();
//
//            switch (choice)
//            {
//                case 1: 
//                    System.out.print("Select the number of players (2 to 8 are supported): ");
//                    int numPlayers;
//                    numPlayers = input.nextInt();
//                    while (numPlayers < 2 || numPlayers > 8)
//                    {
//                        System.out.print("Number of players must be 2 to 8: ");
//                        numPlayers = input.nextInt();
//                    }
//                        /*Deadwood dw = new Deadwood(numPlayers);
//                        dw.Play();*/
//                    break;
//                case 2:
//                    System.out.print("Goodbye.");
//                    return;                
//                default:
//                    continue;
//            }        
//        } // end while
    } // end main()
} // end Game
