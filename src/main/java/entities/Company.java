package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name = "company")
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column (name = "name")
    private String name;
    
    @Column (name = "description")
    private String description;
    
    @Column (name = "cvr")
    private int cvr;
    
    @Column (name = "numofemployees")
    private int NumOfEmployees;
    
    @Column (name = "marketvalue")
    private int marketValue;

    public Company() {
    }

    public Company(String name, String description, int cvr, int NumOfEmployees) {
        this.name = name;
        this.description = description;
        this.cvr = cvr;
        this.NumOfEmployees = NumOfEmployees;
        this.marketValue = calcMarketValue();
    }
    
    public final int calcMarketValue() {
        // random value
        return this.NumOfEmployees * 15550;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCvr() {
        return cvr;
    }

    public void setCvr(int cvr) {
        this.cvr = cvr;
    }

    public int getNumOfEmployees() {
        return NumOfEmployees;
    }

    public void setNumOfEmployees(int NumOfEmployees) {
        this.NumOfEmployees = NumOfEmployees;
    }

    public int getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(int marketValue) {
        this.marketValue = marketValue;
    }
    
}
