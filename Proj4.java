// Brittany Bianco - Project 4, Algorithms and Algorithm Analysis
// Credit to Professor Shultz for the 6-cell dynamic programming model.

import java.util.Scanner;
import java.lang.Math;

public class Proj4{
   private int[] gameList;
   private int[][] optimalMoves;
   private char[][] chooseWhich;
   
   //----------------------------------------
   
   public static void main(String[] args){
      Proj4 newGame = new Proj4();
      
      // 1. Get user input for game list
      newGame.takeInput();
      
      // 2. Build optimal move table
      newGame.buildTable();
      
      // 3. Display table for user
      newGame.showTable();
      
      // 4. Play the game with the user
      newGame.play();
      
   }
   
   //----------------------------------------
   
   public void takeInput(){
   // This method will close the program if the user does not 
   // enter a list of integers as specified by the main method.
      System.out.println("Please enter the number of integers in this game.");
                       
      // take user input
      Scanner s = new Scanner(System.in);
      
      try{
         gameList = new int[s.nextInt()];
         
         s.nextLine();
         
         System.out.println("Please enter any number of positive integers, each separated with a space."
                          + "\nNote that any invalid values will close the game.");
                          
         // split for game list
         String[] temp = s.nextLine().split(" ");
         
         if(gameList.length != temp.length) throw new Exception("Incorrect number of entries.");
         
         // parse entries
         for(int i = 0; i < gameList.length; i++){
            gameList[i] = Integer.parseInt(temp[i]);
            if(gameList[i] <= 0) throw new NumberFormatException("Invalid, non-positive integer value: " + gameList[i]);
         }
         
      } catch (Exception e){
         System.out.println(e.toString());
         System.exit(0);
      }
      
   }
   
   //----------------------------------------
   
   public void buildTable(){
   
      optimalMoves = new int[gameList.length][gameList.length];
      chooseWhich = new char[gameList.length][gameList.length];
      
      for(int i = 0; i < gameList.length; i++){ 
         for(int k = 0; k < gameList.length; k++){
            chooseWhich[i][k] = ' ';
         }
      }
      
      // fill in base cases of type 1: singleton list, main diagonal
      for(int i = 0; i < gameList.length; i++){
         optimalMoves[i][i] = gameList[i];
      }
      
      // fill in base cases of type 2: larger of 2, next diagonal
      for(int i = 0; i < gameList.length - 1; i++){
         if(gameList[i] > gameList[i+1]){
            optimalMoves[i][i+1] = gameList[i];
            chooseWhich[i][i+1] = 'F';
         } else {
            optimalMoves[i][i+1] = gameList[i+1];
            chooseWhich[i][i+1] = 'L';
         }
      }
      
      // fill in rest of cells with basic formula as plotted
      int ri, c1, c2; // right index, choice 1, choice 2
      
      for(int k = 0; k < gameList.length; k++){ // the rest of the diagonals
         for(int i = 0; i < gameList.length-(2+k); i++){
            ri = i+2+k;
            c1 = gameList[ri] + Math.min(optimalMoves[i]  [ri-2], optimalMoves[i+1][ri-1]);
            c2 = gameList[i]  + Math.min(optimalMoves[i+2][ri],   optimalMoves[i+1][ri-1]);
            
            if(c1 > c2){
               optimalMoves[i][ri] = c1;
               chooseWhich[i][ri] = 'L'; // choose last integer
            } else {
               optimalMoves[i][ri] = c2;
               chooseWhich[i][ri] = 'F'; // choose first integer
            }
         }
      }
      
   }
   
   //----------------------------------------
   
   public void showTable(){
      
      System.out.println("\nTable of optimal moves:"
                       + "\nInteger indicates optimal total, character indicates optimal move."
                       + "\nEach cell is defined by a remaining list from the number on the left to the number above.\n");
      
      System.out.print("   "); // aligns top labels with table
      
      // Top labels for table:
      for(int i = 0; i < gameList.length; i++){
         System.out.print("   " + gameList[i] + "     ");
      }
      
      System.out.println();
      
      for(int i = 0; i < gameList.length; i++){
         System.out.print("_________");
      }
      
      System.out.println();
      
      // Table and left labels:
      for(int i = 0; i < gameList.length; i++){
      
         // Left labels for table:
         System.out.print(gameList[i] + " |");
         
         // Table values:
         for(int k = 0; k < gameList.length; k++){
            System.out.print(String.format("%4s", optimalMoves[i][k] + " "));
            System.out.print(chooseWhich[i][k] + "    ");
         }
         
         System.out.println("\n  |");
      }
      
   }
   
   //----------------------------------------
   
   public void play(){
      // This method will close the program if an invalid character is entered.
      
      Scanner s = new Scanner(System.in);
      int li = 0, ri = gameList.length-1; // indeces of current list
      char userMove;
      int userTotal = 0, compTotal = 0;
      
      System.out.println("Please enter 'F' or 'L' for your first move.\n"
                       + "'F' indicates you'll take the first number, and 'L' indicates you'll take the last.");
      
      // Begin game
      while(li <= ri){
      
         // Print current list
         System.out.println("\nCurrent remaining list: ");
         
         System.out.print("F -> ");
         for(int i = li; i <= ri; i++){
            System.out.print(gameList[i] + " ");
         }
         System.out.print("<- L");
         
         System.out.println("\nCurrent totals: "
                          + "\n  User: " + userTotal 
                          + "\n  Computer: " + compTotal);
         
         // User's move
         System.out.print("\nYour move: ");
         userMove = s.nextLine().toUpperCase().charAt(0);
         
         while(!(userMove == 'F' || userMove == 'L')){
            System.out.println("Sorry, that's an invalid entry.\n"
                             + "Please enter F or L.");
            userMove = s.nextLine().toUpperCase().charAt(0);
         }
         
         switch(userMove){
            case 'F':
               userTotal += gameList[li];
               li++;
               break;
            case 'L':
               userTotal += gameList[ri];
               ri--;
               break;
            default: 
               System.out.println("This should be unreachable.");
               System.exit(0);
               break;
         }
         
         if(!(li > ri)){ // This is to ensure the computer doesn't try to act with no remaining list 
                         // (when the list begins with an odd number of integers).
            // Computer's move
            if(chooseWhich[li][ri] == 'F'){
               compTotal += gameList[li];
               li++;
            } else {
               compTotal += gameList[ri];
               ri--;
            }
         }
         
      } // end while
      
      System.out.println("\nYour total: " + userTotal + "\n"
                       + "Computer's total: " + compTotal + "\n"
                       + "Winner: " + (userTotal >= compTotal ? "user" : "computer"));
      
   }
   
} // end class