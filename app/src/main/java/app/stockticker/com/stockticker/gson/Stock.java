package app.stockticker.com.stockticker.gson;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by nitinraj.arvind on 8/15/2015.
 * For main home page request
 */
public class Stock implements Comparable<Stock>, Serializable, Parcelable {
    private static final long serialVersionUID = -425355L;

    public String symbol;

    public String Name;

    public float LastTradePriceOnly;
    public String LastTradeDate;
    public String ChangeinPercent;
    public String Change;
    public String AverageDailyVolume;
    public String DaysLow;
    public String DaysHigh;
    public String YearLow;
    public String YearHigh;
    public String MarketCapitalization;
    public String DaysRange;
    public String Volume;
    public String StockExchange;

    // Add Position fields
    public boolean IsPosition;
    public float PositionPrice;
    public float PositionShares;

    @Override
    public int compareTo(Stock another) {
        return Double.compare(getChangeFromPercentString(another.ChangeinPercent),
                getChangeFromPercentString(ChangeinPercent));
    }

    static double getChangeFromPercentString(String percentString) {
        if (percentString == null) {
            return -1000000.0d;
        }
        return Double.valueOf(percentString.replace('%', '\0').trim());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Stock) {
            if (symbol != null) {
                return symbol.equalsIgnoreCase(((Stock) o).symbol);
            }
        } else if (o instanceof String) {
            return ((String) o).equalsIgnoreCase(symbol);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return symbol != null ? symbol.hashCode() : super.hashCode();
    }

    @Override
    public String toString() {
        return symbol;
    }

    public Stock(Parcel in){
        readFromParcel(in);
    }

    /**
     * @hide
     * */
    public Stock(){
        super();
    }


    /**
     * @hide
     * */
    public static final Creator<Stock> CREATOR =
            new Creator<Stock>() {

                @Override
                public Stock createFromParcel(Parcel source) {
                    return new Stock(source);
                }

                @Override
                public Stock[] newArray(int size) {
                    return new Stock[size];
                }
            };


    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in){
        symbol = in.readString();

        Name = in.readString();

        LastTradePriceOnly = in.readFloat();
        LastTradeDate = in.readString();
        ChangeinPercent = in.readString();
        Change = in.readString();
        AverageDailyVolume = in.readString();
        DaysLow = in.readString();
        DaysHigh = in.readString();
        YearLow = in.readString();
        YearHigh = in.readString();
        MarketCapitalization = in.readString();
        DaysRange = in.readString();
        Volume = in.readString();
        StockExchange = in.readString();

        // Add Position fields
        IsPosition = (in.readInt()==1)?true:false;
        PositionPrice = in.readFloat();
        PositionShares = in.readFloat();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symbol);

        dest.writeString(Name);

        dest.writeFloat(LastTradePriceOnly);
        dest.writeString(LastTradeDate);
        dest.writeString(ChangeinPercent);
        dest.writeString(Change);
        dest.writeString(AverageDailyVolume);
        dest.writeString(DaysLow);
        dest.writeString(DaysHigh);
        dest.writeString(YearLow);
        dest.writeString(YearHigh);
        dest.writeString(MarketCapitalization);
        dest.writeString(DaysRange);
        dest.writeString(Volume);
        dest.writeString(StockExchange);

        // Add Position fields
        dest.writeInt(IsPosition ? 1 : 0);
        dest.writeFloat(PositionPrice);
        dest.writeFloat(PositionShares);
    }
}
