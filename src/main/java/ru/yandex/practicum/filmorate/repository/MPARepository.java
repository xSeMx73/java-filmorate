package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MPARepository extends BaseRepository<MPA> {

    private static final String FIND_MANY_QUERY = "SELECT * FROM MPA";

    private static final String FIND_ONE_QUERY = "SELECT * FROM MPA WHERE MPA_ID = ?";

    public MPARepository(JdbcTemplate jdbc, RowMapper<MPA> mapper) {
        super(jdbc, mapper);
    }

    public Collection<MPA> findAllMPA() {
        return super.findMany(FIND_MANY_QUERY);
    }

    public Optional<MPA> findOneMPA(Long id) {
        return super.findOne(FIND_ONE_QUERY, id);
    }

}
