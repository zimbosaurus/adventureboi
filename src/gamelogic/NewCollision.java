package gamelogic;

import java.awt.Rectangle;
import java.util.ArrayList;

import objects.NewObject;

public class NewCollision {

    public static void check(NewObject object1) {

        //frequent recreation of arraylist, might result in bad performance?
        ArrayList<NewObject> objects = NewObjectStorage.getObjectList();

        /* Object1 : Current object, object that will be acted upon and moved
           Object2 : other objects from list */

        //loop through all other objects
        for (NewObject object2 : objects) {
            //if object1 is not the same as object2 and they are intersecting
            if (object1.get().intersects(object2.get()) && !object2.equals(object1)) {
                if (object1.getCollision()) {
                    collision(object1, object2);
                    object1.setIntersect(true);
                    object1.passCollision(object2);
                }
            }
            else {
                object1.setIntersect(false);
            }
        }
    }

    /** Calculate collision for two objects. */
    private static void collision(NewObject o1, NewObject o2) {

        //define hitboxes
        Rectangle r1 = o1.get();
        Rectangle r2 = o2.get();

        //determine distance of itersection on all side in pixels
        int r = 0, l = 0, t = 0, b = 0, dx = 0, dy = 0;

        boolean x = false;
        boolean y = false;

        t = checkTop(r1, r2);
        b = checkBottom(r1, r2);
        dy = t+b;

        l = checkLeft(r1, r2);
        r = checkRight(r1, r2);
        dx = r+l;

        //move object1 out of object2
        if (dx < dy) {
            x = true;
        }
        else {
            y = true;
        }
        if (dx < r1.width) {
            x = true;
        }
        if (dy < r1.height) {
            y = true;
        }

        if (x) {
            r1.x = r1.x + dx;
        }
        if (y) {
            r1.y = r1.y + dy;
        }

        if (o1.equals(NewObjectStorage.getPlayer(1))) {
            NewObjectStorage.getPlayer(1).setDebugString(String.valueOf(x + " : " + y));
        }
    }
    
    private static int checkLeft(Rectangle r1, Rectangle r2) {
        int i;
        
        if (r1.x < r2.x) {
            i = (int) ( r2.getMinX() - r1.getMaxX() );
        } else {i = 0;}

        return i;
    }
    
    private static int checkRight(Rectangle r1, Rectangle r2) {
        int i;
        
        if (r1.x > r2.x) {
            i = (int) ( r2.getMaxX() - r1.getMinX() );
        } else {i = 0;}
        
        return i;
    }

    private static int checkTop(Rectangle r1, Rectangle r2) {
        int i;

        if (r1.y < r2.y) {
            i = (int) ( r2.getMinY() - r1.getMaxY() );
        } else {i = 0;}

        return i;
    }
    
    private static int checkBottom(Rectangle r1, Rectangle r2) {
        int i;
        
        if (r1.y > r2.y) {
            i = (int) ( r2.getMaxY() - r1.getMinY() );
        } else {i = 0;}
        
        return i;
    }
}