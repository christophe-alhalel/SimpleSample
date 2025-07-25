package com.ocr.anthony;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.InputMismatchException;
import java.util.Scanner;
import static java.nio.file.StandardOpenOption.APPEND;

/*
 * This class is used to scan user entries and provide menu selection
 */
public class Order {

    private Scanner sc = new Scanner(System.in);
    /** Summary of the current order. */
    private String orderSummary="";

    /**
     * Getter for order summary.
     * @return the formatted summary of the order
     */
    public String getOrderSummary() {
        return orderSummary;
    }

    /**
     * Display a selected menu.
     * @param nbMenu The selected menu.
     */
    public void displaySelectedMenu(int nbMenu) {
        switch (nbMenu) {
            case 1:
                System.out.println("Vous avez choisi comme menu : poulet");
                break;
            case 2:
                System.out.println("Vous avez choisi comme menu : boeuf");
                break;
            case 3:
                System.out.println("Vous avez choisi comme menu : végétarien");
                break;
            default:
                System.out.println("Vous n'avez pas choisi de menu parmi les choix proposés");
                break;
        }
    }

    /**
     * Run asking process for several menus.
     */
    public void runMenus() {
        Path orderPath = Paths.get("order.csv");
        System.out.println("Combien souhaitez vous commander de menu ?");
        int menuQuantity = -1;
        boolean responseIsGood;
        do {
            try {
                menuQuantity = sc.nextInt();
                responseIsGood = true;
            } catch (InputMismatchException e) {
                sc.next();
                System.out.println("Vous devez saisir un nombre, correspondant au nombre de menus souhaités");
                responseIsGood = false;
            }
        } while (!responseIsGood);
        orderSummary = "Résumé de votre commande :%n";
        for (int i = 0; i < menuQuantity; i++) {
            orderSummary += "Menu " + (i + 1) + ":%n";
            String orderLine = runMenu();
            try {
                Files.write(orderPath,
                        String.format(orderLine).getBytes(),
                        StandardOpenOption.CREATE, APPEND);
            } catch (IOException e) {
                System.out.println("Oups une erreur est survenue. Merci de réessayer plus tard");
                return;
            }
        }
        System.out.println("");
        System.out.println(String.format(orderSummary));
    }

    /**
     * Run asking process for a menu.
     */
    public String runMenu() {
        int nbMenu = askMenu();
        int nbSide = -1;
        int nbDrink = -1;
        switch (nbMenu) {
            case 1:
                nbSide = askSide(true);
                nbDrink = askDrink();
                break;
            case 2:
                nbSide = askSide(true);
                break;
            case 3:
                nbSide = askSide(false);
                nbDrink = askDrink();
                break;
        }
        return nbMenu + "," + nbSide + "," + nbDrink + "%n";
    }


    public void displaySelectedSide(int nbSide, boolean allSidesEnable) {

        String[] sides;
        if (allSidesEnable) {
            sides = new String[]{"légumes frais", "frites", "riz"};
        } else {
            sides = new String[]{"riz", "pas de riz"};
        }

        if (nbSide >= 1 && nbSide <= sides.length) {
            System.out.println("Vous avez choisi comme accompagnement : " + sides[nbSide - 1]);
        } else {
            System.out.println("Vous n'avez pas choisi d'accompagnement parmi les choix proposés");
        }
    }

        /**
         * Display a selected drink.
         * @param nbDrink The selected drink.
         */
        public void displaySelectedDrink(int nbDrink) {
            switch (nbDrink) {
                case 1:
                    System.out.println("Vous avez choisi comme boisson : eau plate");
                    break;
                case 2:
                    System.out.println("Vous avez choisi comme boisson : eau gazeuse");
                    break;
                case 3:
                    System.out.println("Vous avez choisi comme boisson : soda");
                    break;
                default:
                    System.out.println("Vous n'avez pas choisi de boisson parmi les choix proposés");
                    break;
            }
        }



    /**
     * Display a question about a category in the standard input, get response and display it
     * @param category the category of the question
     * @param responses available responses
     * @return the number of the selected choice
     */
    public int askSomething(String category, String[] responses) {
        System.out.println("Choix " + category);
        for (int i = 1; i <= responses.length; i++)
            System.out.println(i + " - " + responses[i - 1]);
        System.out.println("Que souhaitez-vous comme " + category + "?");
        int nbResponse = 0;
        boolean responseIsGood;
        do {
            try {
                nbResponse = sc.nextInt();
                responseIsGood = (nbResponse >= 1 && nbResponse <= responses.length);
            } catch (InputMismatchException e) {
                sc.next();
                responseIsGood = false;
            }
            if (responseIsGood) {
                String choice = "Vous avez choisi comme " + category + " : " + responses[nbResponse - 1];
                orderSummary += choice + "%n";
                System.out.println(choice);
            } else {
                boolean isVowel = "aeiouy".contains(Character.toString(category.charAt(0)));
                if (isVowel)
                    System.out.println("Vous n'avez pas choisi d'" + category + " parmi les choix proposés");
                else
                    System.out.println("Vous n'avez pas choisi de " + category + " parmi les choix proposés");
            }
        } while (!responseIsGood);
        return nbResponse;
    }


    /**
     * Display a question about menu in the standard input, get response and display it
     * @return the number of the selected menu
     */
    public int askMenu() {
        String[] menus = {"poulet", "boeuf", "végétarien"};
        return askSomething("menu", menus);
    }

    /**
     * Display a question about side in the standard input, get response and display it
     * @param allSidesEnable
     * @return chosen value
     */
    public int askSide(boolean allSidesEnable) {
        if (allSidesEnable) {
            String[] responsesAllSide = {"légumes frais", "frites", "riz"};
            return askSomething("accompagnement", responsesAllSide);
        } else {
            String[] responsesOnlyRice = {"riz", "pas de riz"};
            return askSomething("accompagnement", responsesOnlyRice);
        }
    }

    /**
     * Display a question about drink in the standard input, get response and display it
     * @return chosen value
     */
    public int askDrink() {
        String[] responsesDrink = {"eau plate", "eau gazeuse", "soda"};
        return askSomething("boisson", responsesDrink);
    }
}
