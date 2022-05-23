package Metro;

import com.google.gson.annotations.SerializedName;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Metro {

    @SerializedName("lines")

    private List<Line> lines;
    @SerializedName("stations")
    private List<Station> stationSet;
    @SerializedName("connections")
    private List<List<Station>> connectionsList;

    public Metro(List<Line> lines, List<Station> stationSet, List<List<Station>> connectionsList){
        this.lines = lines;
        this.stationSet = stationSet;
        this.connectionsList = connectionsList;


    }
    public List<Line> getLines() {
        return lines;
    }
    public List<Station> getStations(){
        return stationSet;
    }
    public  List<List<Station>> getConnections(){
        return connectionsList;
    }

}
