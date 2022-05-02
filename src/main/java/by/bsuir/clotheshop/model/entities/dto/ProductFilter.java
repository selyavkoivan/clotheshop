package by.bsuir.clotheshop.model.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductFilter {
    private double minPrice;
    private double maxPrice;
    private String type;
    private String material;
    private String color;
    private String size;
    private boolean hasPhoto;

    private String textForSearch;
}
