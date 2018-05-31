package nl.hu.v1wac.herkansing.persistence;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

import nl.hu.v1wac.herkansing.model.Country;

public class CountryDAO extends BaseDAO {

    public void save(Country country) {
        System.out.print(country);
        try (Connection conn = super.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO country " +
                            "(code, name, continent, region, surfacearea, population, localname, governmentform, code2, latitude, longitude, capital) " +
                            "VALUES " +
                            "(?,?,CAST(? AS continenttype),?,?,?,?,?,?,?,?,?)");

            stmt.setString(1, country.getIso3Code());
            stmt.setString(2, country.getName());
            stmt.setString(3, country.getContinent());
            stmt.setString(4, country.getRegion());
            stmt.setDouble(5, country.getSurface());
            stmt.setInt(6, country.getPopulation());
            stmt.setString(7, country.getName());
            stmt.setString(8, country.getGovernment());
            stmt.setString(9, country.getCode());
            stmt.setDouble(10, country.getLatitude());
            stmt.setDouble(11, country.getLongitude());
            stmt.setString(12, country.getCapital());

            int affectedRows = stmt.executeUpdate();
//            if (affectedRows == 1) return country;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public List<Country> findAll() {
        return selectCountries("SELECT * FROM country");
    }

    public Country findByCode(String code) {
        List<Country> countries = selectCountries("SELECT * FROM country WHERE upper(code2) = '" + code.toUpperCase() + "' OR upper(code) = '" + code.toUpperCase() + "'");
        if (countries.size() > 0) return countries.get(0);
        return null;


    }

    public List<Country> find10LargestPopulations() {
    	List<Country> countries = selectCountries("SELECT * FROM country ORDER BY population DESC LIMIT 10");
    	return countries;
    }
    
//    public List<Country> find10LargestPopulations() {
//		List<Country> countries = null;
//		try {
//			Connection myConn = super.getConnection();
//
//			PreparedStatement pstmt = myConn.prepareStatement("SELECT * FROM country ORDER BY population DESC LIMIT 10");
//			pstmt.executeQuery();
//
//			ResultSet dbResultSet = pstmt.getResultSet();
//
//			while (dbResultSet.next()) {
//				String iso2Code = dbResultSet.getString("code2");
//                String iso3Code = dbResultSet.getString("code");
//                String name = dbResultSet.getString("name");
//                String capital = dbResultSet.getString("capital");
//                String continent = dbResultSet.getString("continent");
//                String region = dbResultSet.getString("region");
//                double surface = dbResultSet.getDouble("surfacearea");
//                int population = dbResultSet.getInt("population");
//                String government = dbResultSet.getString("governmentform");
//                double latitude = dbResultSet.getDouble("latitude");
//                double longitude = dbResultSet.getDouble("longitude");
//
//                countries.add(new Country(iso2Code, iso3Code, name, capital, continent, region, surface, population, government, latitude, longitude));
//			}
//		} catch (Exception exc) {
//			exc.printStackTrace();
//		}
//		return countries;
//	}

    public List<Country> find10LargestSurfaces() {
    	List<Country> countries =  selectCountries("SELECT * FROM country ORDER BY surfacearea DESC LIMIT 10");
    	return countries;
    }

    public Country update(Country country) {
        try (Connection conn = super.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE country SET name = ?, continent = CAST(? AS continenttype), region = ?, surfacearea = ?, population = ?, localname = ?, governmentform = ?, code2 = ?, latitude = ?, longitude = ?, capital = ? WHERE code = ?");


            stmt.setString(1, country.getName());
            stmt.setString(2, country.getContinent());
            stmt.setString(3, country.getRegion());
            stmt.setDouble(4, country.getSurface());
            stmt.setInt(5, country.getPopulation());
            stmt.setString(6, country.getName());
            stmt.setString(7, country.getGovernment());
            stmt.setString(8, country.getCode());
            stmt.setDouble(9, country.getLatitude());
            stmt.setDouble(10, country.getLongitude());
            stmt.setString(11, country.getCapital());

            stmt.setString(12, country.getIso3Code());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 1) return country;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

    public boolean delete(Country country) {
        boolean succes = false;

        try (Connection conn = super.getConnection()) {
            String delete_fk = "DELETE FROM city WHERE countrycode = ?";
            PreparedStatement fk_stmt = conn.prepareStatement(delete_fk);
            fk_stmt.setString(1, country.getIso3Code());
            fk_stmt.executeUpdate();

            String delete_fk_2 = "DELETE FROM countrylanguage WHERE countrycode = ?";
            PreparedStatement fk_stmt_2 = conn.prepareStatement(delete_fk_2);
            fk_stmt_2.setString(1, country.getIso3Code());
            fk_stmt_2.executeUpdate();

            PreparedStatement stmt = conn.prepareStatement("DELETE FROM country WHERE code = ?");
            stmt.setString(1, country.getIso3Code());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 1) {
                succes = true;
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return succes;
    }

    private List<Country> selectCountries(String query) {
        List<Country> countries = new ArrayList<Country>();

        try (Connection conn = super.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet dbResultSet = stmt.executeQuery(query);

            while(dbResultSet.next()) {
                String iso2Code = dbResultSet.getString("code2");
                String iso3Code = dbResultSet.getString("code");
                String name = dbResultSet.getString("name");
                String capital = dbResultSet.getString("capital");
                String continent = dbResultSet.getString("continent");
                String region = dbResultSet.getString("region");
                double surface = dbResultSet.getDouble("surfacearea");
                int population = dbResultSet.getInt("population");
                String government = dbResultSet.getString("governmentform");
                double latitude = dbResultSet.getDouble("latitude");
                double longitude = dbResultSet.getDouble("longitude");

                countries.add(new Country(iso2Code, iso3Code, name, capital, continent, region, surface, population, government, latitude, longitude));
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return countries;
    }

}