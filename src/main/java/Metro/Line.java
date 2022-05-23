package Metro;

import org.json.simple.JSONObject;

import java.util.List;
import java.util.Objects;

public class Line {
    private String number;
    private String name;

    public Line(String number,String name){
        this.number =number;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String toString() {
        return number + " : " + name;
    }
}
