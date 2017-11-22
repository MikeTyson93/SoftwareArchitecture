package de.htwg.se.ws1516.fourwinning.view.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import de.htwg.se.ws1516.fourwinning.controller.IGameController;

public class SavegamePanel extends JPanel implements ActionListener{
    private JButton jcomp1;
    private JButton jcomp2;
    private JButton jcomp3;
    private JList jcomp4;
    private JButton jcomp5;
    private static JFrame frame;
    private IGameController spiel;
    private String[] items;
    private DefaultListModel listenModell;

    public SavegamePanel(String[] items, IGameController spiel) {
        //construct preComponents
        String[] jcomp4Items = {};
        this.spiel = spiel;
        jcomp4Items = items;
        this.items = items;
        //construct components
        jcomp1 = new JButton ("Neu");
        jcomp2 = new JButton ("OK");
        jcomp3 = new JButton ("Loeschen");
        jcomp5 = new JButton ("Abbrechen");
        listenModell = new DefaultListModel();
        
        jcomp4 = new JList (listenModell);
        refreshList();
        //adjust size and set layout
        setPreferredSize (new Dimension (396, 366));
        setLayout (null);

        //add components
        add (jcomp1);
        add (jcomp2);
        add (jcomp3);
        add (jcomp4);
        add (jcomp5);
        //set component bounds (only needed by Absolute Positioning)
        jcomp1.setBounds (275, 30, 100, 20);
        jcomp2.setBounds (40, 330, 100, 20);
        jcomp3.setBounds (275, 60, 100, 20);
        jcomp5.setBounds (275, 330, 100, 20);
        jcomp4.setBounds (40, 30, 205, 280);
        
        
        // Action listeners
        jcomp1.addActionListener(this);
        jcomp2.addActionListener(this);
        jcomp3.addActionListener(this);
        jcomp5.addActionListener(this);
    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
		Object quelle = e.getSource();
		if (quelle == jcomp1){
			// Neuen Spielstand erstellen
			String saveGame = JOptionPane.showInputDialog(frame, "Namen des neuen Spielstandes eingeben.");
			if (!spiel.saveToDB(saveGame)){
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog (null, "Spielstand existiert bereits. M�chten Sie diesen �berschreiben?","Warning",dialogButton);
				if(dialogResult == JOptionPane.YES_OPTION){
					spiel.updateToDB(saveGame);
				}
			}
			// REFRESH LIST!
			refreshList();
			
		} else if (quelle == jcomp2){
            String selected = (String)jcomp4.getSelectedValue();
            if (selected == null) {
                frame.dispose();
                return;
            }
            selected = selected.split("\t")[1];
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (null, "Möchten Sie den ausgewählten Spielstand überschreiben?","Warning",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
                spiel.updateToDB(selected);
            }
            frame.dispose();
		} else if (quelle == jcomp3){
			// Ausgew�hlten Spielstand l�schen
			String selected = (String)jcomp4.getSelectedValue();
			selected = selected.split("\t")[1];
			if (!spiel.deleteFromDB(selected)){
				JOptionPane.showMessageDialog(null, "Spielstand konnte nicht gel�scht werden.", "Fehler",
				        JOptionPane.ERROR_MESSAGE);
			}
			refreshList();
		} else if (quelle == jcomp5){
		    // Abbrechen
            frame.dispose();
        }
	}


    public static void buildPanel(String[] items, IGameController spiel) {
        frame = new JFrame ("MyPanel");
        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add (new SavegamePanel(items, spiel));
        frame.pack();
        frame.setVisible (true);
        
    }
    
    public void refreshList(){
    	String[] entries = spiel.AllGridNames();
    	listenModell.removeAllElements();
    	this.items = entries;
    	for (int i = 0 ; i < entries.length; i++){
    		listenModell.addElement(entries[i]);
    	}
    }
}
