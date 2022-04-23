package by.bsuir.clotheshop.model.entities.user;


import by.bsuir.clotheshop.model.entities.address.Address;
import by.bsuir.clotheshop.model.tables.TableName;
import by.bsuir.clotheshop.model.entities.user.gender.Gender;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = TableName.USER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    @Column
    private String name;
    @Column
    private String surname;
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false,  unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @ManyToOne(cascade=CascadeType.ALL)
    private Address address;

    @Column(nullable = false)
    private boolean emailStatus;

    @Column(columnDefinition = "varchar(255) default 'NoData'")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    public User(UserForm userForm){
        username = userForm.getUsername();
        email = userForm.getEmail();
        password = userForm.getPassword();
    }
}