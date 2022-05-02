package by.bsuir.clotheshop.model.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderFilter {
    private double minPrice;
    private double maxPrice;
    private String type;
    private String material;
    private String color;
    private String size;
    private boolean hasPhoto;
    private int delivery;
    private int status;

    private boolean forAllUsers;

    private String textForSearch;

    public OrderFilter(boolean forAllUsers)
    {
        this.forAllUsers = forAllUsers;
    }
}
