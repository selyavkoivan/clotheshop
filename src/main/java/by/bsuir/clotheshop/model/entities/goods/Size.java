package by.bsuir.clotheshop.model.entities.goods;

import by.bsuir.clotheshop.model.tables.TableName;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableName.SIZE)
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sizeId;
    @Column
    private String size;
    @Column
    private int count;
}
