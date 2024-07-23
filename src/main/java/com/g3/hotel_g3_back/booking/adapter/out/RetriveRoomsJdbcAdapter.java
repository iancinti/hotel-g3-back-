package com.g3.hotel_g3_back.booking.adapter.out;

import com.g3.hotel_g3_back.booking.application.port.out.RetriveRoomsRepository;
import com.g3.hotel_g3_back.booking.domain.Room;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
@Component
public class RetriveRoomsJdbcAdapter implements RetriveRoomsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RetriveRoomsJdbcAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Room> execute(int pageNumber, int pageSize, List<String> types) {
        pageNumber = Math.max(1, pageNumber);
        pageSize = Math.max(1, pageSize);

        int offset = (pageNumber - 1) * pageSize;

        StringBuilder sql = new StringBuilder(
                "SELECT r.id_room, r.description, r.number_people, r.price, rt.name as room_type_name " +
                        "FROM room r LEFT JOIN room_type rt ON r.id_room_type = rt.id_room_type WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        if (types != null && !types.isEmpty()) {
            sql.append(" AND rt.name IN (");
            for (int i = 0; i < types.size(); i++) {
                sql.append("?");
                if (i < types.size() - 1) {
                    sql.append(", ");
                }
                params.add(types.get(i).toUpperCase());
            }
            sql.append(")");
        }

        sql.append(" LIMIT ? OFFSET ?");
        params.add(pageSize);
        params.add(offset);

        return jdbcTemplate.query(sql.toString(), new RoomRowMapper(), params.toArray());
    }

    private static class RoomRowMapper implements RowMapper<Room> {
        public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
            Room room = new Room();
            room.setIdRoom(rs.getInt("id_room"));
            room.setName(rs.getString("description"));
            room.setNumberPeople(rs.getInt("number_people"));
            room.setPrice(rs.getDouble("price"));
            room.setType(rs.getString("room_type_name"));
            return room;
        }
    }
}
