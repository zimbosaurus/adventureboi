package objects;

import adventuregame.Images;

public class AngryShroom extends NewObject implements ObjectMethods {

    public AngryShroom() {
    }

    public void initialize() {
        super.initialize();
        super.setImage(Images.getImage("angryshroom"));
        enableAnimator();
        createAI();
        getAnimator().addList(Images.getFolderImages("assets/animated_sprites/angryshroom"));
        getAnimator().setIndexRange(0, 3);
        getAnimator().speed(5);
    }

    public void ai() {
        super.ai();
    }

    public void logic() {
        
    }

    public void animate() {
        super.animate();
    }
    
	public void intersect() {
    }

    public void update() {
        super.update();
    }

    public void destruct() {
        super.destruct();
    }
}
