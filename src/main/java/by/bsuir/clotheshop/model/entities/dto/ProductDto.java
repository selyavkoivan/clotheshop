package by.bsuir.clotheshop.model.entities.dto;

import by.bsuir.clotheshop.model.entities.goods.Material;
import by.bsuir.clotheshop.model.entities.goods.Size;
import by.bsuir.clotheshop.model.tables.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDto {
    private int productId;
    private Size size;
}
