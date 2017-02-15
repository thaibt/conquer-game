import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

/**
   A program that allows users to edit a scene composed
   of items.
*/
public class ConquerGame
{
   public static void main(String[] args) throws IOException
   {
      JFrame frame = new JFrame();

		
      final GameBoard scene = new GameBoard();
      final StatusDisp disp = new StatusDisp();
      
      JPanel options = new JPanel();
      final JButton help = new JButton("How to Play?");
      help.addActionListener(new ActionListener() 
		{
			public void actionPerformed (ActionEvent ae){
				JOptionPane.showMessageDialog(null, "<html> Hello and Welcome to Conquer! Here is a quick guide on how to play the game! <br>" +
						"1. Each team has a base on opposite corners. The base has 50 health points. <br>"+
						"2. Each team starts with a Miner, which is used to obtain resources. <br>"+
						"3. To obtain resources, move the Miner to nearby crystal or wood. Click the Miner and then the resource to claim it. <br>"
						 + "Be careful, as your opponent can attack and destroy your resources! <br>" +
						"4. Every turn, claimed resources generate new resources for your base. Resources generate up to 20 pieces before disappearing from the map. <br>"
						+ "You can spend resources by clicking on your base and selecting an option during your turn. <br>" +
						"5. You have two base options. Repairing a castle gives the castle +10 hp. \"Summon a warrior\" will summon a Warrior with 4 attack points near your base. <br>" +
						"A Miner only deals 2 attack points and has 10 hp less than warrior. <br>" +
						"6. Watch out for the neutral Enemies. They have 6 hp and 3 attack points and will seek out the closest units and castle to attack. <br>" +
						"Enemies move after both players have made their move. <br>" +
						"7. The goal of the game is to destroy all of your opponent's units or destroy their base. </html>");		
				}
		
		});
		
      JButton forfeit = new JButton("Surrender!");
      forfeit.addActionListener(new ActionListener()
      {
    	  public void actionPerformed (ActionEvent ae){
    		  scene.resetBoard();
    	  }
      });
      
		
      options.add(help);
      options.add(forfeit);
      
      frame.add(scene, BorderLayout.CENTER);
      frame.add(disp, BorderLayout.EAST);
      frame.add(options, BorderLayout.SOUTH);
      
      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
      frame.setSize (1200, 1000);
      frame.setVisible (true);
      
   }
}


