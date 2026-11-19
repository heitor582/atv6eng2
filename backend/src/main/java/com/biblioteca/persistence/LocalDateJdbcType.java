package com.biblioteca.persistence;

import java.sql.*;
import java.time.LocalDate;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.BasicBinder;
import org.hibernate.type.descriptor.jdbc.BasicExtractor;
import org.hibernate.type.descriptor.jdbc.DateJdbcType;

/** JDBC 4.2 preserva datas civis sem convertê-las em instantes/java.sql.Date. */
public class LocalDateJdbcType extends DateJdbcType {
    @Override
    public <X> ValueBinder<X> getBinder(JavaType<X> javaType) {
        return new BasicBinder<X>(javaType, this) {
            @Override
            protected void doBind(PreparedStatement statement, X value, int index,
                                  WrapperOptions options) throws SQLException {
                statement.setObject(index, javaType.unwrap(value, LocalDate.class, options), Types.DATE);
            }
            @Override
            protected void doBind(CallableStatement statement, X value, String name,
                                  WrapperOptions options) throws SQLException {
                statement.setObject(name, javaType.unwrap(value, LocalDate.class, options), Types.DATE);
            }
        };
    }

    @Override
    public <X> ValueExtractor<X> getExtractor(JavaType<X> javaType) {
        return new BasicExtractor<X>(javaType, this) {
            @Override
            protected X doExtract(ResultSet result, int index, WrapperOptions options) throws SQLException {
                return javaType.wrap(result.getObject(index, LocalDate.class), options);
            }
            @Override
            protected X doExtract(CallableStatement statement, int index,
                                  WrapperOptions options) throws SQLException {
                return javaType.wrap(statement.getObject(index, LocalDate.class), options);
            }
            @Override
            protected X doExtract(CallableStatement statement, String name,
                                  WrapperOptions options) throws SQLException {
                return javaType.wrap(statement.getObject(name, LocalDate.class), options);
            }
        };
    }
}
