package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String FIND_MANY = "SELECT * FROM GENRES";

    private static final String FIND_ONE_QUERY = "SELECT * FROM GENRES WHERE GENRE_ID = ?";


    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> findAllGenres() {
        return super.findMany(FIND_MANY);
    }

    public Optional<Genre> findOneGenre(Long id) {
        return super.findOne(FIND_ONE_QUERY, id);
    }
}
