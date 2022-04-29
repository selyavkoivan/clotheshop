package by.bsuir.clotheshop.model.entities.goods;

import by.bsuir.clotheshop.model.tables.TableName;
import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableName.MATERIAL)
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int materialId;
    @Column
    protected String material;
    @Column
    protected String color;
    @Column
    protected String pattern;
}
