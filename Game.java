import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.imageio.ImageIO; 
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game implements ActionListener {

    private static final String MAIN = "Home";
    private static final String HELP = "Help Panel";
    private static final String PHOTOS = "Photos Panel";
    private JPanel cardHolder;
    private JButton helpButton = new JButton(new ImageIcon("icon/help48.png"));
    private JButton homeButton = new JButton("Home");
    private JButton photosButton = new  JButton(new ImageIcon("icon/album48.png"));
    private JTextField textField = new JTextField("Enter command...", 40); 
    private static final int width = 1000;
    private static final int height = 1500;
    private static Rooms r = new Rooms();
    private ArrayList<Rooms> allRooms = r.getRoomsList();
    private Rooms currentRoom = r.getRoom("Welcome!", allRooms);;
    private Adventure mainPanel = new Adventure("Welcome!", allRooms);
    private PhotosPanel photosPanel = new PhotosPanel();
    private ArrayList<String> myObjects = new ArrayList<String>();
    private boolean firstTime = true;
    private boolean enterCommand = true;

    public JPanel createCardHolderPanel() {
        cardHolder = new JPanel(new CardLayout());
        cardHolder.add(mainPanel, MAIN);
        cardHolder.add(new HelpPanel(), HELP);
        cardHolder.add(photosPanel, PHOTOS);

        return cardHolder;
    }

    public JPanel createButtonPanel() {
    	JPanel panel = new JPanel(new GridLayout(2,1,10,10));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 48,48));
        JPanel textPanel = new JPanel(new GridLayout(1,1,48,48));
        
        textField.setHorizontalAlignment(JTextField.CENTER);
        textField.setFont(new Font("SansSerif", Font.PLAIN, 30));
       	textPanel.add(textField);
       	textField.addKeyListener(new KeyListener() {
       		@Override
       		public void keyTyped(KeyEvent e) {
       		}

       		@Override
       		public void keyReleased(KeyEvent e) {
       		}

       		@Override
       		public void keyPressed(KeyEvent e){
       			if (firstTime) {
       				textField.setText("");
       				firstTime = false;
       			}
       		}
       	});
       	textField.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
            	if (firstTime) {
            		textField.setText("");
            		firstTime = false;
            	}
            }
        });
        textField.addActionListener(this);

        buttonPanel.add(helpButton);
        buttonPanel.add(homeButton);
        buttonPanel.add(photosButton);

        homeButton.addActionListener(this);
        helpButton.addActionListener(this);
        photosButton.addActionListener(this);

        panel.add(textPanel);
        panel.add(buttonPanel);
        return panel;
    }

    public JPanel createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(width, height));
        panel.add(createButtonPanel(), BorderLayout.SOUTH);
        panel.add(createCardHolderPanel(), BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CardLayout cardLayout = (CardLayout) (cardHolder.getLayout());
        if (e.getSource() == homeButton) {
            cardLayout.show(cardHolder, MAIN);
            enterCommand = true;
            firstTime = true;
            textField.setText("Enter command...");
        }
        if (e.getSource() == helpButton) {
            cardLayout.show(cardHolder, HELP);
            firstTime = false;
            enterCommand = false;
        }
        if (e.getSource() == photosButton) {
            cardLayout.show(cardHolder, PHOTOS);
            firstTime = false;
            enterCommand = false;
        }

        if (!enterCommand) {
        	textField.setText("Please go back to home before entering a command");
        }

        if (enterCommand) {
	        if (e.getSource() == textField) {
	        	String command = r.isValidCommand(textField.getText(), currentRoom.getName(),allRooms);
	        	if (command.equals("invalid")) {
	        		System.out.println(command + " is not an exit or an object.");
	        		System.out.println("These are the possible objects to 'pick up':");
	        		for (int i = 0; i < currentRoom.getObjects().size(); i++) System.out.println(currentRoom.getObjects().get(i));
	        		System.out.println("These are the possible exits to 'go to':");
	        		for (int i = 0; i < currentRoom.getExits().size(); i++) System.out.println(currentRoom.getExits().get(i));
	        		textField.setText("Invalid command! Try again");
	        		firstTime = true;
	        	}

	        	else if (command.substring(0, 6).equals("exit: ")) {
	        		command = command.substring(6);
	        		System.out.println("Command: " + command);
	        		textField.setText("Enter command...");
	        		firstTime = true;
	        		currentRoom = r.getRoom(r.getNextRoom(command, allRooms), allRooms);
	        		mainPanel.repaintComponent(currentRoom.getName(), allRooms);
	        	}

	        	else if (command.length() > 8 && command.substring(0,8).equals("object: ")) {
	        		command = command.substring(8);
	        		System.out.println("Command: " + command);
	        		textField.setText("You picked up " + command + "! Enter command...");
	        		firstTime = true;
	        		allRooms = r.pickUp(command, currentRoom, allRooms);
	        		myObjects.add(command);
	        		photosPanel.repaintComponent(myObjects);
	        		mainPanel.repaintComponent(currentRoom.getName(), allRooms);
	        	}
	        }
        }
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Text Adventure Game");
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game main = new Game();
        frame.add(main.createContentPane());
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
    	// Rooms.testRoomsList();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

class Adventure extends JPanel {

 	private static int width = 1000;
 	private static int height = 1500;
 	private static int padding = 25;
 	private static int rectWidth = width - 2*padding;
 	private static int rectHeight = height - 2*padding - 48 - padding;
 	private Rooms currentRoom = new Rooms();

 	public Adventure(String rName, ArrayList<Rooms> rList) {
 		currentRoom = Rooms.getRoom(rName, rList);
 	}

 	public void repaintComponent(String rName, ArrayList<Rooms> rList) {
 		currentRoom = Rooms.getRoom(rName, rList);
 		repaint();
 	}


 	/* Drop shadow code inspired by http://www.javafaq.nu/java-example-code-681.html */
 	private void dropShadow(String text, FontMetrics fm, Graphics g, int cx, int cy) {
 		int tracking = 0;
 		// left shadow
 		int left_x = 0; int left_y = 0;
 		Color left_color = Color.BLACK;
 		// right shadow
 		int right_x = 2; int right_y = 3;
 		Color right_color = Color.BLACK;

 		char[] chars = text.toCharArray();

        for(int i=0; i<chars.length; i++) {
            char ch = chars[i];
            int w = fm.charWidth(ch) + tracking;

            g.setColor(left_color);
            g.drawString(""+chars[i],cx-left_x,cy-left_y);

            g.setColor(right_color);
            g.drawString(""+chars[i],cx+right_x,cy+right_y);

            g.setColor(Color.WHITE);
            g.drawString(""+chars[i],cx,cy);

            cx+=w;
        }
 	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
 	    BufferedImage img = null;
 	    try {
 	    	img = ImageIO.read(new File("background_img/" + currentRoom.getName() + ".jpg"));
 	    }
 	    catch (IOException e) {
 	    	System.out.println("Background image not found");
 	    }
 	    g.drawRect(0,0,width,height);
 	    g.setColor(Color.WHITE);
 	    g.fillRect(0,0,width,height);
 	    // draw text background
	    g.setColor(new Color(179, 217, 255));
	    g.fillRoundRect(padding, padding, rectWidth, rectHeight, 25, 25);
	    g.drawImage(img,padding,padding,null);
 	    // set font
 	    Font fNormal = new Font("SansSerif", Font.BOLD, 30);
 	    Font fBoldItalic = new Font("SansSerif", Font.BOLD + Font.ITALIC, 30);
 	    FontMetrics fNormalM = g.getFontMetrics(fNormal);
 	    FontMetrics fBoldItalicM = g.getFontMetrics(fBoldItalic);

 	    int cx = padding*2; int cy = padding*4;
 	    g.setColor(Color.BLACK);
 	    g.setFont(fBoldItalic);
 	    System.out.println(currentRoom.getName());
 	    g.drawString(currentRoom.getName(), cx, cy);

 	    g.setFont(fNormal);
 	    splitIntoLines(currentRoom.getDesc(), currentRoom.getObjects(), currentRoom.getExits(), g, fNormalM);

 	    g.dispose();
	}

	private void splitIntoLines(String desc, ArrayList<String> objs, ArrayList<String> exits, Graphics g, FontMetrics fm) {
		String[] dList = desc.split(" ");
		int textWidth = width - 2*padding;
		int cx = padding*2; int cy = padding*4 + 40;
		for (int i = 0; i < dList.length; i++) {
			if (fm.stringWidth(dList[i] + " ") + cx > textWidth) {
				cx = padding*2; cy+=40;
			}
			dropShadow(dList[i] + " ", fm, g, cx, cy);
			cx += fm.stringWidth(dList[i] + " ");
		}
		
		if (objs.size() > 0) {
			cx = padding*2; cy+=80;
			dropShadow("These are the objects you can pick up: ", fm, g, cx, cy);
			cx += fm.stringWidth("These are the objects you can pick up: ");
			for (int i = 0; i < objs.size(); i++) {
				if (fm.stringWidth(objs.get(i) + " ") + cx > textWidth) {
					cx = padding*2; cy+=40;
				}
				if (i == objs.size() - 1) {
					dropShadow(objs.get(i), fm, g, cx, cy);
					cx += fm.stringWidth(objs.get(i));
				}
				else {
					dropShadow(objs.get(i) + ", ", fm, g, cx, cy);
					cx += fm.stringWidth(objs.get(i) + ", ");
				}
				
			}
		}
		
		if (exits.size() > 0) {
			cx = padding*2; cy+=80;
			dropShadow("Where to next? ", fm, g, cx, cy);
			cx += fm.stringWidth("Where to next? ");
			for (int i = 0; i < exits.size(); i++) {
				if (fm.stringWidth(exits.get(i) + " ") + cx > textWidth) {
					cx = padding*2; cy+=40;
				}
				if (i == exits.size() - 1) {
					dropShadow(exits.get(i), fm, g, cx, cy);
					cx += fm.stringWidth(exits.get(i));
				}
				else {
					dropShadow(exits.get(i) + ", ", fm, g, cx, cy);
					cx += fm.stringWidth(exits.get(i) + ", ");
				}
			}
		}
	}
}

class HelpPanel extends JPanel {
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1000, 1500);
		Font f = new Font("SansSerif", Font.PLAIN, 30);
		FontMetrics fm = g.getFontMetrics(f);
 	    int cx = 25*2; int cy = 25*4;
 	    // to change cx, cx += fm.stringWidth(String s);
 	    g.setColor(Color.BLACK);
 	    g.setFont(f);
 	    g.drawString("Instructions:", cx, cy);
 	    String instructions = "Type in the textbox below to enter commands. ENTER ENTER If you want to leave a room, type 'go to roomname', where roomname is the name of the room you want to go to. ENTER ENTER If you want to pick up an object, type 'pick up objectname', where objectname is the name of the object you want to pick up. ENTER You can view all objects, AKA photographs, you've picked up by clicking the album button on the bottom. ENTER ENTER Have fun!";
 	    splitIntoLines(instructions, g, fm);
 	    g.dispose();
	}

	private void splitIntoLines(String s, Graphics g, FontMetrics fm) {
		String[] sList = s.split(" ");
		int textWidth = 1000 - 2*25;
		int cx = 25*2; int cy = 25*4 + 40;
		for (int i = 0; i < sList.length; i++) {
			if (fm.stringWidth(sList[i] + " ") + cx <= textWidth && !sList[i].equals("ENTER")) { // fits in line!
				g.drawString(sList[i] + " ", cx, cy);
				cx += fm.stringWidth(sList[i] + " ");
			}
			else { // start new line
				cx = 25*2; cy+= 40;
				if (!sList[i].equals("ENTER")) {
					g.drawString(sList[i] + " ", cx, cy);
					cx += fm.stringWidth(sList[i] + " ");
				}
			}
		}
	}
}

class PhotosPanel extends JPanel {
	private ArrayList<String> fileNames = new ArrayList<String>();
	private boolean isEnlarged = false;

	public void repaintComponent(ArrayList<String> objects) {
 		fileNames = objects;
 		repaint();
 	}

	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, 1000, 1500);
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1000, 1500);
		Font f = new Font("SansSerif", Font.PLAIN, 30);
		FontMetrics fm = g.getFontMetrics(f);
 	    int cx = 25*2; int cy = 25*4;
 	    // to change cx, cx += fm.stringWidth(String s);
 	    g.setColor(Color.BLACK);
 	    g.setFont(f);
 	    g.drawString("Welcome to your photo gallery!", cx, cy);
 	    cy+=40;
 	    g.drawString("You can view all pictures you've picked up here.", cx,cy);  

 	    cx = 50; cy = 180;
 	    for (int i = 0; i < fileNames.size(); i++) {
 	    	BufferedImage img = null;
 	    	try {
 	    		img = ImageIO.read(new File("img/" + fileNames.get(i) + ".jpg"));
 	    	}
 	    	catch (IOException e0) {
 	    		System.out.println(fileNames.get(i) + " image not found");
 	    		try {
 	    			img = ImageIO.read(new File("img/no-image-available.jpg"));
 	    		}
 	    		catch (IOException e1) {
 	    			System.out.println("No image found");
 	    		}
 	    	}
 			g.drawImage(img, cx, cy, 283, 283, null);

 			// final int x = cx; final int y = cy; final BufferedImage thisImage = img; final String currentName = fileNames.get(i);
 			// addMouseListener(new MouseAdapter() {
    //             @Override
    //             public void mouseClicked(MouseEvent e) {
    //                 if (thisImage != null) {
    //                     Point me = e.getPoint();
    //                     Rectangle bounds = new Rectangle(x, y, 283, 283);
    //                     if (bounds.contains(me)) {
    //                         System.out.println("in bounds of " + currentName);
    //                         isEnlarged = true;
    //                         g.clearRect(0, 0, 1000, 1500);
    //                         repaint();
    //                     }
    //                     else {
				// 			isEnlarged = false;
				// 			g.clearRect(0, 0, 1000, 1500);
				// 			repaint();
    //                     }
    //                 }
    //             }

    //         });

    //         enlargeImage(img, g, fileNames.get(i), cx, cy);

 			cx+= 308;
 			if (cx == 974) {
 				cx = 50; cy+= 308;
 			}
 		}
 		g.dispose();
	}

	// private void enlargeImage(BufferedImage img, Graphics g, String desc, int cx, int cy) {
	// 	// http://letmecodejava.blogspot.com/2014/06/java-swing-enlarge-image.html
	// 	if (isEnlarged) {
	// 		g.setColor(new Color(0, 0, 0, 200));
	// 		g.fillRect(0, 0, 1000, 1500);
	// 		g.drawImage(img, 50, 180, 900, 900, null);
	// 		Font f = new Font("SansSerif", Font.ITALIC, 40);
	// 		g.setColor(Color.WHITE);
 // 	    	g.setFont(f);
	// 		g.drawString(desc, 50, 1170);
	// 	}
	// 	else {
	// 		g.drawImage(img, cx, cy, 283, 283, null);
	// 	}
	// }
}

class Rooms {
	private String name;
	private String desc;
	private ArrayList<String> objects = new ArrayList<String>();
	private ArrayList<String> exits =new ArrayList<String>();
	private ArrayList<Rooms> roomsList = new ArrayList<Rooms>();
	private int id;

	/* 
	All Exits:
	Wally goes to: Witches Brew, Pizzaiola, 24 Hour Bagel, Movie Theater
		Cassidy's house
		Kate's house
	Daddy's BMW goes to: Long Beach, Dunkin Donuts, Taco Bell, Oceanside High School
		Ariel's house
		Ali's house
	Nancy's car goes to: Artrageous, Trader Joe's, Masago, Southold, Boston
		Nancy's house

	All Objects:
		x Witches Brew Selfie
		x Tent
		x Flower crown
		- Chipotle tacos
		x My House vinyl
		- Sprinkle munchkins
		- Chicken roll
		x Rock Band game
		- Caramel Swirl Iced Latte
		- Bagel
		x Blue
		- Jude
		- Charlie
		- Chipotle Chicken Loaded Griller
		- Blue Ranch Doritos Locos Taco
		x Long Beach selfie
		x Graduation photo
		- Painted pottery
	*/

	private void createRoomsList() {
		String n0 = "Welcome!";
		String d0 = "Welcome to The Clique\u2122 game. The directions are simple: traverse rooms by typing in the text box below \"go to Room\" (where Room is the name of the room). And, pick up objects by typing \"pick up Object\" (where Object is the name of the object). After you pick up an object, you can click the photo gallery button below to view all of your pictures. If you forget any of these directions at any time, just click the help button below! Are you ready for a corny adventure with your fave five ladies? If so, type 'go to Witches Brew' to begin (because obviously we have to start there)! And, if you're not ready, then just leave. We don't want you here.";
		ArrayList<String> rObjects0 = new ArrayList<String>();
		ArrayList<String> rExits0 = new ArrayList<String>();
		rExits0.add("Witches Brew");
		Rooms r0 = new Rooms(n0, d0, rObjects0, rExits0, 0);

		String n1 = "Witches Brew";
		String d1 = "You are impatiently waiting for all of the clique to show up. While you wait you decide to take more selfies. You also find other selfies from past Witch's Brew dates. Finally, the whole Clique arrives and you order an overpriced assortment of coffees, teas, cheesecakes, and nachos.";
		ArrayList<String> rObjects1 = new ArrayList<String>();
		ArrayList<String> rExits1 = new ArrayList<String>();
		rObjects1.add("Witches Brew Selfie");
		rExits1.add("Wally");
		Rooms r1 = new Rooms(n1, d1, rObjects1, rExits1, 1);

		String n2 = "Nancy's car";
		String d2 = "As you get into Nancy's car, you see some flowers and chocolate covered pretzels from Trader Joe's. Nancy is nicer than Cassidy and allows anyone to DJ, but will have to insist that Kate sits shotgun because she's the only one capable of navigating.";
		ArrayList<String> rObjects2 = new ArrayList<String>();
		ArrayList<String> rExits2 = new ArrayList<String>();
		rExits2.add("Cassidy's House");
		rExits2.add("Ali's House");
		rExits2.add("Nancy's House");
		rExits2.add("Ariel's House");
		rExits2.add("Kate's House");
		rExits2.add("Artrageous");
		rExits2.add("Trader Joe's");
		rExits2.add("Masago");
		rExits2.add("Southold");
		rExits2.add("Boston campsite");
		Rooms r2 = new Rooms(n2, d2, rObjects2, rExits2, 2);

		String n3 = "Boston campsite";
		String d3 = "You can't tell what's funnier--watching everyone try to put a tent up, watching everyone cook their food over a campfire, or watching everyone have the strangest cuddle sesh inside a tent. Ariel just took a glorious selfie of everyone being outdoorsy. You all are just SO HYPED for BosCalls!";
		ArrayList<String> rObjects3 = new ArrayList<String>();
		ArrayList<String> rExits3 = new ArrayList<String>();
		rObjects3.add("Tent");
		rExits3.add("Nancy's car");
		rExits3.add("Boston Calling");
		Rooms r3 = new Rooms(n3, d3, rObjects3, rExits3, 3);

		String n4 = "Boston Calling";
		String d4 = "After an enjoyable road trip where Ali's feet may or may not have ended up outside the sunroof, the Clique has arrived in Boston. Over the next few days, Ali and Cassidy cry over Brand New, you meet some cool people (Hot Andy, Vaguely Kevin), Ariel jams to Modest Mouse and Bastille, and Kate and Nancy have romantic Chipotle dinners. You also find this bomb-ass flower crown in Panera, so take some selfies with it.";
		ArrayList<String> rObjects4 = new ArrayList<String>();
		ArrayList<String> rExits4 = new ArrayList<String>();
		rObjects4.add("Flower crown");
		rObjects4.add("Chipotle tacos");
		rExits4.add("Nancy's car");
		rExits4.add("Boston campsite");
		Rooms r4 = new Rooms(n4, d4, rObjects4, rExits4, 4);

		String n5 = "Cassidy's House";
		String d5 = "The Clique has gathered at Cassidy's house. Cassidy has a limited edition 7\" vinyl of My House by Flo Rida playing on her record player. There is also a box of 50 sprinkle munchkins on the table. Honestly, tg for dd.";
		ArrayList<String> rObjects5 = new ArrayList<String>();
		ArrayList<String> rExits5 = new ArrayList<String>();
		rObjects5.add("My House vinyl");
		rObjects5.add("Sprinkle munchkins");
		rExits5.add("Wally");
		Rooms r5 = new Rooms(n5, d5, rObjects5, rExits5, 5);

		String n6 = "Wally";
		String d6 = "Cassidy is blasting Alt Nation, as she normally does. This bitch doesn't listen to anything else. And no, she will not let you DJ. She doesn't care what you have to say. This is Cassidy's car and she's in charge, if you don't like it go on somebody else's scavenger hunt.";
		ArrayList<String> rObjects6 = new ArrayList<String>();
		ArrayList<String> rExits6 = new ArrayList<String>();
		rExits6.add("Cassidy's House");
		rExits6.add("Witches Brew");
		rExits6.add("Movie Theater");
		rExits6.add("Ali's House");
		rExits6.add("Nancy's House");
		rExits6.add("Ariel's House");
		rExits6.add("Kate's House");
		rExits6.add("Pizzaiola");
		rExits6.add("24 Hour Bagel");
		Rooms r6 = new Rooms(n6, d6, rObjects6, rExits6, 6);

		String n7 = "Daddy's BMW";
		String d7 = "This is the car Ariel gets to drive sometimes. It's her dad's, which is why it's lovingly called \"Daddy's BMW\" (kinky). Ariel really only likes to go to Dunkin Donuts and Long Beach. Good luck getting her to take you somewhere else. Maybe if you buy her Dunkin, maybe she'll take you to other places. So where is she dragging the Clique today?";
		ArrayList<String> rObjects7 = new ArrayList<String>();
		ArrayList<String> rExits7 = new ArrayList<String>();
		rExits7.add("Long Beach");
		rExits7.add("Dunkin Donuts");
		rExits7.add("Taco Bell");
		rExits7.add("Ariel's House");
		rExits7.add("Ali's House");
		rExits7.add("Nancy's House");
		rExits7.add("Kate's House");
		rExits7.add("Cassidy's House");
		Rooms r7 = new Rooms(n7, d7, rObjects7, rExits7, 7);

		String n8 = "Pizzaiola";
		String d8 = "Cassidy decides she is craving a chicken roll something fierce. She drags the rest of the clique to Pizzaiola with her. They all decide to get chicken rolls (except that bitch Kate. She gets a plain slice).";
		ArrayList<String> rObjects8 = new ArrayList<String>();
		ArrayList<String> rExits8 = new ArrayList<String>();
		rObjects8.add("Chicken Roll");
		rExits8.add("Wally");
		Rooms r8 = new Rooms(n8, d8, rObjects8, rExits8, 8);

		String n9 = "Ariel's House";
		String d9 = "We don't come here that often, so you can tell Ariel is uncomfortable by having all these people in her house because she just doesn't know what to do with them. We all decide to do the thing we always do when we're at Ariel's house--play Beatles Rock Band and come up with Marauders headcanons.";
		ArrayList<String> rObjects9 = new ArrayList<String>();
		ArrayList<String> rExits9 = new ArrayList<String>();
		rObjects9.add("Rock Band game");
		rExits9.add("Daddy's BMW");
		Rooms r9 = new Rooms(n9, d9, rObjects9, rExits9, 9);

		String n10 = "Kate's House";
		String d10 = "Uh oh! We don't have a description for this yet. Go back to the beginning by typing 'go to Welcome!'";
		ArrayList<String> rObjects10 = new ArrayList<String>();
		ArrayList<String> rExits10 = new ArrayList<String>();
		rExits10.add("Wally");
		Rooms r10 = new Rooms(n10, d10, rObjects10, rExits10, 10);

		String n11 = "Ali's House";
		String d11 = "Damn, there are a lot of animals here. You decide to play with them and ignore the rest of the clique. Kate has to stay outside though, she's allergic to the cat. The rest of the Clique isn't too upset about it really because the animals are much more important.";
		ArrayList<String> rObjects11 = new ArrayList<String>();
		ArrayList<String> rExits11 = new ArrayList<String>();
		rObjects11.add("Blue");
		rObjects11.add("Jude");
		rObjects11.add("Charlie");
		rExits11.add("Daddy's BMW");
		Rooms r11 = new Rooms(n11, d11, rObjects11, rExits11, 11);

		String n12 = "Nancy's House";
		String d12 = "Uh oh! We don't have a description for this yet. Go back to the beginning by typing 'go to Welcome!'";
		ArrayList<String> rObjects12 = new ArrayList<String>();
		ArrayList<String> rExits12 = new ArrayList<String>();
		rExits12.add("Nancy's car");
		Rooms r12 = new Rooms(n12, d12, rObjects12, rExits12, 12);

		String n13 = "Dunkin Donuts";
		String d13 = "So the only reason we really came here was because Ariel requested it (demanded it) but tbh we're all chill with it. Ariel gets her usual medium caramel swirl iced latte with skim milk and her blueberry cake muffin and ends up paying no money for it. Like, how???? Anyways, you all get Dunkin and decide it's time to hit the road.";
		ArrayList<String> rObjects13 = new ArrayList<String>();
		ArrayList<String> rExits13 = new ArrayList<String>();
		rObjects13.add("Caramel Swirl Iced Latte");
		rExits13.add("Daddy's BMW");
		Rooms r13 = new Rooms(n13, d13, rObjects13, rExits13, 13);

		String n14 = "Movie Theater";
		String d14 = "Uh oh! We don't have a description for this yet. Go back to the beginning by typing 'go to Welcome!'";
		ArrayList<String> rObjects14 = new ArrayList<String>();
		ArrayList<String> rExits14 = new ArrayList<String>();
		rExits14.add("Wally");
		Rooms r14 = new Rooms(n14, d14, rObjects14, rExits14, 14);

		String n15 = "Taco Bell";
		String d15 = "Okay, if we're being honest this is for Ali. She's always craving Taco Bell much like how Cassidy is always craving a chicken roll. Ariel decides to take Ali here. Cassidy has a sudden urge for a Blue Ranch Doritos Locos Taco.";
		ArrayList<String> rObjects15 = new ArrayList<String>();
		ArrayList<String> rExits15 = new ArrayList<String>();
		rObjects15.add("Chipotle Chicken Loaded Griller");
		rObjects15.add("Blue Ranch Doritos Locos Taco");
		rExits15.add("Daddy's BMW");
		Rooms r15 = new Rooms(n15, d15, rObjects15, rExits15, 15);

		String n16 = "Artrageous";
		String d16 = "The last time the whole Clique was here was for Nancy's birthday that one summer, and since then, we've said we'd come back, but we haven't...until now!";
		ArrayList<String> rObjects16 = new ArrayList<String>();
		ArrayList<String> rExits16 = new ArrayList<String>();
		rObjects16.add("Painted pottery");
		rExits16.add("Nancy's car");
		Rooms r16 = new Rooms(n16, d16, rObjects16, rExits16, 16);

		String n17 = "Trader Joe's";
		String d17 = "Ah, the holy land. W";
		ArrayList<String> rObjects17 = new ArrayList<String>();
		ArrayList<String> rExits17 = new ArrayList<String>();
		rExits17.add("Nancy's car");
		Rooms r17 = new Rooms(n17, d17, rObjects17, rExits17,17);

		String n18 = "Masago";
		String d18 = "Uh oh! We don't have a description for this yet. Go back to the beginning by typing 'go to Welcome!'";
		ArrayList<String> rObjects18 = new ArrayList<String>();
		ArrayList<String> rExits18 = new ArrayList<String>();
		rExits18.add("Nancy's car");
		Rooms r18 = new Rooms(n18, d18, rObjects18, rExits18,18);

		String n19 = "Southold";
		String d19 = "Uh oh! We don't have a description for this yet. Go back to the beginning by typing 'go to Welcome!'";
		ArrayList<String> rObjects19 = new ArrayList<String>();
		ArrayList<String> rExits19 = new ArrayList<String>();
		rExits19.add("Nancy's car");
		Rooms r19 = new Rooms(n19, d19, rObjects19, rExits19, 19);

		String n20 = "24 Hour Bagel";
		String d20 = "Wow honestly tg for Long Island ba(e)gels. 24 has the usual scent of fresh bagels and iced coffee. Long Island may suck sometimes, but its bagels are bae.";
		ArrayList<String> rObjects20 = new ArrayList<String>();
		ArrayList<String> rExits20 = new ArrayList<String>();
		rObjects20.add("Bagel");
		rExits20.add("Wally");
		Rooms r20 = new Rooms(n20, d20, rObjects20, rExits20, 20);

		String n21 = "Oceanside High School";
		String d21 = "Okay, no. Who decided to come back to OHS? Just, why? You all collectively decide this was an awful idea and get back into Ariel's car. As you're doing so, you find a graduation picture of the Clique from that sunny Friday afternoon all those years ago. Wow, amazing how it survived for nearly three years!";

		ArrayList<String> rObjects21 = new ArrayList<String>();
		ArrayList<String> rExits21 = new ArrayList<String>();
		rObjects21.add("Graduation photo");
		rExits21.add("Daddy's BMW");
		Rooms r21 = new Rooms(n21, d21, rObjects21, rExits21, 21);

		String n22 = "Long Beach";
		String d22 = "There's nothing like the ocean breeze and the smell of salt water. The young hooligans are hiding under the boardwalk drinking out of red solo cups, while all the mature bitches (us) walk on the boardwalk and take dumb selfies and catch up.";
		ArrayList<String> rObjects22 = new ArrayList<String>();
		ArrayList<String> rExits22 = new ArrayList<String>();
		rObjects22.add("Long Beach selfie");
		rExits22.add("Daddy's BMW");
		Rooms r22 = new Rooms(n22, d22, rObjects22, rExits22, 22);

		roomsList.add(r0);
		roomsList.add(r1);
		roomsList.add(r2);
		roomsList.add(r3);
		roomsList.add(r4);
		roomsList.add(r5);
		roomsList.add(r6);
		roomsList.add(r7);
		roomsList.add(r8);
		roomsList.add(r9);
		roomsList.add(r10);
		roomsList.add(r11);
		roomsList.add(r12);
		roomsList.add(r13);
		roomsList.add(r14);
		roomsList.add(r15);
		roomsList.add(r16);
		roomsList.add(r17);
		roomsList.add(r18);
		roomsList.add(r19);
		roomsList.add(r20);
		roomsList.add(r21);
		roomsList.add(r22);
	}

	public static void testRoomsList(ArrayList<Rooms> rList) {
		for (Rooms obj : rList) {
			System.out.println(obj.getName());
			System.out.println(obj.getDesc());
			System.out.println(obj.getObjects());
			System.out.println(obj.getExits() + "\n");
		}
	}

	public Rooms() {

	}

	public Rooms(String n, String d, ArrayList<String> o, ArrayList<String> e, int i) {
		name = n;
		desc = d;
		objects = o;
		exits = e;
		id = i;
	}

	private void setName(String n) {
		name = n;
	}

	private void setDesc(String d) {
		desc = d;
	}

	private void setObjects(ArrayList<String> objs) {
		objects = objs;
	}

	private void setExits(ArrayList<String> e) {
		exits = e;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	public ArrayList<String> getObjects() {
		return objects;
	}

	public ArrayList<String> getExits() {
		return exits;
	}

	private String isValidObject(String o, String r, ArrayList<Rooms> rList) {
		if (o.length() <= 8) return "";
		else if (o.substring(0,8).toLowerCase().equals("pick up ")) {
			for (String str : getRoom(r, rList).objects) {
				if (str.toLowerCase().equals(o.substring(8).toLowerCase())) {
					return str;
				}
			}
		}
		return "";
	}

	private String isValidExit(String e, String r, ArrayList<Rooms> rList) {
		if (e.length() <= 6) return "";
		else if (e.substring(0, 6).toLowerCase().equals("go to ")) {
			for (String str : getRoom(r, rList).exits) {
				if (str.toLowerCase().equals(e.substring(6).toLowerCase())) {
					return str;
				}
			}
		}
		return "";
	}

	public String isValidCommand(String c, String r, ArrayList<Rooms> rList) {
		if (!isValidExit(c, r, rList).equals("")) return "exit: " + isValidExit(c, r, rList);
		else if (!isValidObject(c, r, rList).equals("")) return "object: " + isValidObject(c, r, rList);
		else return "invalid";
	}

	public String getNextRoom(String command, ArrayList<Rooms> rList) {
		return getRoom(command, rList).getName();
	}

	public ArrayList<Rooms> pickUp(String command, Rooms r, ArrayList<Rooms> rList) {
		rList.get(r.id).objects.remove(command);
		return rList;
	}

	public static ArrayList<Rooms> getRoomsList() {
		Rooms r = new Rooms();
		r.createRoomsList();
		return r.roomsList;
	}		

	public static Rooms getRoom(String n, ArrayList<Rooms> rList) {
		for (Rooms obj : rList) {
			if (obj.getName().equals(n)) {
				return obj;
			}
		}
		return new Rooms();
	}
}