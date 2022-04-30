package by.bsuir.clotheshop.model.entities.goods;

import by.bsuir.clotheshop.model.entities.address.Address;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import by.bsuir.clotheshop.model.tables.TableName;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = TableName.PRODUCT)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    @Column(nullable = false, unique = true)
    private String name;
    @Column
    private String description;
    @Column
    private double price;
    @Column
    private String type;

    @ManyToOne(cascade=CascadeType.ALL)
    private Material material;

    @ElementCollection
    @CollectionTable(name="photoUrl", joinColumns=@JoinColumn(name="product_id"))
    @Column(name="photoUrl")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<String> photoUrls;

    @Column
    private String mainPhoto;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Size> sizes;

    public void addSize(Size size)
    {
       sizes.add(size);
    }

    public void addPhotoUrl(String url)
    {
        photoUrls.add(url);
    }
}
