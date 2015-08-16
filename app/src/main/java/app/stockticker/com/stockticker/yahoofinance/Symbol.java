package app.stockticker.com.stockticker.yahoofinance;

/**
 * Created by nitinraj.arvind on 8/14/2015.
 * For making query building easy
 */
public class Symbol {

    String name;

    public Symbol(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\"" + name + "\"" ;
    }
}
