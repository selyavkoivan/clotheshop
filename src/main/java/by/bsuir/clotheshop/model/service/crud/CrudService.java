package by.bsuir.clotheshop.model.service.crud;

import java.util.List;

public interface CrudService<T, S> {
    S create(T t);
    List<T> read();
    S update(T t);
    S delete(T t);
}
