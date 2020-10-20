package dto;

import entities.CityInfo;

public class CityInfoDTO {
    
    private int zipCode;
    private String city;

    public CityInfoDTO(CityInfo cityInfo) {
        this.zipCode = cityInfo.getZipCode();
        this.city = cityInfo.getCity();
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
}
