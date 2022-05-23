package Metro;

public class Station {

    private String numLine;
    private String name;

    public Station(String numLine, String name) {
        this.numLine = numLine;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public String getNumLine(){
        return numLine;
    }
//    public String toString(){
//        return numLine + " : " + name;
//    }

}
