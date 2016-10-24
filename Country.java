import java.util.ArrayList;

//Robin's code
public class Country {

	// CONSTRUCTOR
	// GLOBAL VARIABLE COMPONENTS
	private int cornerX;
	private int cornerY;
	private int length;
	private int height;
	private String name;
	private Team control;
	private String continent;
	
	public  ArrayList<Country> borders;

	// CONSTRUCTOR
	public Country(String name, int x, int y, int length, int height) {
		cornerX = x;
		cornerY = y;
		this.length = length;
		this.height = height;
		this.name = name;
		control = null;
		borders = new ArrayList<Country>();
	}

	public boolean contains(int x, int y) {
		return (x >= cornerX && x <= cornerX + length && y >= cornerY && y <= cornerY
				+ height);
	}

	public String getName() {
		return name;
	}

	// FOR SIMPLE CHANGES NOT ALL THAT IMPORTANT
	public int getCornerX() {
		return cornerX;
	}

	public void setCornerX(int cornerX) {
		this.cornerX = cornerX;
	}

	public int getCornerY() {
		return cornerY;
	}

	public void setCornerY(int cornerY) {
		this.cornerY = cornerY;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	// DEFIINES WHICH TEAM OWNS THE COUNTRY
	public Team getControl() {
		return control;
	}

	public void setControl(Team control) {
		this.control = control;
	}
	
	public void addBorder( Country c){
		borders.add(c);
	}
	
	public boolean hasBorder(Country c){
		if(borders.contains(c))
			return true;
		else
			return false;
	}

	public String getContinent() {
		return continent;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}
}