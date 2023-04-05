import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.println("Welcome to the card game!");
        System.out.println("Please enter the number of players (2-4): ");
        int numPlayers =sc.nextInt() ;
        while (numPlayers < 2 || numPlayers > 4) {
            System.out.println("Invalid number of players. Please enter a number between 2 and 4.");
numPlayers=sc.nextInt();
        }
        Game game = new Game();
        game.run();
    }
}