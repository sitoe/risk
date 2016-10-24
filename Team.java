import java.awt.Color;
import java.util.ArrayList;

public class Team
{
	//VARIABLES
	private int troopPlacement;
	private boolean turn;
	private Color color;
	private ArrayList<Country> holdings; //FUTURE OPTIMIZATIONS
	
	public Team(Color color){
		this.color = color;
		holdings = new ArrayList<Country> ();
		turn = false;
	}

	public boolean isTurn() {
		return turn;
	}

	public void setTurn(boolean turn) {
		this.turn = turn;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ArrayList<Country> getHoldings() {
		return holdings;
	}

	public void addHolding(Country hold)
	{
		if(hold.getControl()== null){
			holdings.add(hold);
			hold.setControl(this);
		}
	}
	
	public void changeTurn(){
		turn = !turn;
	}
	
	public boolean holdingsFull()
	{
		if(holdings.size()==14)
			return true;
		else
			return false;
	}
	public void removeFromHoldings(Country c){
		if(holdings.contains(c)){
			holdings.remove(holdings.indexOf(c));
		}
	}

	public int getTroopPlacement() {
		return troopPlacement;
	}

	public void setTroopPlacement(int troopPlacement) {
		this.troopPlacement = troopPlacement;
	}
}