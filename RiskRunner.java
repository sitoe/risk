// #ClassyCodersComment ;)

// IMPORTS (OOOH SO MANY)
// LABELS
import java.awt.Color;
import java.awt.Font;
// USER INPUT
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
//DATA STRUCTURES
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// USER INTERACTION
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

// YAY PACKAGES
import acm.graphics.*;
import acm.program.*;

/*NOTES__________________________________
 |Army number color coding				|
 |Color coded pixel map				    |
 |Army movement					        |
 |Multiplayer (2 humans, 1 AI)			|
 |I am  really not sure why this exists	|
 ----------------------------------------*/
//Robin's code that other people understand sometimes when they try
@SuppressWarnings("serial")
public class RiskRunner extends GraphicsProgram {
	public static void main(String[] args) {
		new RiskRunner().start(args); // highly exciting
	}

	// MAP SELECTION
	public static final String FILE_NAME = "RiskMap.png";
	public static final int APPLICATION_WIDTH = 1298;
	public static final int APPLICATION_HEIGHT = 700;
	// BUTTON INSTANTIATION(These are actually totally useless just FYI, didn't need them, but pretty :P )
	private static final String RED_BUTTON_NAME = "TEAM RED";
	private static final String GREEN_BUTTON_NAME = "TEAM GREEN";
	private static final String YELLOW_BUTTON_NAME = "TEAM YELLOW";
	// (Now these have a point.)
	private static final String ATTACK_BUTTON = "ATTACK!";
	private static final String MOVE_TROOPS = "MOVE TROOPS";
	private static final String NEXT_TURN = "END TURN";
	// COLORS FOR PRETTINESS THAT SOME PEOPLE DON'T UNDERSTAND SORRY
	private static final Color GREEN = new Color(120, 150, 90);
	private static final Color RED = new Color(170, 100, 90);
	private static final Color GREY = new Color(100, 100, 100);
	private static final Color YELLOW = new Color(200, 170, 105);
	private static final Color LGREY = new Color(200, 200, 200); // Yes, I needed 2 colors of grey
	// TEAM INSTANTIATION
	Team teamRed = new Team(RED);
	Team teamGreen = new Team(GREEN);
	Team teamYellow = new Team(YELLOW);
	// COUNTRY INSTANTIATION
	private static Map<String, GOval> touchableAreas = new HashMap<String, GOval>();
	private static Map<String, Country> countries = new HashMap<String, Country>();
	public static Map<String, GLabel> troops = new HashMap<String, GLabel>();
	// CONTINENT INSTANTIATION
	private static final ArrayList<String> asia = new ArrayList<String>();
	private static final ArrayList<String> europe = new ArrayList<String>();
	private static final ArrayList<String> northAm = new ArrayList<String>();
	private static final ArrayList<String> africa = new ArrayList<String>();
	private static final ArrayList<String> southAm = new ArrayList<String>();
	private static final ArrayList<String> australia = new ArrayList<String>();
	// CONTINENT BONUSES
	private static final int ASIA_NUM = 7;
	private static final int EUROPE_NUM = 5;
	private static final int NORTH_AMERICA_NUM = 5;
	private static final int AFRICA_NUM = 3;
	private static final int SOUTH_AMERICA_NUM = 2;
	private static final int AUSSIE_NUM = 2;
	// RANDOM GENERATION (It was useful on oh so many occasions)
	public Random random = new Random();
	public int a = random.nextInt(3);

	// Literally, stuff on the screen and built into the program
	@Override
	public void init() {
		// GENERAL SET UP
		setTitle("Let's play Risk!"); // Friendly introduction required before we try to take over the world :)
		addMouseListeners(); // So the mouse works
		// A MAP (It's pretty self-explanatory)
		GImage map = new GImage(FILE_NAME);
		map.scale(.4997); // Weird numbers required
		map.scale(1.07, .95);
		add(map);
		// JButton (graphical setup)
		// RED TEAM
		JButton red = new JButton(RED_BUTTON_NAME);
		red.addActionListener(this);
		red.setBackground(RED);
		add(red, EAST);
		// GREEN TEAM
		JButton green = new JButton(GREEN_BUTTON_NAME);
		green.addActionListener(this);
		green.setBackground(GREEN);
		add(green, EAST);
		// COMPUTER AI
		JButton yellow = new JButton(YELLOW_BUTTON_NAME);
		yellow.addActionListener(this);
		yellow.setBackground(YELLOW);
		add(yellow, EAST);
		// ATTACK BUTTON YAY
		JButton attack = new JButton(ATTACK_BUTTON);
		attack.addActionListener(this);
		attack.setBackground(LGREY);
		add(attack, EAST);
		// MOVE TROOPS BUTTON
		JButton move = new JButton(MOVE_TROOPS);
		move.addActionListener(this);
		move.setBackground(LGREY);
		add(move, EAST);
		// NEXT TURN BUTTON
		JButton next = new JButton(NEXT_TURN);
		next.addActionListener(this);
		next.setBackground(LGREY);
		add(next, EAST);
		// LISTS OF CRAP (If you are really interested, scroll down a looooooonnnng way)
		hidStuff();
		borders();
	
	}

	// Really just the stuff that starts the game
	public void run() {

		// MAP Generation with troops on random locations
		mapGen();
		// More friendliness before you destroy each other
		JOptionPane.showMessageDialog(null, "Welcome to Risk! \nIn this game you will play against 1 other"
				+ " player, as well as the computer. \nEvery player will receive new troops on his or her turn"
				+ " the amounts of which will depend upon the number of holdings of that player. \nYou may place troops"
				+ " in any of your occupied territories and then may choose to attack territories held by other players."
				+ " \nThe object of the game is to win it all. \nGood luck, and take over the world.");
		// LET IT BEGIN!
		turn();
		
	}
	
	// Switches between turns so y'all can't cheat
	public void turn(){
		 // The first turn is randomly generated (a) and from then on it goes in order
		 switch (a) {
         case 0:  teamRed.setTurn(true); teamGreen.setTurn(false); teamYellow.setTurn(false); a++;
         			JOptionPane.showMessageDialog(null, "TEAM RED'S TURN");
         			turnControl();
                  break;
         case 1:   teamGreen.setTurn(true); teamYellow.setTurn(false); teamRed.setTurn(false); a++;
     				JOptionPane.showMessageDialog(null, "TEAM GREEN'S TURN");
     				turnControl();
         	      break;
         case 2:   teamYellow.setTurn(true);teamGreen.setTurn(false); teamRed.setTurn(false); a = 0;
     				JOptionPane.showMessageDialog(null, "TEAM YELLOW'S TURN");
     				turnControl();
     				JOptionPane.showMessageDialog(null,  "Please wait while the computer plays.");
     				// Prompts AI to, you know, play
     				//riskAI.takeTurn(Hash map);
         		  break;
		}
		
	}

	// Decides if your random clicks actually mean anything
	public void mouseClicked(MouseEvent e) {
		// Boils down to: if you click a country, something might happen
		for (String key : touchableAreas.keySet()) {
			if (touchableAreas.get(key).contains(e.getX(), e.getY())) {
				// Checks whose turn it is, and if they have troops left to place
				if (teamRed.isTurn()==true && teamRed.getTroopPlacement() > 0 && countries.get(key).getControl() == teamRed){
					// adds troops to countries you control
					int i = Integer.parseInt(troops.get(countries.get(key).getName()).getLabel());
					i++;
					troops.get(countries.get(key).getName()).setLabel(Integer.toString(i));				
					teamRed.setTroopPlacement(teamRed.getTroopPlacement() - 1);
				}
				
				else if (teamGreen.isTurn() == true && teamGreen.getTroopPlacement() > 0 && countries.get(key).getControl() == teamGreen){
					// Same
					int i = Integer.parseInt(troops.get(countries.get(key).getName()).getLabel());
					i++;
					troops.get(countries.get(key).getName()).setLabel(Integer.toString(i));				
					teamGreen.setTroopPlacement(teamGreen.getTroopPlacement() - 1);
				}
				else if (teamYellow.isTurn() == true && teamYellow.getTroopPlacement() > 0 && countries.get(key).getControl() == teamYellow){
					// Same, except for the AI which does not HAVE to be implemented.
					int i = Integer.parseInt(troops.get(countries.get(key).getName()).getLabel());
					i++;
					troops.get(countries.get(key).getName()).setLabel(Integer.toString(i));				
					teamYellow.setTroopPlacement(teamYellow.getTroopPlacement() - 1);
				}
			}
		}
	}
	
	// Determines how many troops one gets at the beginning of one's turn
	public void turnControl(){
		// Basically, you get 1 troop for every three territories you own plus bonuses for continents
		if(teamGreen.isTurn()== true){
			teamGreen.setTroopPlacement((teamGreen.getHoldings().size() / 3) + continentalControl());
		}
		else if(teamRed.isTurn()== true){
			teamRed.setTroopPlacement((teamRed.getHoldings().size() / 3) + continentalControl());
		}
		else if(teamYellow.isTurn()== true){
			teamYellow.setTroopPlacement((teamYellow.getHoldings().size() / 3) + continentalControl());
		}
	}

	// Didja click a button? Learn what it does HERE!
	public void actionPerformed(ActionEvent e) {
		// Player decides when to end their own turn, SO DO IT WISELY
		if (e.getActionCommand().equals(NEXT_TURN))
			turn();
		// NEW STUFF THAT ROBIN ADDED FOR PLAYING GAME THINGS
		// hehe attacking things is fun
		else if(e.getActionCommand().equals(ATTACK_BUTTON))
		{
			// USER INPUT (all attacks use the max possible troops available)
			String to = JOptionPane.showInputDialog(new JFrame(), "Enter name of country you wish to attack.", null); // EVERYWHERE
			String from = JOptionPane.showInputDialog(new JFrame(), "Enter attacking country name", null);
			// CHECK: both are countries, you own the attacking country
			if(countries.containsKey(to)&& countries.containsKey(from) && countries.get(from).getControl().isTurn()){
				// YES! THE ATTACKING
				attack(countries.get(from), countries.get(to), countries.get(from).getControl(), countries.get(to).getControl());
				win();}
			else
				JOptionPane.showMessageDialog(null, "You may not make that attack"); // Sorry not sorry
		}
		// Just in case you don't like the way the board looks
		else if(e.getActionCommand().equals(MOVE_TROOPS))
		{
			// Self-explanatory
			String from = JOptionPane.showInputDialog(new JFrame(), "Enter name of country you wish to move troops from.", null);
			// CHECK: if country and owned by you and has enough troops to move some
			if(countries.containsKey(from) && countries.get(from).getControl().isTurn() && 
					Integer.parseInt(troops.get(countries.get(from).getName()).getLabel())>1)
			{
				int numTroops = Integer.parseInt(JOptionPane.showInputDialog(new JFrame(), "How many troops do you wish to move?", null));
				// Can only move n-1 troops when the "from" country starts with n troops
				if(numTroops >= Integer.parseInt(troops.get(countries.get(from).getName()).getLabel()))
					JOptionPane.showMessageDialog(null, "Invalid move!"); // Totally your fault that time
				else
				{
					String to = JOptionPane.showInputDialog(new JFrame(), "Move to:", null);
					// Checks if its a country and you own it
					if(countries.containsKey(to) && countries.get(to).getControl().isTurn())
					{
						// Moves troops. You're welcome.
						int i = Integer.parseInt(troops.get(countries.get(from).getName()).getLabel());
						i -= numTroops;
						troops.get(countries.get(from).getName()).setLabel(Integer.toString(i));
						
						int j = Integer.parseInt(troops.get(countries.get(to).getName()).getLabel());
						j += numTroops;
						troops.get(countries.get(to).getName()).setLabel(Integer.toString(j));
					}
					else
						JOptionPane.showMessageDialog(null, "Not your territory"); // yeah bro
				}
			}
			else
				JOptionPane.showMessageDialog(null, "You may not move troops from there."); // gosh, what are you doing with your life.
		}
	}

	// Makes circles appear magically ;)
	public void mouseMoved(MouseEvent e) {
		// yay endless loops because you keep moving the mouse
		for (String key : touchableAreas.keySet()) {
			if ((touchableAreas.get(key).contains(e.getX(), e.getY()))) {
				// So one knows where to click, and to restrict clickable areas visually
				touchableAreas.get(key).setVisible(true);
			}
			if ((!touchableAreas.get(key).contains(e.getX(), e.getY()))) {
				touchableAreas.get(key).setVisible(false);
			}
		}
	}
	
	// ATTACK
	// #ThisIsWhereTheMagicHappens
	// (but no violence)
	public static void attack(Country from, Country to, Team attacker, Team defender) {
		int j; // troops in attacking country
		int k; // troops in defending country
		//checks that the attack itself is possible (the countries border each other and attacker has enough troops)
		if(from.hasBorder(to) && Integer.parseInt(troops.get(from.getName()).getLabel())>1){
			int i = Integer.parseInt(troops.get(from.getName()).getLabel());
			int h = Integer.parseInt(troops.get(to.getName()).getLabel());
			if (i == 2) // can attack with n-1 troops if you have n
				j = 1;
			else if (i == 3)
				j = 2;
			else
				j = 3;
			if (h == 1) // can defend with up to 2 troops
				k = 1;
			else
				k = 2;
			compareRoll(roll("Attacker", j), from, roll("Defender", k), to); // determines victory
			// if attacker wins, some housekeeping is in order
			if (Integer.parseInt(troops.get(to.getName()).getLabel()) == 0) {
				defender.removeFromHoldings(to); // aww you lost :(
				to.setControl(null);
				attacker.addHolding(to); // YAY new stuff
				// changes control of country visually
				troops.get(to.getName()).setColor(from.getControl().getColor()); 
				troops.get(to.getName()).setLabel("" + j);
				troops.get(from.getName()).setLabel("" + (Integer.parseInt(troops.get(from.getName()).getLabel())-j));
			}
		}
		else
			JOptionPane.showMessageDialog(null, "You may not attack that country");
	}

	// DICE SIMULATOR
	public static int[] roll(String role, int num) {
		Random random = new Random();
		int[] roll = new int[num];
		String display = role + " rolls: ";
		for (int i = 0; i < num; i++) {
			roll[i] = random.nextInt(6) + 1;
			display += roll[i] + " ";
		}
		JOptionPane.showMessageDialog(null, display);
		return roll;
	}

	// METHOD FOR COMPARING ROLL
	public static void compareRoll(int[] roll1, Country from, int[] roll2, Country to) {
		int high1 = 0;
		int index1 = 0;
		int high2 = 0;
		int index2 = 0;
		int count = 0;
		while (count < roll1.length && count < roll2.length) {
			for (int i = 0; i < roll1.length; i++)
				if (roll1[i] > high1) {
					high1 = roll1[i];
					index1 = i;
				}
			for (int j = 0; j < roll2.length; j++)
				if (roll2[j] > high2) {
					high2 = roll2[j];
					index2 = j;
				}
			if (high1 > high2) {
				int i = Integer.parseInt(troops.get(to.getName()).getLabel());
				i--;
				troops.get(to.getName()).setLabel(Integer.toString(i));
			} 
			else {
				int i = Integer.parseInt(troops.get(from.getName()).getLabel());
				i--;
				troops.get(from.getName()).setLabel(Integer.toString(i));
			}
			roll1[index1] = 0;
			roll2[index2] = 0;
			count++;
		}
	}

	// Method for labeling all the countries with troop numbers.
	// Theoretical application for all troop movements
	public void addLabel(String name, int x, int y) {
		troops.put(name, new GLabel("0", x, y));
		troops.get(name).setColor(GREY);
		troops.get(name).setFont(new Font("serif", Font.BOLD, 20));
		add(troops.get(name));
	}
	
	public void addCircleArea(String name, int x, int y) {
		// GUI ONLY
		touchableAreas.put(name, new GOval(x, y, 40, 40));
		touchableAreas.get(name).setColor(GREY);
		add(touchableAreas.get(name));
		touchableAreas.get(name).setVisible(false);
		// FOR ACTUAL AREAS
		countries.put(name, new Country(name, x, y, 40, 40));

	}
	//WIN METHOD, TO FINISH
	public void win(){
	if (teamGreen.getHoldings().size() == touchableAreas.size()){
		JOptionPane.showMessageDialog(null, "TEAM GREEN HAS WON");	
	}	
	else if (teamRed.getHoldings().size() == touchableAreas.size()){
		JOptionPane.showMessageDialog(null, "TEAM RED HAS WON");	
	}
	else if (teamYellow.getHoldings().size() == touchableAreas.size()){
		JOptionPane.showMessageDialog(null, "TEAM YELLOW HAS WON");	
		
	}
	}
	
	public void hidStuff() {
		// _______________________________________________________
		// Initial Labels for each country, meaning the numbers
		// All Are in Alphabetical order
		// A__________________________________
		addLabel("Afganistan", 779, 279);
		asia.add("Afganistan");
		addLabel("Alberta", 205, 187);
		northAm.add("Alberta");
		addLabel("Alaska", 110, 101);
		northAm.add("Alaska");
		addLabel("Argentina", 299, 569);
		southAm.add("Argentina");
		// B__________________________________
		addLabel("Brazil", 358, 469);
		southAm.add("Brazil");
		// C__________________________________
		addLabel("Central America", 200, 349);
		northAm.add("Central America");
		addLabel("Congo", 639, 523);
		africa.add("Congo");
		addLabel("China", 856, 295);
		asia.add("China");
		// E__________________________________
		addLabel("East Africa", 659, 446);
		africa.add("East Africa");
		addLabel("Eastern Australia", 1073, 599);
		australia.add("Eastern Australia");
		addLabel("Eastern United States", 278, 235);
		northAm.add("Eastern United States");
		addLabel("Egypt", 595, 388);
		africa.add("Egypt");
		// G__________________________________
		addLabel("Great Britain", 517, 202);
		europe.add("Great Britain");
		addLabel("Greenland", 398, 101);
		northAm.add("Greenland");
		// I__________________________________
		addLabel("Iceland", 496, 119);
		europe.add("Iceland");
		addLabel("India", 802, 342);
		asia.add("India");
		addLabel("Indonesia", 930, 462);
		australia.add("Indonesia");
		addLabel("Irrutsk", 910, 194);
		asia.add("Irrutsk");
		// J__________________________________
		addLabel("Japan", 1070, 235);
		asia.add("Japan");
		// K
		addLabel("Kamchatka", 983, 125);
		asia.add("Kamchatka");
		// M__________________________________
		addLabel("Madagascar", 697, 619);
		africa.add("Madagascar");
		addLabel("Middle East", 699, 373);
		asia.add("Middle East");
		addLabel("Mongolia", 930, 259);
		asia.add("Mongolia");
		// N__________________________________
		addLabel("New Guinea", 1070, 448);
		australia.add("New Guinea");
		addLabel("North Africa", 529, 430);
		africa.add("North Africa");
		addLabel("Northern Europe", 558, 252);
		europe.add("Northern Europe");
		addLabel("Northwest Territory", 269, 113);
		northAm.add("Northwest Territory");
		// O__________________________________
		addLabel("Ontario", 285, 191);
		northAm.add("Ontario");
		// P__________________________________
		addLabel("Peru", 317, 479);
		southAm.add("Peru");
		// Q__________________________________
		addLabel("Quebec", 329, 198);
		northAm.add("Quebec");
		// S__________________________________
		addLabel("Scandinavia", 577, 160);
		europe.add("Scandinavia");
		addLabel("Siam", 907, 370);
		asia.add("Siam");
		addLabel("Siberia", 843, 155);
		asia.add("Siberia");
		addLabel("South Africa", 627, 611);
		africa.add("South Africa");
		addLabel("Southern Europe", 605, 304);
		europe.add("Southern Europe");
		// U__________________________________
		addLabel("Ukraine", 668, 210);
		europe.add("Ukraine");
		addLabel("Ural", 802, 196);
		asia.add("Ural");
		// W__________________________________
		addLabel("Western Australia", 978, 569);
		australia.add("Western Australia");
		addLabel("Western Europe", 518, 287);
		europe.add("Western Europe");
		addLabel("Western United States", 237, 222);
		northAm.add("Western United States");
		// V__________________________________
		addLabel("Venezuela", 271, 398);
		southAm.add("Venezuela");
		// Y__________________________________
		addLabel("Yakutsk", 912, 117);
		asia.add("Yakutsk");
		// ______________________________________________________
		// OVAL INSTANTIATION
		// A__________________________________
		addCircleArea("Afganistan", 762, 252);
		addCircleArea("Alberta", 190, 160);
		addCircleArea("Alaska", 94, 71);
		addCircleArea("Argentina", 293, 547);
		// B__________________________________
		addCircleArea("Brazil", 344, 443);
		// C__________________________________
		addCircleArea("Central America", 190, 321);
		addCircleArea("Congo", 626, 494);
		addCircleArea("China", 843, 266);
		// E__________________________________
		addCircleArea("East Africa", 647, 418);
		addCircleArea("Eastern Australia", 1064, 570);
		addCircleArea("Eastern United States", 265, 213);
		addCircleArea("Egypt", 583, 353);
		// G__________________________________
		addCircleArea("Great Britain", 504, 176);
		addCircleArea("Greenland", 387, 76);
		// I__________________________________
		addCircleArea("Iceland", 484, 89);
		addCircleArea("India", 791, 311);
		addCircleArea("Indonesia", 916, 433);
		addCircleArea("Irrutsk", 901, 165);
		// J__________________________________
		addCircleArea("Japan", 1058, 201);
		// K
		addCircleArea("Kamchatka", 969, 94);
		// M__________________________________
		addCircleArea("Madagascar", 685, 589);
		addCircleArea("Middle East", 685, 344);
		addCircleArea("Mongolia", 919, 232);
		// N__________________________________
		addCircleArea("New Guinea", 1057, 416);
		addCircleArea("North Africa", 514, 399);
		addCircleArea("Northern Europe", 546, 221);
		addCircleArea("Northwest Territory", 257, 81);
		// O__________________________________
		addCircleArea("Ontario", 275, 164);
		// P__________________________________
		addCircleArea("Peru", 305, 453);
		// Q__________________________________
		addCircleArea("Quebec", 315, 175);
		// S__________________________________
		addCircleArea("Scandinavia", 566, 132);
		addCircleArea("Siam", 894, 342);
		addCircleArea("Siberia", 829, 127);
		addCircleArea("South Africa", 616, 583);
		addCircleArea("Southern Europe", 592, 275);
		// U__________________________________
		addCircleArea("Ukraine", 655, 181);
		addCircleArea("Ural", 790, 170);
		// W__________________________________
		addCircleArea("Western Australia", 963, 541);
		addCircleArea("Western Europe", 504, 261);
		addCircleArea("Western United States", 222, 189);
		// V__________________________________
		addCircleArea("Venezuela", 260, 371);
		// Y__________________________________
		addCircleArea("Yakutsk", 898, 88);		
	}
	
	public void mapGen() {
		Random random = new Random();
		for (String key : countries.keySet()) {
			int gen = random.nextInt(3);
			switch (gen) {
			case 0:
				if (!teamRed.holdingsFull()) {
					teamRed.addHolding(countries.get(key));
					countries.get(key).setControl(teamRed);
					break;
				}
			case 1:
				if (!teamGreen.holdingsFull()) {
					teamGreen.addHolding(countries.get(key));
					countries.get(key).setControl(teamGreen);
					break;
				}
			default:
				if (!teamYellow.holdingsFull())
					teamYellow.addHolding(countries.get(key));
				
				else if (!teamRed.holdingsFull())
					teamRed.addHolding(countries.get(key));
				
				else
					teamGreen.addHolding(countries.get(key));
				
			}
			troops.get(key).setColor(countries.get(key).getControl().getColor());
			troops.get(key).setLabel("1");
		}

	}
	// Initializes the connections between countries so you can like attack and stuff
	public void borders(){
		// AFGANISTAN____________________________________________________________________________
		countries.get("Afganistan").addBorder(countries.get("China"));
		countries.get("Afganistan").addBorder(countries.get("India"));
		countries.get("Afganistan").addBorder(countries.get("Middle East"));
		countries.get("Afganistan").addBorder(countries.get("Ukraine"));
		countries.get("Afganistan").addBorder(countries.get("Ural"));
		// ALBERTA_______________________________________________________________________________
		countries.get("Alberta").addBorder(countries.get("Alaska"));
		countries.get("Alberta").addBorder(countries.get("Northwest Territory"));
		countries.get("Alberta").addBorder(countries.get("Ontario"));
		countries.get("Alberta").addBorder(countries.get("Western United States"));
		// ALASKA________________________________________________________________________________
		countries.get("Alaska").addBorder(countries.get("Alberta"));
		countries.get("Alaska").addBorder(countries.get("Kamchatka"));
		countries.get("Alaska").addBorder(countries.get("Northwest Territory"));
		// ARGENTINA_____________________________________________________________________________
		countries.get("Argentina").addBorder(countries.get("Brazil"));
		countries.get("Argentina").addBorder(countries.get("Peru"));
		// BRAZIL________________________________________________________________________________
		countries.get("Brazil").addBorder(countries.get("Argentina"));
		countries.get("Brazil").addBorder(countries.get("Peru"));
		countries.get("Brazil").addBorder(countries.get("North Africa"));
		countries.get("Brazil").addBorder(countries.get("Venezuela"));
		// CENTRAL AMERICA_______________________________________________________________________
		countries.get("Central America").addBorder(countries.get("Eastern United States"));
		countries.get("Central America").addBorder(countries.get("Venezuela"));
		countries.get("Central America").addBorder(countries.get("Western United States"));
		// CONGO_________________________________________________________________________________
		countries.get("Congo").addBorder(countries.get("East Africa"));
		countries.get("Congo").addBorder(countries.get("North Africa"));
		countries.get("Congo").addBorder(countries.get("South Africa"));
		// CHINA_________________________________________________________________________________
		countries.get("China").addBorder(countries.get("Afganistan"));
		countries.get("China").addBorder(countries.get("India"));
		countries.get("China").addBorder(countries.get("Mongolia"));
		countries.get("China").addBorder(countries.get("Siam"));	
		countries.get("China").addBorder(countries.get("Siberia"));
		countries.get("China").addBorder(countries.get("Ural"));
		// EAST AFRICA___________________________________________________________________________
		countries.get("East Africa").addBorder(countries.get("Congo"));
		countries.get("East Africa").addBorder(countries.get("Egypt"));
		countries.get("East Africa").addBorder(countries.get("Madagascar"));
		countries.get("East Africa").addBorder(countries.get("North Africa"));
		countries.get("East Africa").addBorder(countries.get("South Africa"));
		// EASTERN AUSTRALIA_____________________________________________________________________
		countries.get("Eastern Australia").addBorder(countries.get("New Guinea"));
		countries.get("Eastern Australia").addBorder(countries.get("Western Australia"));
		// EASTERN UNITED STATES_________________________________________________________________
		countries.get("Eastern United States").addBorder(countries.get("Central America"));
		countries.get("Eastern United States").addBorder(countries.get("Ontario"));
		countries.get("Eastern United States").addBorder(countries.get("Quebec"));
		countries.get("Eastern United States").addBorder(countries.get("Western United States"));
		// EGYPT_________________________________________________________________________________
		countries.get("Egypt").addBorder(countries.get("East Africa"));
		countries.get("Egypt").addBorder(countries.get("Middle East"));
		countries.get("Egypt").addBorder(countries.get("North Africa"));
		countries.get("Egypt").addBorder(countries.get("Southern Europe"));
		// GREAT BRITAIN_________________________________________________________________________
		countries.get("Great Britain").addBorder(countries.get("Iceland"));
		countries.get("Great Britain").addBorder(countries.get("Northern Europe"));
		countries.get("Great Britain").addBorder(countries.get("Scandinavia"));
		countries.get("Great Britain").addBorder(countries.get("Western Europe"));
		// GREENLAND_____________________________________________________________________________
		countries.get("Greenland").addBorder(countries.get("Iceland"));
		countries.get("Greenland").addBorder(countries.get("Northwest Territory"));
		countries.get("Greenland").addBorder(countries.get("Ontario"));
		countries.get("Greenland").addBorder(countries.get("Quebec"));
		// ICELAND_______________________________________________________________________________
		countries.get("Iceland").addBorder(countries.get("Great Britain"));
		countries.get("Iceland").addBorder(countries.get("Greenland"));
		countries.get("Iceland").addBorder(countries.get("Scandinavia"));
		// INDIA_________________________________________________________________________________
		countries.get("India").addBorder(countries.get("Afganistan"));
		countries.get("India").addBorder(countries.get("China"));
		countries.get("India").addBorder(countries.get("Middle East"));
		countries.get("India").addBorder(countries.get("Siam"));
		// INDONESIA_____________________________________________________________________________
		countries.get("Indonesia").addBorder(countries.get("New Guinea"));
		countries.get("Indonesia").addBorder(countries.get("Siam"));
		countries.get("Indonesia").addBorder(countries.get("Western Australia"));
		// IRRUTSK_______________________________________________________________________________
		countries.get("Irrutsk").addBorder(countries.get("Kamchatka"));
		countries.get("Irrutsk").addBorder(countries.get("Mongolia"));
		countries.get("Irrutsk").addBorder(countries.get("Siberia"));
		countries.get("Irrutsk").addBorder(countries.get("Yakutsk"));
		// JAPAN_________________________________________________________________________________
		countries.get("Japan").addBorder(countries.get("Kamchatka"));
		countries.get("Japan").addBorder(countries.get("Mongolia"));
		// KAMCHATKA_____________________________________________________________________________
		countries.get("Kamchatka").addBorder(countries.get("Alaska"));
		countries.get("Kamchatka").addBorder(countries.get("Irrutsk"));
		countries.get("Kamchatka").addBorder(countries.get("Japan"));
		countries.get("Kamchatka").addBorder(countries.get("Mongolia"));
		countries.get("Kamchatka").addBorder(countries.get("Yakutsk"));
		// MADAGASCAR____________________________________________________________________________
		countries.get("Madagascar").addBorder(countries.get("East Africa"));
		// MIDDLE EAST___________________________________________________________________________
		countries.get("Middle East").addBorder(countries.get("Afganistan"));
		countries.get("Middle East").addBorder(countries.get("Egypt"));
		countries.get("Middle East").addBorder(countries.get("India"));
		countries.get("Middle East").addBorder(countries.get("Southern Europe"));
		countries.get("Middle East").addBorder(countries.get("Ukraine"));
		// MONGOLIA______________________________________________________________________________
		countries.get("Mongolia").addBorder(countries.get("China"));
		countries.get("Mongolia").addBorder(countries.get("Irrutsk"));
		countries.get("Mongolia").addBorder(countries.get("Japan"));
		countries.get("Mongolia").addBorder(countries.get("Kamchatka"));
		countries.get("Mongolia").addBorder(countries.get("Siberia"));
		// NEW GUINEA____________________________________________________________________________
		countries.get("New Guinea").addBorder(countries.get("Eastern Australia"));
		countries.get("New Guinea").addBorder(countries.get("Indonesia"));
		countries.get("New Guinea").addBorder(countries.get("Western Australia"));
		// NORTH AFRICA__________________________________________________________________________
		countries.get("North Africa").addBorder(countries.get("Brazil"));
		countries.get("North Africa").addBorder(countries.get("Congo"));
		countries.get("North Africa").addBorder(countries.get("East Africa"));
		countries.get("North Africa").addBorder(countries.get("Egypt"));
		countries.get("North Africa").addBorder(countries.get("Southern Europe"));
		countries.get("North Africa").addBorder(countries.get("Western Europe"));
		// NORTHERN EUROPE_______________________________________________________________________
		countries.get("Northern Europe").addBorder(countries.get("Great Britain"));
		countries.get("Northern Europe").addBorder(countries.get("Western Europe"));
		countries.get("Northern Europe").addBorder(countries.get("Scandinavia"));
		countries.get("Northern Europe").addBorder(countries.get("Southern Europe"));
		countries.get("Northern Europe").addBorder(countries.get("Ukraine"));
		// NORTHWEST TERRITORY___________________________________________________________________
		countries.get("Northwest Territory").addBorder(countries.get("Alaska"));
		countries.get("Northwest Territory").addBorder(countries.get("Alberta"));
		countries.get("Northwest Territory").addBorder(countries.get("Greenland"));
		countries.get("Northwest Territory").addBorder(countries.get("Ontario"));
		// ONTARIO_______________________________________________________________________________
		countries.get("Ontario").addBorder(countries.get("Alberta"));
		countries.get("Ontario").addBorder(countries.get("Eastern United States"));
		countries.get("Ontario").addBorder(countries.get("Greenland"));
		countries.get("Ontario").addBorder(countries.get("Northwest Territory"));
		countries.get("Ontario").addBorder(countries.get("Quebec"));
		countries.get("Ontario").addBorder(countries.get("Western United States"));
		// PERU__________________________________________________________________________________
		countries.get("Peru").addBorder(countries.get("Argentina"));
		countries.get("Peru").addBorder(countries.get("Brazil"));
		countries.get("Peru").addBorder(countries.get("Venezuela"));
		// QUEBEC________________________________________________________________________________
		countries.get("Quebec").addBorder(countries.get("Eastern United States"));
		countries.get("Quebec").addBorder(countries.get("Greenland"));
		countries.get("Quebec").addBorder(countries.get("Ontario"));
		// SCANDINAVIA___________________________________________________________________________
		countries.get("Scandinavia").addBorder(countries.get("Great Britain"));
		countries.get("Scandinavia").addBorder(countries.get("Iceland"));
		countries.get("Scandinavia").addBorder(countries.get("Northern Europe"));
		countries.get("Scandinavia").addBorder(countries.get("Ukraine"));
		// SIAM__________________________________________________________________________________
		countries.get("Siam").addBorder(countries.get("China"));
		countries.get("Siam").addBorder(countries.get("India"));
		countries.get("Siam").addBorder(countries.get("Indonesia"));
		// SIBERIA_______________________________________________________________________________
		countries.get("Siberia").addBorder(countries.get("China"));
		countries.get("Siberia").addBorder(countries.get("Irrutsk"));
		countries.get("Siberia").addBorder(countries.get("Mongolia"));
		countries.get("Siberia").addBorder(countries.get("Ural"));
		countries.get("Siberia").addBorder(countries.get("Yakutsk"));
		// SOUTH AFRICA__________________________________________________________________________
		countries.get("South Africa").addBorder(countries.get("Congo"));
		countries.get("South Africa").addBorder(countries.get("East Africa"));
		// SOUTHERN EUROPE_______________________________________________________________________
		countries.get("Southern Europe").addBorder(countries.get("Egypt"));
		countries.get("Southern Europe").addBorder(countries.get("Middle East"));
		countries.get("Southern Europe").addBorder(countries.get("North Africa"));
		countries.get("Southern Europe").addBorder(countries.get("Northern Europe"));
		countries.get("Southern Europe").addBorder(countries.get("Ukraine"));
		countries.get("Southern Europe").addBorder(countries.get("Western Europe"));
		// UKRAINE_______________________________________________________________________________
		countries.get("Ukraine").addBorder(countries.get("Afganistan"));
		countries.get("Ukraine").addBorder(countries.get("Middle East"));
		countries.get("Ukraine").addBorder(countries.get("Northern Europe"));
		countries.get("Ukraine").addBorder(countries.get("Scandinavia"));
		countries.get("Ukraine").addBorder(countries.get("Southern Europe"));
		countries.get("Ukraine").addBorder(countries.get("Ural"));
		// URAL__________________________________________________________________________________
		countries.get("Ural").addBorder(countries.get("Afganistan"));
		countries.get("Ural").addBorder(countries.get("China"));
		countries.get("Ural").addBorder(countries.get("Siberia"));
		countries.get("Ural").addBorder(countries.get("Ukraine"));
		// WESTERN AUSTRALIA_____________________________________________________________________
		countries.get("Western Australia").addBorder(countries.get("Eastern Australia"));
		countries.get("Western Australia").addBorder(countries.get("Indonesia"));
		countries.get("Western Australia").addBorder(countries.get("New Guinea"));
		// WESTERN EUROPE________________________________________________________________________
		countries.get("Western Europe").addBorder(countries.get("Great Britain"));
		countries.get("Western Europe").addBorder(countries.get("North Africa"));
		countries.get("Western Europe").addBorder(countries.get("Northern Europe"));
		countries.get("Western Europe").addBorder(countries.get("Southern Europe"));
		// WESTERN UNITED STATES_________________________________________________________________
		countries.get("Western United States").addBorder(countries.get("Alberta"));
		countries.get("Western United States").addBorder(countries.get("Central America"));
		countries.get("Western United States").addBorder(countries.get("Eastern United States"));
		countries.get("Western United States").addBorder(countries.get("Ontario"));
		// VENEZUELA_____________________________________________________________________________
		countries.get("Venezuela").addBorder(countries.get("Brazil"));
		countries.get("Venezuela").addBorder(countries.get("Central America"));
		countries.get("Venezuela").addBorder(countries.get("Peru"));
		// YAKUTSK_______________________________________________________________________________
		countries.get("Yakutsk").addBorder(countries.get("Irrutsk"));
		countries.get("Yakutsk").addBorder(countries.get("Kamchatka"));
		countries.get("Yakutsk").addBorder(countries.get("Siberia"));
	}
	
	public int continentalControl(){
		int extraTroops = 0;
		boolean allAfrica = true;
		boolean allAsia = true;
		boolean allAustralia = true;
		boolean allEurope = true;
		boolean allNAmerica = true;
		boolean allSAmerica = true;
		
		for(String c: africa)
		{
			if(!countries.get(c).getControl().isTurn())
				allAfrica = false;
		}
		for(String c: asia)
		{
			if(!countries.get(c).getControl().isTurn())
				allAsia = false;
		}
		for(String c: australia)
		{
			if(!countries.get(c).getControl().isTurn())
				allAustralia = false;
		}
		for(String c: europe)
		{
			if(!countries.get(c).getControl().isTurn())
				allEurope = false;
		}
		for(String c: northAm)
		{
			if(!countries.get(c).getControl().isTurn())
				allNAmerica = false;
		}
		for(String c: southAm)
		{
			if(!countries.get(c).getControl().isTurn())
				allSAmerica = false;
		}
		if (allAfrica)
			extraTroops += AFRICA_NUM;
		if (allAsia)
			extraTroops += ASIA_NUM;
		if (allAustralia)
			extraTroops += AUSSIE_NUM;
		if (allEurope)
			extraTroops += EUROPE_NUM;
		if (allNAmerica)
			extraTroops += NORTH_AMERICA_NUM;
		if (allSAmerica)
			extraTroops += SOUTH_AMERICA_NUM;
		
		return extraTroops;
	}//CONTINENTAL CONTROL END
	
}
