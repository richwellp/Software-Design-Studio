package student;

import student.adventure.Adventure;

public class Main {

    public static void main(final String[] args) {
        Adventure adventure1 = new Adventure();
        String result = adventure1.menu(System.in, System.out);
        // Loops until user gives a valid schema or uses default
        while (!result.equals("Success")) {
            System.out.println(result);
            result = Adventure.menu(System.in, System.out);
        }
        // Loops the player until it decides or reaches exit.
        String progress = adventure1.startAdventure(System.in);
        while (!progress.contains("Game Over")) {
            System.out.println(progress);
            progress = adventure1.startAdventure(System.in);
        }
    }
}