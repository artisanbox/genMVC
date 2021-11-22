package genMVC.mapper.dao;

import java.sql.PreparedStatement;

public interface Modify<T> {
    public T modify(PreparedStatement prep) throws Exception;
}
