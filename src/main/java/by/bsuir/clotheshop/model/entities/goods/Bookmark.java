package by.bsuir.clotheshop.model.entities.goods;

import by.bsuir.clotheshop.model.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookmarkId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;
}
