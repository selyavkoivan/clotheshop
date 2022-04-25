package by.bsuir.clotheshop.model.service.crud;

import by.bsuir.clotheshop.model.entities.user.User;

public interface CrudService<T, S> {
    S create(T t);
    Iterable<User> read();
    S update(T t);
    S delete(T t);
}
