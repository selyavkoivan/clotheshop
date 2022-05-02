package by.bsuir.clotheshop.model.entities.goods;

import by.bsuir.clotheshop.model.entities.address.Address;
import by.bsuir.clotheshop.model.entities.user.User;
import by.bsuir.clotheshop.model.tables.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableName.ORDER)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int productInCartId;

    @ManyToOne
    protected Product product;

    @ManyToOne
    protected Size size;

    @Column
    protected int count;

    @ManyToOne
    protected User user;

    @ManyToOne
    protected Address address;

    @Column
    protected boolean delivery;

    @Column
    protected int status;

    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date date;

    @Transient
    private boolean rememberCard;

    @Transient
    private boolean rememberAddress;

    public Address generateAddress() {
        var address = new Address();
        address.setCountry("Беларусь");
        address.setRegion("Минская");
        address.setCity("Минск");
        address.setStreet("Якуба Коласа");
        address.setHomeNumber("53/3");
        address.setApartmentNumber((short) 83);
        return address;
    }

    public double getTotalPrice(){
        return product.getPrice()*count;
    }
}
