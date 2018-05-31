package nl.hu.v1wac.herkansing.webservices;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.json.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.hu.v1wac.herkansing.model.Country;
import nl.hu.v1wac.herkansing.model.ServiceProvider;
import nl.hu.v1wac.herkansing.model.WorldService;

import java.io.StringReader;

@PermitAll
@Path("/countries")
public class WorldResource {

    private WorldService service = ServiceProvider.getWorldService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountries() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Country c : service.getAllCountries()) {
            JsonObjectBuilder job = createModelObject(c);
            jab.add(job);
        }
        return jab.build().toString();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addCountry(String json) {
        System.out.println(json);
        JsonObject object = stringToJson(json);
        Country country = buildCountry(object);
        service.addCountry(country);
        return Response.ok().build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateCountry(String json) {
        System.out.println(json);
        JsonObject object = stringToJson(json);
        Country country = buildCountry(object);
        service.updateCountry(country);
        return Response.ok().build();
    }


    @GET
    @Path("/largestsurfaces")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLargestSurfaces() {
        JsonArrayBuilder jab = Json.createArrayBuilder();

        for (Country c : service.get10LargestSurfaces()) {
            JsonObjectBuilder job = createModelObject(c);
            jab.add(job);
        }
        return jab.build().toString();
    }


    @GET
    @Path("/largestpopulations")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLargestPopulations() {
        JsonArrayBuilder jab = Json.createArrayBuilder();


        for (Country c : service.get10LargestPopulations()) {
            JsonObjectBuilder job = createModelObject(c);
            jab.add(job);
        }

        return jab.build().toString();
    }


    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountry(@PathParam("code") String code) {
        Country c = service.getCountryByCode(code);
        JsonObjectBuilder job = createModelObject(c);
        return job.build().toString();
    }

    @DELETE
    @RolesAllowed("admin")
    @Path("/{code}")
    public Response deleteCountry(@PathParam("code") String code) {
        System.out.println(code);
        Country country = service.getCountryByCode(code);
        System.out.println(country);
        boolean success = service.delete(country);

        return Response.ok().build();
    }

    private JsonObject stringToJson(String jsonString) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonString));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    private JsonObjectBuilder createModelObject(Country c) {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("code", c.getCode())
                .add("name", c.getName())
                .add("continent", c.getContinent())
                .add("region", c.getRegion())
                .add("surface", c.getSurface())
                .add("population", c.getPopulation())
                .add("government", c.getGovernment())
                .add("code3", c.getIso3Code())
                .add("capital", c.getCapital())
                .add("latitude", c.getLatitude())
                .add("longitude", c.getLongitude());
        return job;
    }

    private Country buildCountry(JsonObject object) {
        Country country = new Country(object.getString("code"));
        country.setName(object.getString("name"));
        country.setContinent(object.getString("continent"));
        country.setRegion(object.getString("region"));
        country.setSurface(Double.parseDouble(object.getString("surface")));
        country.setPopulation(Integer.parseInt(object.getString("population")));
        country.setGovernment(object.getString("government"));
        country.setIso3Code(object.getString("code3"));
        country.setCapital(object.getString("capital"));

        country.setLatitude(Double.parseDouble(object.getString("latitude")));
        country.setLongitude(Double.parseDouble(object.getString("longitude")));

        System.out.println(country);

        return country;
    }
}
