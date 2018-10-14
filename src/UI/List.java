package UI;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Collection;

import adventuregame.Position;
import gamelogic.Numbers;

public class List extends UIObject {

    Collection<EntryData> entries;

    int yMargin = 50, ySpacing = 50;
    int NAME_OFFSET = -50;

    public List(String parentname) {
        super(parentname);
        style();
    }

    void style() {
        setBackgroundColor(getParent().getUITextColor());
        setBorder(getParent().getUIBackgroundColor(), 15);
        get().setSize(500, 500);
    }

    @SuppressWarnings("unchecked")
    public void giveList(Collection<? extends EntryData> entries) {
        this.entries = (Collection<EntryData>) entries;
        EntryData.initEntries(this.entries, getParent());
    }

    /**
     * Takes a location relative to this {@code List} from for example
     * {@code getRelativeEntryLocation()} and returns the absolute position on the screen.
     * @param location the location to convert
     * @return an absolute position onscreen.
     */
    public Point unrelativize(Point location) {
        return new Point(get().x + location.x, get().y + location.y);
    }

    /**
     * Get location of entry relative to this {@code List}.
     * @param e the entry in question
     * @return location of the entry
     */
    public Point getRelativeEntryLocation(EntryData e) {
        Point p = new Point(0,0);

        p.y = yMargin + e.getIndex() * (e.getEntry().getFullHeight() + ySpacing);
        
        e.getEntry().setParentRectangle(this.get());
        e.getEntry().centerInParentX(true);

        return p;
    }

    /**
     * Refresh the location of a single {@code EntryData}.
     * @param e the entry.
     */
    protected void refreshEntryLocation(EntryData e) {
        e.getEntry().get().setLocation( unrelativize( getRelativeEntryLocation(e)));
    }

    /**
     * Do a {@code refreshEntryLocation()} on every entry in this {@code List}.
     */
    protected void refreshEntryLocations() {

        for (EntryData d : entries) {
            refreshEntryLocation(d);
        }
    }

    void moveEntry(EntryData d, int x, int y) {
        UIObject e = d.getEntry();
        e.get().translate(x, y);
    }

    /**
     * Move all entries in this {@code List} vertically, "scroll" as some might say.
     * @param amount of pixels to scroll
     */
    protected void scroll(int amount) {
        for (EntryData e : entries) {
            e.getEntry().get().translate(0, amount);
        }
        limitEntries();
    }

    /**
     * Limit entries in certain ways and check if they are out of the bounds of this {@code List}.
     * Make them not visible if they are outside the list.
     */
    void limitEntry(EntryData d) {
        UIObject e = d.getEntry();
        boolean visible = true;

        if (Numbers.checkMaxLimit(e.get().getMaxY(), get().getMaxY())) {
            visible = false;
        }
        else if (Numbers.checkMinLimit(e.get().getMinY(), get().getMinY())) {
            visible = false;
        }

        e.setVisible(visible);
    }

    /**
     * Limit all entries in this {@code List}. Perform a {@code limitEntry()}
     * on every {@code EntryData}.
     */
    void limitEntries() {
        for (EntryData d : entries) {
            limitEntry(d);
        }
    }

    /** Perform all neccessary refreshing on entries. */
    public void refresh() {
        refreshEntryLocations();
        //limitEntries();
    }

    /**
     * Refresh list while also giving it a new collection of entries before refreshing.
     * @param entries said collection of entries to be refreshed with.
     */
    public void refresh(Collection<? extends EntryData> entries) {
        giveList(entries);
        refresh();
    }

    @Override
    public void leftMouseReleased() {
        super.leftMouseReleased();
        for (EntryData d : entries) {
            if (d.getEntry().checkMouse()) {
                d.getEntry().leftMouseReleased();
            }
        }
    }

    @Override
    public void update() {
        super.update();
        for (EntryData d : entries) {
            d.getEntry().update();
        }
    }

    /**
     * Display the name of this list above it using {@code getText()}.
     * @param g graphics.
     */
    private void listNamePlacement(Graphics g) {
        String text = getText();
        int width = g.getFontMetrics().stringWidth(text), height = g.getFontMetrics().getHeight();

        textCenterHeight = NAME_OFFSET - height;
        textCenterWidth = (getFullWidth() / 2) - (width / 2);
    }

    @Override
    public void paint(Graphics g) {
        listNamePlacement(g);
        super.paint(g);
        for (EntryData d : entries) {
            d.getEntry().paint(g);
        }
    }

}