import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import acm.graphics.GLabel;


public class AI {
	private String teamColor = "Yellow";
	private int numCountries =0;
	private ArrayList<Country> myCountries = new ArrayList<Country>();
	private boolean turn;
	private Team team;
	private static final int ASIA_NUM = 7;
	private static final int EUROPE_NUM = 5;
	private static final int NORTH_AMERICA_NUM = 5;
	private static final int AFRICA_NUM = 3;
	private static final int SOUTH_AMERICA_NUM = 2;
	private static final int AUSSIE_NUM = 2;
	private ArrayList<Country> asia = new ArrayList<Country>();
	private ArrayList<Country> europe = new ArrayList<Country>();
	private ArrayList<Country> northAm = new ArrayList<Country>();
	private ArrayList<Country> africa = new ArrayList<Country>();
	private ArrayList<Country> southAm = new ArrayList<Country>();
	private ArrayList<Country> australia = new ArrayList<Country>();
	private ArrayList<ArrayList> allFull = new ArrayList<ArrayList>();

	private HashMap<String, GLabel> notMyCountries = new HashMap<String, GLabel>();
	//Must access its own countries' and the location of its opponents'
	//Country class should have knowledge of all the countries it 
	//connects to

	public AI(ArrayList holdings, Team team, ArrayList asia1,
			ArrayList europe1, ArrayList northAm1, ArrayList africa1, 
			ArrayList southAm1, ArrayList australia1){
		this.myCountries = team.getHoldings();
		this.team = team;
		allFull.add(asia1);
		allFull.add(europe1);
		allFull.add(northAm1);
		allFull.add(africa1);
		allFull.add(southAm1);
		allFull.add(australia1);

		//ADDS THE CONTINENTS TO THE ACTUAL DATA STRUCTURES
		for(int i = 0; i<myCountries.size();i++){
			if(asia1.contains(myCountries.get(i))){
				asia.add(myCountries.get(i));
			}
			else if(europe1.contains(myCountries.get(i))){
				europe.add(myCountries.get(i));
			}
			else if(northAm1.contains(myCountries.get(i))){
				northAm.add(myCountries.get(i));
			}
			else if(africa1.contains(myCountries.get(i))){
				africa.add(myCountries.get(i));
			}
			else if(southAm1.contains(myCountries.get(i))){
				southAm.add(myCountries.get(i));
			}
			else{
				australia.add(myCountries.get(i));
			}
			numCountries++;
		}


	}

	public void numberOfTroops(){

	}

	public void attack(){
		//check to see if which continent holds more AI countries and 
		//higher concentration of troops
		Country from =countrySelection().get(0).get(0);
		Country to =countrySelection().get(1).get(0);
		RiskRunner.attack(from, to,from.getControl(),to.getControl());
	}

	public ArrayList<ArrayList> continentSelection(){
		ArrayList<ArrayList> allTheLists = new ArrayList<ArrayList>(); 
		allTheLists.add(asia);
		allTheLists.add(europe);
		allTheLists.add(northAm);
		allTheLists.add(africa);
		allTheLists.add(southAm);
		allTheLists.add(australia);
		allTheLists= sort(allTheLists);

		return allTheLists;

	}

	public ArrayList<ArrayList<Country>> countrySelection(){
		ArrayList<Country> possibleWinners = continentSelection().get(0); 
		ArrayList<Country> orderedAttack = new ArrayList<Country>();
		ArrayList<Country> whoAttacked = new ArrayList<Country>();
		//ArrayList<ArrayList> borders = new ArrayList<ArrayList>();
		//ADDS BORDERS TO AN ARRAYLIST
		/*for(int i = 0; i < possibleWinners.get(i).borders.size();i++){
			borders.add(possibleWinners.get(i).borders);
		}*/
		orderedAttack.add(possibleWinners.get(0).borders.get(0));
		whoAttacked.add(possibleWinners.get(0));
		for (int i = 1; i < possibleWinners.size();i++){
			//For loop for internal arrays of country borders
			for (int j =  0; j < possibleWinners.get(i).borders.size();i++){
				int borderTroops = Integer.parseInt(RiskRunner.troops.get(possibleWinners.get(i).borders.get(j).getName()).getLabel());
				int myTroops =  Integer.parseInt(RiskRunner.troops.get(possibleWinners.get(i).getName()).getLabel());
				if(myTroops - borderTroops > 0){
					for (int k = 0; k < orderedAttack.size();k++){
						if(myTroops - borderTroops > 
						Integer.parseInt(RiskRunner.troops.get(orderedAttack.get(k).getName()).getLabel())){
							orderedAttack.add(k,possibleWinners.get(i).borders.get(j));	
							whoAttacked.add(k,possibleWinners.get(i));
						}
					}
				}

			}
		}
		ArrayList<ArrayList<Country>> a = new ArrayList<ArrayList<Country>>();
		a.add(whoAttacked);
		a.add(orderedAttack);
		return a;				
	}

	public ArrayList<ArrayList> sort(ArrayList<ArrayList> c){
		ArrayList<ArrayList> backup = new ArrayList<ArrayList>();
		backup.add(c.get(0));
		for(int i = 0;i < c.size()-1;i++){
			for (int j = 1;j < c.size();j++){
				if(c.get(j).size()/allFull.get(j).size() > backup.get(i).size()/allFull.get(i).size()){
					allFull.add(0, c.get(j));
				}
				else{
					backup.add(c.get(j));	

				}

			}

		}


		return backup;
	}
	//Sould work, inept AI though
	public void placeTroops(ArrayList<Country> myCountries, HashMap notMyCountries){
		ArrayList<Country> toAdd= countrySelection().get(0);
		int i = 0;		
		while(toAdd.get(0).getControl().getTroopPlacement() > 0){
		int troops = Integer.parseInt(RiskRunner.troops.get(toAdd.get(toAdd.size()-i).getName()).getLabel());	
			
			if(troops < 5){
				RiskRunner.troops.get(toAdd.get(toAdd.size()-i).getName()).setLabel(Integer.toString(troops++));				
				
			}else if(troops >= 4){
			 i++;			
			}
		}
		
			
			
			
		

	}
}