package dto;

import entities.Hobby;

public class HobbyDTO {
    
    private String name;
    private String category;
    private String type;
    
    public HobbyDTO(Hobby hobby) {
        this.name = hobby.getName();
        this.category = hobby.getCategory();
        this.type = hobby.getType();
    }
    
}
