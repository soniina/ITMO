package common.models;

public enum Country {
    RUSSIA ("РОССИЯ"),
    UNITED_KINGDOM ("ВЕЛИКОБРИТАНИЯ"),
    USA ("США"),
    NORTH_KOREA ("СЕВЕРНАЯ КОРЕЯ");

    private final String country;
    Country (String name) {
        this.country = name;
    }
    public String getCountry() {
        return country;
    }
}
