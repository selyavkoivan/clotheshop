package by.bsuir.clotheshop.model.entities.user;


import by.bsuir.clotheshop.model.entities.address.Address;
import by.bsuir.clotheshop.model.entities.dto.UserDto;
import by.bsuir.clotheshop.model.entities.user.gender.Gender;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import by.bsuir.clotheshop.model.tables.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cardId;

    @Column
    private String cardNumber;
    @Column
    private String expiration;
    @Column
    private int cvv;



}