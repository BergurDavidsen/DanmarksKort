package dk.itu.MapOfDenmark;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class Address {
    public final String street, house, floor, side, postcode, city;


    private Address(
            String _street, String _house, String _floor, String _side,
            String _postcode, String _city) {
        street = _street;
        house = _house;
        floor = _floor;
        side = _side;
        postcode = _postcode;
        city = _city;
    }
    public String toString() {
        return street + " " + house + ", " + floor + " " + side + "\n"
                + postcode + " " + city;
    }
    private final static String REGEX = "(?<street>[A-Za-zÆØÅæøå.\\s ]+)(?<house>[0-9]+[a-zA-Z]? )?(?<floor>[0-9]+. )?(?<side>[a-z]{2}. )?(?<postcode>[0-9]{4} )?(?<city>[A-Za-zÆØÅæøå ]+)?";
    private final static Pattern PATTERN = Pattern.compile(REGEX);
    static boolean noNumber = false;
    public static Address parse(String input) {
        var matcher = PATTERN.matcher(input);
        var builder = new Builder();
        if (matcher.matches()){
            builder.street = matcher.group("street");
            builder.house = matcher.group("house");
            builder.postcode = matcher.group("postcode");
            builder.city = matcher.group("city");
            builder.floor = matcher.group("floor");
            builder.side = matcher.group("side");
        }
        if (matcher.group("house") == null) noNumber = true;
        return builder.build();
    }

    public long matchAddress(InputStream inputStream) throws XMLStreamException {
        long id = 0;
        String houseNumber = "";
        Double lat = 0.0;
        Double lon = 0.0;
        String streetName = street.replaceAll("\\s+", "").toLowerCase(); //Removes space after streetname for the users  and sets to lower case
        boolean houseNumberMatch = true;
        if (!noNumber){
            houseNumber = house.replaceAll("\\s+", "").toLowerCase(); //Removes space after houseNumber for the users input and sets to lower case
            houseNumberMatch = false;
        }
        boolean streetMatch = false;
        var input = XMLInputFactory.newInstance().createXMLStreamReader(new InputStreamReader(inputStream));
        while (input.hasNext()) {
            var tagKind = input.next();
            if (tagKind == XMLStreamConstants.START_ELEMENT) {
                var name = input.getLocalName(); // <name ... >
                if (name.equals("node")){
                    if (!noNumber){
                        houseNumberMatch = false;
                    }
                    streetMatch = false;
                    id = Long.parseLong(input.getAttributeValue(null, "id")); //Saves id for node before checking tags
                    lat = Double.parseDouble(input.getAttributeValue(null, "lat"));
                    lon = Double.parseDouble(input.getAttributeValue(null, "lon"));
                } else if (name.equals("tag")) {
                    var k = input.getAttributeValue(null, "k").replaceAll("\\s+", "").toLowerCase();
                    var v = input.getAttributeValue(null, "v").replaceAll("\\s+", "").toLowerCase();
                    //System.out.println(streetNoSpace);
                    if (k.equals("addr:housenumber") && v.equals(houseNumber)){
                        houseNumberMatch = true;
                    } else if (k.equals("addr:street") && v.equals(streetName)) {
                        streetMatch = true;
                    }
                    if (houseNumberMatch && streetMatch){
                        return id;
                    }
                }
            }
        }
        return -1;
    }

    public static class Builder {
        private String street, house, floor, side, postcode, city;
        public Builder street(String _street) {
            street = _street;
            return this;
        }
        public Builder house(String _house) {
            house = _house;
            return this;
        }
        public Builder floor(String _floor) {
            floor = _floor;
            return this;
        }
        public Builder side(String _side) {
            side = _side;
            return this;
        }
        public Builder postcode(String _postcode) {
            postcode = _postcode;
            return this;
        }
        public Builder city(String _city) {
            city = _city;
            return this;
        }
        public Address build() {
            return new Address(street, house, floor, side, postcode, city);
        }
    }
}