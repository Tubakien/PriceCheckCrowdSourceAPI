package edu.matc.pricecheck;

/**
 * Created by student on 3/4/17.
 */

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by student on 3/1/17.
 */


@Path("/pricerequest")
public class PriceRequest {
    //TODO Pass in Request
    // The Java method will process HTTP GET requests

    @GET
    // The Java method will produce content identified by the MIME Media type "text/plain"
    @Path("/JSON/{param}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMsgPlainJSON(@PathParam("param") String msg,
                                    @QueryParam("name") String itemName,
                                    @QueryParam("brand") String brandName,
                                    @QueryParam("lon") double longtitude,
                                    @QueryParam("lat") double latitude,
                                    @QueryParam("distance") double distance) {
        ProcessRequest processRequest = null;
        Request request = null;

        request = processMessage(msg, itemName, brandName, longtitude,
                latitude, distance);

        // Return a simple message
        processRequest = new ProcessRequest();
        String output = processRequest.getItem(request);
        return Response.status(200).entity(output).build();
    }

    private Request processMessage(String msg, String itemName, String
            brandName, double longtitude, double latitude, double distance) {
        String parameter = null;

        if (msg.startsWith("request?")) {
            parameter = msg.substring(9);
        }

        return createRequest(itemName, brandName, latitude, longtitude,
                distance);
    }

    private Request createRequest(String itemName, String brandName, double
            latitude, double longtitude, double distance) {
        Request request = new Request();
        List<EntryItem> entryList = new ArrayList<EntryItem>();
        List<ItemsItem> itemList = new ArrayList<ItemsItem>();
        EntryItem entryItem = new EntryItem();
        ItemsItem itemsItem = new ItemsItem();

        request.setAction("request");
        request.setRadiusMile((int) distance);
        request.setUserLatitude(latitude);
        request.setUserLongtitude(longtitude);
        itemsItem.setName(itemName);
        itemsItem.setBrand(brandName);
        itemList.add(itemsItem);
        entryItem.setItems(itemList);
        entryList.add(entryItem);
        request.setEntry(entryList);

        return request;
    }
}

