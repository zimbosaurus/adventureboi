package UI;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import adventuregame.GlobalData;
import gamelogic.MouseFunctions;

public class UIList extends UIObject {

    protected UIObject handle;
    protected int handleHeight = 50;
    protected double listContentHeight = 0;
    /** How many percent larger/smaller the content of the list is than the list. */
    protected double listContentPercent = 0;

    protected boolean pressed = false;
    protected int contentSpacing = 50;

    protected int contentScrollHeight = 0;
    protected int entryHeight = 50;
    protected int entryWidth = 100;
    protected boolean entryFullWidth = true;

    public float scrollSpeedMultiplier = 20;

    /** The distance the handle is offset from the mouse when holding/pressing on scrollbar. */
    protected int handleHoldOffset = 0;
    protected int scrollbarWidth = 50;
    protected Rectangle scrollbar;

    protected UIObject entry;
    public UIObject entry() {return entry;}
    public void setEntry(UIObject o) {entry = o;}

    protected ArrayList<UIObject> list = new ArrayList<UIObject>();
    public ArrayList<UIObject> getList() {return list;}

    public UIList(String parentname) {
        super();
        setParentName(parentname);
        start();
        setEntry();
    }

    protected void setEntry() {
        entry = new UIObject(getParentName());
    }
    
    public void isEntryFullWidth(boolean b) {entryFullWidth = b;}
    public Rectangle getScrollbar() {return scrollbar;}
    public UIObject handle() {return handle;}
    public void setSpacing(int i) {contentSpacing = i;}
    
    public void start() {
        handle = new UIObject() {
            @Override
            public void leftMousePressed() {
                super.leftMousePressed();
                pressed = true;
            }
            @Override
            public void leftMouseReleasedSomewhere() {
                super.leftMouseReleasedSomewhere();
                pressed = false;
            }
        };
        handle.setParentName(getParentName());
        handle.setBackgroundColor(getParent().getUIBackgroundColor());
        handle.get().height = handleHeight;
        scrollbar = new Rectangle( (int) get().getMaxX() - scrollbarWidth, (int) get().getMinY(), scrollbarWidth, get().height);
    }

    public UIObject getEntry(String text) {
        UIObject o = new UIObject();
        this.text = text;

        cloneEntry(o);

        return o;
    }

    public UIObject cloneEntry(UIObject o) {
        o.setParentName(getParentName());
        o.setTag(tag());
        o.setText(text);
        o.setParentRectangle(get());
        o.centerTextY(true);
        o.setFontSize(getFontSize());
        o.setBackgroundPadding(entry().getBackgroundPadding());
        
        o.takeInput(entry().takeInput());
        o.setBackgroundColor(entry().getBackgroundColor());
        o.textColor(entry().getTextColor());
        o.setHoverTextColor(entry().getHoverTextColor());
        o.hoverColorChange(entry().getHoverBackgroundColor());

        if (entryFullWidth) {o.get().setSize(this.getFullWidth() - scrollbarWidth, entryHeight);}
        else {o.get().setSize(entryWidth, entryHeight);}

        return o;
    }

    public void refreshList(String[] content) {
        list.clear();
        for (String t : content) {
            list.add(getEntry(t));
        }
        determineContentHeight();
        determineHandleHeight();
    }

    protected void calcEntryHeight() {
    }

    protected void determineEntryLocation() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).get().y = get().y + (contentSpacing * (i)) + (i * entryHeight) + entry().getBackgroundPadding() / 2 + contentScrollHeight;
            list.get(i).get().x = get().x;
        }
    }

    public void scroll(int distance) {
        contentScrollHeight = distance;
    }
    public int getScrollHeight() {return contentScrollHeight;}

    @Override
    public void scroll(MouseWheelEvent e) {
        super.scroll(e);
        handle.get().y += e.getWheelRotation() * scrollSpeedMultiplier;
    }

    protected void determineContentHeight() {
        //content height
        int s = list.size();
        int height = 0;
        for (int i = 1; i <= s; i++) {
            height += contentSpacing + entryHeight;
        }
        listContentHeight = height;
        //content percent
        listContentPercent = (get().height / listContentHeight);
        if (listContentPercent > 1) {listContentPercent = 1;}
    }

    protected void determineHandleHeight() {
        double h = get().height * listContentPercent;
        if (h > get().height) {h = get().height;}
        handle.get().height = (int) h;
    }

    protected boolean isPressed() {return handle.checkMouse() && MouseFunctions.getClickListener().isLeftPressed();}
    
    /** Move handle with mouse. */
    protected void moveHandle() {
        //first press
        if (!pressed && isPressed()) {
            handleHoldOffset = handle.get().y - GlobalData.getMouse().y;
            pressed = true;
        }
        //logic
        if (!MouseFunctions.getClickListener().isLeftPressed()) {pressed = false;}
        //while holding
        if (pressed) {
            handle.get().y = GlobalData.getMouse().y + handleHoldOffset;
        }
    }

    public void leftMouseReleased() {
        for (UIObject o : list) {
            if (o.checkMouse() && getParent().getObjectsThatStartsWithTag("alert").length == 0) {
                MouseFunctions.executeListAction(o);
            }
        }
    }

    protected void hideOverflow(UIObject o) {
        if (o.get().getMinY() < get().getMinY() || o.get().getMaxY() > get().getMaxY()) {
            o.setVisible(false);
        }
        else {o.setVisible(true);}
    }

    protected void calculateScrollLength() {
        int handleDistance = (int) (get().getMinY() - handle.get().getMinY());
        double scrollRatio = ( ((double) handleDistance ) / get().height) + 1;        
        int scrollDistance = (int) ( (listContentHeight * scrollRatio) - listContentHeight);
        if (scrollDistance != 0) {
            scroll(scrollDistance);
        }
    }

    /** Prevent handle from leaving scrollbar area. */
    protected void limitHandle() {
        if (handle.get().getMaxY() > get().getMaxY()) {
            handle.get().y = (int) get().getMaxY() - handle.get().height;
        }
        if (handle.get().y < get().getMinY()) {
            handle.get().y = (int) get().getMinY();
        }
        if (handle.get().x != get().getMaxX() - handle.get().width) {
            handle.get().x = (int) get().getMaxX() - handle.get().width;
        }
    }
    
    public void update() {
        super.update();
        handle.update();
        calcEntryHeight();
        moveHandle();
        limitHandle();
        calculateScrollLength();
        determineContentHeight();
        determineEntryLocation();
        for (UIObject o : list) {o.update(); hideOverflow(o);}
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        handle.paint(g);
        for (UIObject o : list) {o.paint(g);}
    }

}
