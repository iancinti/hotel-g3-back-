package com.g3.hotel_g3_back.service.adapter.out;

import com.g3.hotel_g3_back.service.appliction.port.out.RetiveServiceByIdQueryRepository;
import com.g3.hotel_g3_back.service.domain.Service;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RetriveServiceByIdJdbcAdapter implements RetiveServiceByIdQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_SERVICE_BY_ID_SQL = "SELECT id, name, description, price FROM service WHERE id = ?";

    public RetriveServiceByIdJdbcAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Service execute(String id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_SERVICE_BY_ID_SQL, new ServiceRowMapper(), id);
        } catch (DataAccessException e) {
            System.out.println("Error al recuperar el servicio: " + e.getMessage());
            return null;
        }
    }

    private static class ServiceRowMapper implements RowMapper<Service> {
        @Override
        public Service mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Service(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDouble("price")
            );
        }
    }
}
