package com.knowhouse.thereceiptbook.UtitlityClasses;

import android.graphics.Bitmap;

public class WeatherClass {
    //Declaration of the values for the weather cardview
    private long date;
    private String town;
    private double humidity;
    private double pressure;
    private double wind;
    private String icon;
    private double temperature;
    private double feelsLike;
    private String cloud;


    public WeatherClass(long date, String town,
                        double humidity, double pressure,
                        double wind, String icon, double temperature,
                        double feelsLike, String cloud) {
        this.date = date;
        this.town = town;
        this.humidity = humidity;
        this.pressure = pressure;
        this.wind = wind;
        this.icon = icon;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.cloud = cloud;
    }

    public long getDate() {
        return date;
    }

    public String getTown() {
        return town;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getWind() {
        return wind;
    }

    public String getIcon() {
        return icon;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public String getCloud() {
        return cloud;
    }

}
