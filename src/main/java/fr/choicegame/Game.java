package fr.choicegame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;

import fr.choicegame.character.Direction;
import fr.choicegame.character.Player;
import fr.choicegame.event.EventComputer;
import fr.choicegame.lwjglengine.IGameLogic;
import fr.choicegame.lwjglengine.TileItem;
import fr.choicegame.lwjglengine.Window;
import fr.choicegame.lwjglengine.graph.Texture;

public class Game implements IGameLogic{

	private HashMap<String, Boolean> triggers;
	private HashMap<String, Integer> globvars;
	
	private EventComputer evComputer;
	private HashMap<String, Map> maps;
	
	private String currentMap;
	
	private Player player;
	
	private List<TileItem> tileItems;
	private final Renderer renderer;
	
	private final JFrame splash;
	
	public enum UserEvent{
		ACTION,
		LEFT,
		RIGHT,
		DOWN,
		UP;
	}
	
	public Game(Loader loader, JFrame splash){
		
		this.splash = splash;
		
		this.triggers = new HashMap<>();
		this.globvars = new HashMap<>();
		
		this.maps = new HashMap<>();
		
		evComputer = new EventComputer(this);
		
		tileItems = new ArrayList<>();
		renderer = new Renderer(loader);
		
		if(loader != null){
			
			this.currentMap = "start"; //Maybe a Constants.java with START_MAP = start ?
		
			this.maps.put(this.currentMap, loader.loadMap(this.currentMap));
		
			//TODO create main character and give it to map
		
		}
		
		
	}
	
	public void onUserEvent(UserEvent e, boolean release){
		switch(e){
		case ACTION:
			break;
		case DOWN:
			break;
		case LEFT:
			break;
		case RIGHT:
			break;
		case UP:
			break;
		}
	}
	
	public boolean getTrigger(String trigname){
		if(this.triggers.containsKey(trigname))
			return this.triggers.get(trigname);
		else
			return false;
	}
	
	public void setTrigger(String trigname, boolean value){
		this.triggers.put(trigname, value);
	}
	
	public int getGlobalVariable(String varname){
		if(this.globvars.containsKey(varname))
			return this.globvars.get(varname);
		else
			return 0;
	}
	
	public void setGlobalVariable(String varname, int value){
		this.globvars.put(varname, value);
	}
	
	public Map getCurrentMap(){
		return this.maps.get(this.currentMap);
	}
	
	public EventComputer getEventComputer(){
		return evComputer;
	}

	public void action(EventComputer listener) {
		int posX = (int) Math.round(player.getPosX()), posY = (int) Math.round(player.getPosY());
		switch(player.getFacing()) {
			case NORTH :
				maps.get(currentMap).getTile(posX, posY-1).getEvent().action(posX, posY-1);
			break;
			case EAST :
				maps.get(currentMap).getTile(posX+1, posY).getEvent().action(posX+1, posY);
			break;
			case SOUTH :
				maps.get(currentMap).getTile(posX, posY+1).getEvent().action(posX, posY+1);
			break;
			case WEST :
				maps.get(currentMap).getTile(posX-1, posY).getEvent().action(posX-1, posY);
			break;
		default:
			break;
		}
	}
	
	public void move(Direction dir, Map map) {
		switch(dir) {
			case NORTH :
				player.setPosY(player.getPosY() - 1);
			break;
			case SOUTH :
				player.setPosY(player.getPosY() + 1);
			break;
			case EAST :
				player.setPosX(player.getPosX() + 1);
			break;
			case WEST :
				player.setPosX(player.getPosX() - 1);
			break;
			case NORTH_EAST:
				player.setPosY(player.getPosY() - 1/Math.sqrt(2));
				player.setPosX(player.getPosX() + 1/Math.sqrt(2));
			break;
			case NORTH_WEST:
				player.setPosY(player.getPosY() - 1/Math.sqrt(2));
				player.setPosX(player.getPosX() - 1/Math.sqrt(2));
			break;
			case SOUTH_EAST:
				player.setPosY(player.getPosY() + 1/Math.sqrt(2));
				player.setPosX(player.getPosX() + 1/Math.sqrt(2));
			break;
			case SOUTH_WEST:
				player.setPosY(player.getPosY() + 1/Math.sqrt(2));
				player.setPosX(player.getPosX() - 1/Math.sqrt(2));
			break;
		}
	}

	@Override
	public void init(Window window) throws Exception {
		System.out.println("Initializing renderer and Loading textures ...");
		renderer.init(window);
		System.out.println("Creating map...");
		this.updateMap();
		System.out.println("Finished loading");
		splash.setVisible(false); //end of loading
	}

	@Override
	public void input(Window window) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float interval) {
		// update every tile
		
		
	}
	
	public void updateMap() {
		// free tile memory
		for(TileItem item:tileItems)
			item.cleanup();
		
		//allocate tiles
		Map m = getCurrentMap();
		for(int x = 0; x < m.getWidth(); x++){
			for(int y = 0; y < m.getHeight(); y++){
				TileImage[] timgs = m.getTile(x, y).getImages();
				Texture[] texs = new Texture[timgs.length];
				int[] ids = new int[timgs.length];
				for(int i = 0; i < timgs.length; i++){
					if(timgs[i] != null){
						ids[i] = timgs[i].getId();
						texs[i] = renderer.getTexture("/tilesets/"+timgs[i].getTileset()+".png");
					}
				}
				TileItem item = new TileItem(texs, ids);
				item.setPosition((x-m.getWidth()/2f), (m.getHeight()/2f-y));
				item.setScale(1.01f); //merge borders;
				tileItems.add(item);
			}
		}
	}

	@Override
	public void render(Window window) {
		renderer.render(window, tileItems);
	}

	@Override
	public void cleanup() {
		renderer.cleanup();
		for(TileItem item:tileItems)
			item.cleanup();
	}
}