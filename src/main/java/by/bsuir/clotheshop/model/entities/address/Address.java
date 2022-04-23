package by.bsuir.clotheshop.model.entities.address;

import by.bsuir.clotheshop.model.tables.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = TableName.ADDRESS)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int addressId;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String homeNumber;

    @Column
    private short apartmentNumber;

}
