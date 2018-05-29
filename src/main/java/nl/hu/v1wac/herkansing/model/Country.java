package nl.hu.v1wac.herkansing.model;

public class Country {
	private String iso2Code;
	private String iso3Code;
	private String name;
	private String capital;
	private String continent;
	private String region;
	private double surface;
	private int population;
	private String government;
	private double latitude;
	private double longitude;
	
	
	public Country(String iso2cd, String iso3cd, String nm, String cap, String ct, String reg, double sur, int pop, String gov, double lat, double lng) {
		iso2Code = iso2cd;
		iso3Code = iso3cd;
		name = nm;
		capital = cap;
		continent = ct;
		region = reg;
		surface = sur;
		population = pop;
		government = gov;
		latitude = lat;
		longitude = lng;
	}

	public Country(String iso2cd) {
		iso2Code = iso2cd;
	}

	public String getCode() {
		return iso2Code;
	}
	
	public String getIso3Code() {
		return iso3Code;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCapital() {
		return capital;
	}
	
	public String getContinent() {
		return continent;
	}
	
	public String getRegion() {
		return region;
	}
	
	public double getSurface() {
		return surface;
	}
	
	public int getPopulation() {
		return population;
	}
	
	public String getGovernment() {
		return government;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public void setIso2Code(String iso2Code) {
		this.iso2Code = iso2Code;
	}

	public void setIso3Code(String iso3Code) {
		this.iso3Code = iso3Code;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public void setContinent(String continent) {
		this.continent = continent;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setSurface(double surface) {
		this.surface = surface;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public void setGovernment(String government) {
		this.government = government;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "Country{" +
				"iso2Code='" + iso2Code + '\'' +
				", iso3Code='" + iso3Code + '\'' +
				", name='" + name + '\'' +
				", capital='" + capital + '\'' +
				", continent='" + continent + '\'' +
				", region='" + region + '\'' +
				", surface=" + surface +
				", population=" + population +
				", government='" + government + '\'' +
				", latitude=" + latitude +
				", longitude=" + longitude +
				'}';
	}
}
