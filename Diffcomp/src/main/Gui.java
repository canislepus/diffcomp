package main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Gui extends JFrame implements ActionListener, ChangeListener, MouseListener{

	DiffData data;
	JList<String> datlist;
	JTextField namefield;
	JSpinner pWeightingSpinner, cWeightingSpinner;			//Spinners for pro-/con weighting factors
	JButton stateButton;
	
	String openfile;
	
	boolean state = true;		//True: submits increase pro; False: -||- con
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6896552375386521626L;

	public Gui(String title, int w, int h){
		super(title);
		this.setSize(w, h);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		data = new DiffData();							//initialize data
		try{
			data.loadFromFile("autoload.dat");		//load data from auto load file
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
		
		JPanel namePanel = new JPanel();
		JLabel nameLabel = new JLabel("Name:");
		namefield = new JTextField(30);
		JButton submitButton = new JButton("Submit");
		submitButton.setActionCommand("submit");
		submitButton.addActionListener(this);
		namePanel.add(nameLabel, BorderLayout.WEST);
		namePanel.add(namefield,BorderLayout.CENTER);
		namePanel.add(submitButton, BorderLayout.EAST);
		namePanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		getContentPane().add(namePanel, BorderLayout.NORTH);
		
		JPanel listPanel = new JPanel();
		//data.getSortedList();
		datlist = new JList<String>((String[])data.getSortedStringList().toArray(new String[0]));
		datlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		datlist.addMouseListener(this);
		listPanel.add(datlist);
		getContentPane().add(new JScrollPane(listPanel), BorderLayout.CENTER);
		
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem savemenuitem = new JMenuItem("Save");
		savemenuitem.setActionCommand("save");
		savemenuitem.addActionListener(this);
		menu.add(savemenuitem);
		JMenuItem saveasmenuitem = new JMenuItem("Save as");
		saveasmenuitem.setActionCommand("saveas");
		saveasmenuitem.addActionListener(this);
		menu.add(saveasmenuitem);
		JMenuItem loadmenuitem = new JMenuItem("Load");
		loadmenuitem.setActionCommand("load");
		loadmenuitem.addActionListener(this);
		menu.add(loadmenuitem);
		JMenuItem addfilemenuitem = new JMenuItem("Add file");
		addfilemenuitem.setActionCommand("addfile");
		addfilemenuitem.addActionListener(this);
		menu.add(addfilemenuitem);
		menubar.add(menu);
		JMenuItem clearmenuitem = new JMenuItem("Clear");
		clearmenuitem.setActionCommand("clear");
		clearmenuitem.addActionListener(this);
		menu.add(clearmenuitem);
		this.setJMenuBar(menubar);
		
		JPanel buttonPanel = new JPanel();
		stateButton = new JButton("State: " + (state ? "Pro": "Con"));
		stateButton.setActionCommand("togglestate");
		stateButton.addActionListener(this);
		buttonPanel.add(stateButton);
		JPanel weightingPanel = new JPanel();
		pWeightingSpinner = new JSpinner();
		pWeightingSpinner.setValue(1);
		pWeightingSpinner.addChangeListener(this);
		cWeightingSpinner = new JSpinner();
		cWeightingSpinner.setValue(1);
		cWeightingSpinner.addChangeListener(this);
		weightingPanel.add(new JLabel("Weighting factors"), BorderLayout.NORTH);
		JPanel wspinnerpanel = new JPanel();
		wspinnerpanel.add(new JLabel("Pro: "));
		wspinnerpanel.add(pWeightingSpinner);
		wspinnerpanel.add(new JLabel("     Con: "));
		wspinnerpanel.add(cWeightingSpinner);
		weightingPanel.add(wspinnerpanel, BorderLayout.CENTER);
		buttonPanel.add(weightingPanel);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		
		
		this.setVisible(true);
	}
	
	public String filePrompt(String title){
		JFileChooser prompt = new JFileChooser(title);
		prompt.setFileFilter(new FileNameExtensionFilter("DAT files", "dat"));
		int returnval = prompt.showOpenDialog(this);
		if(returnval == JFileChooser.APPROVE_OPTION){
			return prompt.getSelectedFile().getPath();
		}
		
		return null;
	}
	
	public void updateList(){
		datlist.setListData(data.getSortedStringList().toArray(new String[0]));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("submit")){
			String key = namefield.getText().toLowerCase();
			if(!data.contains(key)){
				data.add(new ProCon(key, 0, 0));
			}else{
				if(state){
					data.pro(key, 1);
				}else{
					data.con(key, 1);
				}
			}
			updateList();
		}
		
		if(e.getActionCommand().equals("save")){
			String path;
			if(openfile != null){
				path = openfile;
			}else{
			path = filePrompt("Save");
			}
			if(path != null){
				try{
					if(!(path.endsWith(".dat") || path.endsWith(".DAT"))){
						path = path.concat(".dat");
					}
					data.saveToFile(path);
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			}
		}
		
		if(e.getActionCommand().equals("saveas")){
			String path = filePrompt("Save");
			if(path != null){
				try{
					if(!(path.endsWith(".dat") || path.endsWith(".DAT"))){
						path = path.concat(".dat");
					}
					data.saveToFile(path);
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			}
		}
		
		if(e.getActionCommand().equals("load")){
			String path = filePrompt("Load");
			if(path != null){
				try{
					data.clear();
					data.loadFromFile(path);
					updateList();
					openfile = path;
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			}
		}
		
		if(e.getActionCommand().equals("addfile")){
			String path = filePrompt("Add file");
			if(path != null){
				try{
					data.loadFromFile(path);
					updateList();
				}catch(IOException ioe){
					System.out.println(ioe.getMessage());
				}
			}
		}
		
		if(e.getActionCommand().equals("togglestate")){
			state = !state;
			stateButton.setText("State: " + (state ? "Pro" : "Con"));
		}
		
		if(e.getActionCommand().equals("clear")){
			if(JOptionPane.showConfirmDialog(this, "Clear data?") == JOptionPane.YES_OPTION){
				data.clear();
				updateList();
			}
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {
		try {
			pWeightingSpinner.commitEdit();
			cWeightingSpinner.commitEdit();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ProCon.proWeight = (Integer) pWeightingSpinner.getValue();
		ProCon.conWeight = (Integer) cWeightingSpinner.getValue();
		
		updateList();
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2){
			String selection = datlist.getSelectedValue();
			selection = selection.substring(0, selection.lastIndexOf("     "));				//Remove value
			namefield.setText(selection);
		}
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
