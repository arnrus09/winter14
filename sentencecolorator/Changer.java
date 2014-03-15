package sentencecolorator;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

import java.util.*;
import java.util.concurrent.TimeUnit;



public class Changer extends JFrame{
	static final int WIDTH = 800;
	JPanel 
		jp1 = new JPanel(),
		jp2 = new JPanel(),
		jp3 = new JPanel(),
		jp4 = new JPanel();
	JButton chbutton = new JButton("change");
	JTextArea jte = new JTextArea();
	JScrollPane 
		jsp1 = new JScrollPane(jte),
		jsp2 = new JScrollPane(jp3);
	Wrappers ws;
	JRadioButton 
		sing = new JRadioButton("sing", true),
		speak = new JRadioButton("speak", false)
		//nrml = new JRadioButton("normal", false),
		//asct = new JRadioButton("associate", true);
	//ButtonGroup bg1 = new ButtonGroup()
	;
	Set<JButton> jbset = new HashSet<JButton>();
	java.util.List<String> tagged = null;

	public Changer(){
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		//bg1.add(nrml);
		//bg1.add(asct);
		jte.setText("hello!");
		jte.setLineWrap(true);
		jp1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED) );
		jp1.setLayout(new GridLayout(2,1) );
		jp1.add(jsp1);
		jp3.setBorder(new SoftBevelBorder(BevelBorder.LOWERED) );
		jp3.setLayout( new FlowLayout() );
		jp1.add(jp3);
		add(jp1);
		chbutton.addActionListener(new ActionListener(){
			class WmButton extends JButton{
				WmWrapper w;
				WmButton(WmWrapper w){
					this.w = w;
					setText( w+"" );
				}
				void update(){
					super.setText( w.update() );
				}
			};
			public void actionPerformed(ActionEvent e) {
				chbutton.setEnabled(false);
				SwingUtilities.invokeLater(new Runnable(){
					public void run(){
						/*if( !jbset.isEmpty() )
							for(JButton jbt : jbset)
								jp3.remove(jbt);*/
						tagged = SentenceManager.tag( jte.getText() );
						//String searchArg =  nrml.isSelected()? "syns" : "associate";
						String searchArg = "syns";
						ws = new SentenceManager(tagged, searchArg ).wrap();
						if( System.getProperty("os.name").equals("Mac OS X") ){
							ButtonGroup bg2 = new ButtonGroup();
							bg2.add(speak);
							bg2.add(sing);
							JButton vocalize = new JButton("vocalize");
							vocalize.addActionListener(new ActionListener(){
							
								public void actionPerformed(ActionEvent arg0) {
									new Thread(
											new Runnable(){
												public void run(){
												if ( sing.isSelected() )
													ws.sing();
												else if( speak.isSelected() )
													ws.speak();
												}
											}).start();
								}
							
							});
							jp2.add(vocalize);
							jp2.add(speak);
							jp2.add(sing);
						}
						WmButton b;
						for(WmWrapper w : ws ){
							jp3.add(b = new WmButton(w) );
							jbset.add(b);
							b.setBorderPainted(false);
							b.addActionListener(new ActionListener(){

								public void actionPerformed(ActionEvent e) {
									( (WmButton)e.getSource() ).update();
									
								}
								
							});
							
						}
					}
				});
				//chbutton.setEnabled(true);	
			}
			
		});
		jp2.add(chbutton);
		//jp2.add(nrml);
		//jp2.add(asct);
		add(BorderLayout.NORTH, jp2);
		setBackground(Color.WHITE);
	}

	public static void main(String[] args){
		SwingConsole.run((new Changer()), WIDTH, 400);
		
	}
	}
