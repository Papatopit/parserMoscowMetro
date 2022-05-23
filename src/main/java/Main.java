import Metro.Line;
import Metro.Metro;
import Metro.Station;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Main {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static List<Station> stationSet = new ArrayList<>();                   // Коллекция станций
    private static final List<Line> lines = new LinkedList<>();
    public static List<List<Station>> connectionsList = new ArrayList<>();         // Переходы
    public static Metro metro;
    public static Document siteMetro;



    public static void main(String[] args) throws Exception {

        siteMetro = Jsoup.connect("https://www.moscowmap.ru/metro.html#lines").maxBodySize(0).get();
        Elements lineMetro = siteMetro.select(".js-metro-stations");


        getLineWithStation(lineMetro);
        getStation(lineMetro);
        getConnection(lineMetro);

        createJsonFile();
        JsonParser();

//        metro = new Metro(lines, stationSet, connectionsList);
//        String json = GSON.toJson(metro);
//        System.out.println(json);


    }

    public static String parseFile(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            List<String> lines = Files.readAllLines(Paths.get(path));
            lines.forEach(line -> stringBuilder.append(lines).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static void JsonParser() throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        try(Reader reader = new FileReader("src\\main\\resources\\metro.json")){
            jsonObject = (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray stations = null;
        if (jsonObject != null) {
            stations = (JSONArray) jsonObject.get("stations");
        }
        if (stations != null) {
            for (Object lineNum : stations) {
                JSONArray stationsArray = (JSONArray) stations;
                for (Line line : metro.getLines()) {

                        System.out.println("Линия " + line.getNumber() + " " + line.getName() +
                                " -> количество станций: " + stationsArray.size());

                }
            }
        }
    }

    public static void createJsonFile() throws IOException {

        metro = new Metro(lines, stationSet, connectionsList);
        try {
            FileWriter file = new FileWriter("src\\main\\resources\\metro.json");
            file.write(GSON.toJson(metro));
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getConnection(Elements lineMetro) {

        for (Element stationElement : lineMetro) {
            String fromNumLine = stationElement.attr("data-line");
            Elements stations = stationElement.select("p");

            for (Element st : stations) {
                String fromStationName = st.select("span[class=name]").text();
                //System.out.printf("Линия:<%s>, Станция:<%s>%n", fromNumLine, fromStationName);

                Elements connections = st.select("span[class*=t-icon-metroln]");

                List<Station> connection = new ArrayList<>();
                Station fromStation = new Station(fromNumLine, fromStationName);
                if (connections.isEmpty()){
                    continue;
                }
                connection.add(fromStation);

                for (Element con : connections) {
                        connection.add(getConnectedStation(con));
                    }
                //connection.add(fromStation);

//                connections.stream()
//                        .map(Main::getConnectedStation)
//                        .forEach(connection::add);

                    connectionsList.add(connection);
            }
        }
    }
    private static Station getConnectedStation(Element element) {
        String[] split = element.attr("class").split("-");
        String numCon = split[3];

        String[] split1 = element.attr("title").split("[\\«,\\»]");
        String nameCon = split1[1];
        return new Station(numCon, nameCon);
    }


    public static List<Line> getLineWithStation(Elements lineMetro) {
        Elements name = siteMetro.getElementsByAttributeValueStarting("class", "js-metro-line t-metrostation-list-header t-icon-metroln ln");
        for (Element lineElement : name) {

            String nameLine = lineElement.text();
            String numLine = lineElement.attr("data-line");
            Line line = new Line(numLine, nameLine);
            //lineSet.add(line);
            lines.add(line);
        }
        return lines;
    }

    public static List<Station> getStation(Elements lineMetro) {

        for (Element stationElement : lineMetro) {
            String numLine = stationElement.attr("data-line");
            Elements stations = stationElement.select(".js-metro-stations span[class=name]");
            for (Element st : stations) {
                String string = st.text();
                Station station = new Station(numLine, string);
                stationSet.add(station);


            }
        }
        return stationSet;
    }
}










