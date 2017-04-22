package fr.choicegame;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import fr.choicegame.lwjglengine.GameEngine;
import fr.choicegame.lwjglengine.IGameLogic;


public class Splash extends JFrame{

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		Splash splash = new Splash();
		Loader load = splash.run();
		if(load != null){
	        boolean vSync = true;
	        try{
	            IGameLogic gameLogic = new Game(load, splash);
	            GameEngine gameEng = new GameEngine("Choice game",
	                600, 480, vSync, gameLogic);
	            gameEng.start();
	        } catch (Exception excp) {
	            excp.printStackTrace();
	            System.exit(-1);
	        }
		}
	}
	
	public Splash(){
		ImageIcon splashImg = new ImageIcon(this.getClass().getResource("/splash.jpg"));
		this.setSize(splashImg.getIconWidth(), splashImg.getIconHeight());
		this.setUndecorated(true);
		this.setLocationRelativeTo(null);
		JLabel img = new JLabel(splashImg);
		this.getContentPane().add(img);
		this.setVisible(true);
	}
	
	public Loader run(){

		Loader loader = new Loader();
		
		if(loader.load()){
			//this.setVisible(false);
			return loader;
		}else{
			this.setVisible(false);
			JOptionPane.showMessageDialog(this, "Couldn't load game assets.",
					"Loading error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	

}
