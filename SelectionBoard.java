import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SelectionBoard extends JFrame{

	private JLabel rowsOfInvadersL = new JLabel("Rows of invaders: ");
    private JLabel columnsOfInvadersL = new JLabel("Columns of invaders: ");
    private JLabel rateInvadersMoveL = new JLabel("Rate invaders go back and forth: ");
    private JLabel rateInvadersDropDownL = new JLabel("Rate invaders drop down: ");
    private JLabel rateInvadersFireL = new JLabel("Rate invaders fire: ");
    private JLabel defenderLivesL = new JLabel("Number of defender lives: ");
    private JTextField textrowsOfInvadersL = new JTextField(20);
    private JTextField textcolumnsOfInvadersL = new JTextField(20);
    private JTextField textrateInvadersMoveL = new JTextField(20);
    private JTextField textrateInvadersDropDownL = new JTextField(20);
    private JTextField textrateInvadersFireL = new JTextField(20);
    private JTextField textdefenderLivesL = new JTextField(20);
    private JButton startGame = new JButton("Start Game");
    private boolean rest;
    
    public GameManager gameManager;
    
    public SelectionBoard(){
		super("Space Invader");
        JPanel newPanel = new JPanel(new GridBagLayout());
        
    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    	ActionListener startListener = new StartListener();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
         
        constraints.gridx = 0; constraints.gridy = 0;     
        newPanel.add(rowsOfInvadersL, constraints);
        constraints.gridx = 1;
        newPanel.add(textrowsOfInvadersL, constraints);
         
        constraints.gridx = 0; constraints.gridy = 1;     
        newPanel.add(columnsOfInvadersL, constraints);
        constraints.gridx = 1;
        newPanel.add(textcolumnsOfInvadersL, constraints);
         
        constraints.gridx = 0; constraints.gridy = 2;     
        newPanel.add(rateInvadersMoveL, constraints);
        constraints.gridx = 1;
        newPanel.add(textrateInvadersMoveL, constraints);
        
        constraints.gridx = 0; constraints.gridy = 3;     
        newPanel.add(rateInvadersDropDownL, constraints);
        constraints.gridx = 1;
        newPanel.add(textrateInvadersDropDownL, constraints);     

        constraints.gridx = 0; constraints.gridy = 4;     
        newPanel.add(rateInvadersFireL, constraints);
        constraints.gridx = 1;
        newPanel.add(textrateInvadersFireL, constraints);
        
        constraints.gridx = 0; constraints.gridy = 5;     
        newPanel.add(defenderLivesL, constraints);
        constraints.gridx = 1;
        newPanel.add(textdefenderLivesL, constraints);
        
        constraints.gridx = 0; constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        startGame.addActionListener(startListener);
        newPanel.add(startGame, constraints);

        newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Selecting features"));
        add(newPanel);         
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        rest = true;
    	gameManager = new GameManager();
        while(rest){
        	try{
        		Thread.sleep(20);
        	}catch(InterruptedException ex) { }
        }
//        System.out.println(gameManager.rowsOfInvaders);
//        System.out.println(gameManager.columnsOfInvaders);
//        System.out.println(gameManager.rateInvadersMove);
//        System.out.println(gameManager.rateInvadersDropDown);
//        System.out.println(gameManager.rateInvadersFire);
//        System.out.println(gameManager.defenderLives);
        gameManager.run();
	}
	
	class StartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	try{gameManager.rowsOfInvaders = Integer.parseInt(textrowsOfInvadersL.getText());}catch(Exception ex){}
        	try{gameManager.columnsOfInvaders = Integer.parseInt(textcolumnsOfInvadersL.getText());}catch(Exception ex){}
        	try{gameManager.rateInvadersMove = Integer.parseInt(textrateInvadersMoveL.getText());}catch(Exception ex){}
        	try{gameManager.rateInvadersDropDown = Integer.parseInt(textrateInvadersDropDownL.getText());}catch(Exception ex){}
        	try{gameManager.rateInvadersFire = Integer.parseInt(textrateInvadersFireL.getText());}catch(Exception ex){}
        	try{gameManager.defenderLives = Integer.parseInt(textdefenderLivesL.getText());}catch(Exception ex){}
        	setVisible(false);
        	dispose();
        	rest = false;
        }
     }
}
