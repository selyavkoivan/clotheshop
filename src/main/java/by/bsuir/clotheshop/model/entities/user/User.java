package by.bsuir.clotheshop.model.entities.user;


import by.bsuir.clotheshop.model.entities.address.Address;
import by.bsuir.clotheshop.model.entities.user.role.Role;
import by.bsuir.clotheshop.model.tables.TableName;
import by.bsuir.clotheshop.model.entities.user.gender.Gender;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.*;
import java.util.*;

import static org.springframework.test.context.transaction.TestTransaction.isActive;

@Entity
@Table(name = TableName.USER)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
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


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Role> roles;

    @Column(nullable = false,
            columnDefinition = "varchar(255) default 'https://avatarko.ru/img/kartinka/1/avatarko_anonim.jpg'")
    private String avatarUrl;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}