package objects;

import java.awt.Point;

import adventuregame.Animator;
import adventuregame.Images;
import data.NumberFactory;
import gamelogic.Item;
import gamelogic.NewObjectStorage;
import objects.EnemyMold;

public class Enemy extends NewObject implements EnemyMold {

    int level = 0;
    int contactDamage = 0;
    int health;

    public void level(int level) {this.level = level; scale();}
    public int level() {return level;}
    public double experience() {return experience;}

    protected boolean destructOnDeath = true;

    //drops
    Item drop;
    double experience;

    public Enemy(int level, double experience, int health, String name) {
        super();
        this.level = level;
        this.experience = experience;
        this.health = health;

        setName(name);
        setText(name);

        startCore();
        start();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public void startCore() {
        get().setSize(100, 100);
    }

    public void start() {
        enableHealthModule(health);
        healthModule().showHp(true);
        enableAnimator();
        startAnimator();
        createAI();
        startAI();
        startMisc();

        setImage(Images.getImage("object"));

        scale();
    }

    public void startMisc() {
        shrinkSpeed = 4;
    }

    public void scale() {
        double f = NumberFactory.getEnemyScaling(level);
        experience *= f;
        health = (int) (health * f);
        contactDamage = (int) (contactDamage * f);

        healthModule().setMaxHealth(health);
        healthModule().setHealth(health);
    }

    public void logic() {
        if (healthModule().isDead() && getAI().isEnabled()) {
            die();
        }
        dead();
    }

    public void startAnimator() {
        Animator a = getAnimator();
        a.setIndexRange(0, a.size() -3);
    }

    public void startAI() {

    }

    @Override
    public void collide(NewObject collision) {
        super.collide(collision);
        getAI().collision(collision);
        if (collision.getClass().equals(NewPlayer.class)) {contactDamage(collision);}
    }

    @Override
    /** Called when enemy first dies. */
	public void die() {
        drop();

        //cleanup
        healthModule().showHp(false);
        getAI().setEnabled(false);

        //destruct
        if (destructOnDeath) {
            shrink();
            physics().setGravity(false);
        }
    }

    /** Repeatedly called when enemy is dead. */
    public void dead() {
        if (beenShrunked) {
            destruct();
        }
    }

    public void dropXp() {
        String pname = NewObjectStorage.findNearestPlayer(get().getLocation());
        NewObjectStorage.getPlayer(pname).giveXp((int)experience);
    }
    
    public void drop() {
        dropXp();
        dropItem();
    }

    public void dropItem() {
        if (drop != null) {
            Point loc = get().getLocation();
            ItemObject io = new ItemObject(drop) {{
                this.get().setLocation(loc);
            }};
            NewObjectStorage.add(io);
        }
    }

    public void destruct() {
        super.destruct();
        NewObjectStorage.remove(this);
    }

	@Override
	public void contactDamage(NewObject col) {
        if (contactDamage > 0) {
            col.healthModule().damage(contactDamage);
        }
	}

}