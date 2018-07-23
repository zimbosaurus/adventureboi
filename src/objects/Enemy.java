package objects;

import java.awt.Rectangle;
import java.util.ArrayList;

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
    int money;

    public void level(int level) {this.level = level; scale();}
    public int level() {return level;}
    public double experience() {return experience;}

    protected boolean destructOnDeath = true;
    boolean dead = false;

    //drops
    ArrayList<Item> drops = new ArrayList<Item>();
    double experience;

    public Enemy(int level, double experience, int health, int money, String name) {
        super();
        this.level = level;
        this.experience = experience;
        this.health = health;
        this.money = money;

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
        health = (int) (health * f);
        contactDamage = (int) (contactDamage * f);
        NumberFactory.getXpScaling(level);
        
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

    public void addDrop(Item item) {
        drops.add(item);
    }

    public int dropAmount() {return drops.size();}

    public boolean isDead() {return dead;}

    @Override
    public void collide(NewObject collision) {
        super.collide(collision);
        getAI().collision(collision);
        
        if (collision.getClass().equals(NewPlayer.class)) {
            playerContact((NewPlayer)collision);
        }
    }

    @Override
    /** Called when enemy first dies. */
	public void die() {

        //cleanup
        healthModule().showHp(false);
        getAI().setEnabled(false);
        dead = true;

        //destruct
        if (destructOnDeath) {
            shrink();
            moveWhenColliding(false);
            physics().setGravity(false);
        }
    }

    @Override
    public void shrinkDone() {
        destruct();
        drop();
    }

    /** Repeatedly called when enemy is dead. */
    public void dead() {
    }

    public void dropXp() {
        String pname = NewObjectStorage.findNearestPlayer(get().getLocation());
        NewObjectStorage.getPlayer(pname).giveXp((int)experience);
    }
    
    public void drop() {
        dropXp();
        dropItem();
    }

    public void setDropLevel() {
        for (Item drop : drops) {
            drop.level(Item.getRandomLevel(level()));
        }
    }

    public void dropItem() {
        for (Item drop : drops) {
            if (drop != null) {
                setDropLevel();
                
                Rectangle e = get();
                ItemObject io = new ItemObject(drop) {{
                    Rectangle i = this.get();
                    this.get().setLocation(e.x - (i.width / 2), e.y - (i.height / 2));
                }};
                NewObjectStorage.add(io);
            }
        }
    }

    public void destruct() {
        super.destruct();
        NewObjectStorage.remove(this);
    }

	public void playerContact(NewPlayer col) {
        if (contactDamage > 0) {
            col.healthModule().damage(contactDamage);
        }
    }
}