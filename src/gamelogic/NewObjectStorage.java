package gamelogic;

import java.awt.Graphics;
import java.util.ArrayList;

import objects.NewObject;
import objects.NewPlayer;

public class NewObjectStorage {

    private static ArrayList<NewObject> objects = new ArrayList<NewObject>();

    //players
    private static NewPlayer player1;
    private static NewPlayer player2;
    private static int playerCount = 0;

    public static ArrayList<NewObject> getObjectList() {
        return objects;
    }

    /** Add object to game.
     * @param object - Initialized object to add to game.
    */
    public static void add(NewObject object) {
        objects.add(object);
    }

    public static void remove(NewObject object) {
        objects.remove(object);
    }

    public static void clearEnvironment() {
        objects.clear();
    }

    public static void addToIndex(NewObject object, int i) {
        objects.add(i, object);
    }

    /** update all objects */
    public static void update() {
        for (int i = 0; i < objects.size(); i++) {
            objects.get(i).update();
        }
        NewCamera.update();
    }

    /** Get playerobject. */
    public static NewPlayer getPlayer(int id) {
        if (id == 1) {
            return player1;
        }
        else if (id == 2) {
            return player2;
        }
        else {
            return null;
        }
    }

    public static int playerCount() {
        return playerCount;
    }

    /** Create a new player and add it to the playerlist. */
    public static void newPlayer() {
        playerCount++;
        if (playerCount == 1) {
            player1 = new NewPlayer(1);
            addToIndex(player1, 0);
        }
        else if (playerCount == 2) {
            player2 = new NewPlayer(2);
            addToIndex(player2, 1);
        }
    }

    /** Find all objects of a certain type and return a list of them. */
    public static ArrayList<?> findObjects(Class<?> type) {
        ArrayList<NewObject> arr = new ArrayList<NewObject>();
        for (NewObject o : objects) {
            if (checkForType(o, type)) {
                arr.add(o);
            }
        }
        return arr;
    }

    /** Check if object is of a certain subclass. */
    public static boolean checkForType(Object candidate, Class<?> type){
        return type.isInstance(candidate);
    }

    //paint all objects to screen
    public static void paint(Graphics g) {
        for (NewObject o : objects) {
            o.paint(g);
        }
    }

}
