package adventuregame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import worlds.ListWorld;
import worlds.World;

public class MethodAction extends AbstractAction {

	public ListWorld world;
	public String action;

	public MethodAction(String a, ListWorld w) {
		world = w;
		action = a;
	}

	public void actionPerformed(ActionEvent e) {
		if (action == "escp") {
			if (world.optionsactive == false) {
				world.optionsactive = true;
				world.options.visible = true;
				world.switchHud("options");
			} else {
				world.optionsactive = false;
				world.switchHud(world.lastHud);
			}
			System.out.println("world name: " + world.name);
			if (!(world.getName() == "menu") && !(world.getName() == "levels")) {
				world.lastHud = "";
			}
			
		}
		//actualhud
		if (world.optionsactive == false || world.currentHud == "") {
			world.switchHud("actualhud");
		}
		//console
		if (action.equals("consolep") && world.typelistener.getEnabled()) {
			world.typelistener.disable();
			world.typelistener.c.visible = false;
			world.startPlayerController(world.p);
		}
		else if (action.equals("consolep") && world.typelistener.getEnabled() == false) {
			world.typelistener.enable();
			world.typelistener.c.visible = true;
			world.stopPlayerController();
		}
		//shift (sprint)
		if (action.equals("shiftp")) {
			world.p.sprint(true);
		}
		if (action.equals("shiftr")) {
			world.p.sprint(false);
		}
	}
}