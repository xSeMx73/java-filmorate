package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private static final MPARowMapper MPA_ROW_MAPPER = new MPARowMapper();
    private static final GenreRowMapper GENRE_ROW_MAPPER = new GenreRowMapper();
    private static final String FIND_MPA_RATING = "SELECT * FROM MPA where MPA_ID = ?";
    private static final String FIND_GENRES = "SELECT GENRE_ID FROM FILM_GENRES where FILM_ID = ?";
    private static final String FIND_GENRE = "SELECT * FROM GENRES where GENRE_ID = ?";
    private static final String FIND_LIKES = "SELECT USER_ID FROM FILM_LIKES where FILM_ID = ?";
    protected final JdbcTemplate jdbc;

    public FilmRowMapper(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {

        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("NAME"));
        film.setMpa(jdbc.queryForObject(FIND_MPA_RATING, MPA_ROW_MAPPER, rs.getLong("MPA_ID")));
        List<Long> genres = jdbc.query(FIND_GENRES, (rs1, rowNum1) -> {
            long num = rs1.getLong("GENRE_ID");
            return num;
        }, film.getId());
        for (Long id : genres) {
            film.getGenres().add(jdbc.queryForObject(FIND_GENRE, GENRE_ROW_MAPPER, id));
        }
        List<Long> likeIds = jdbc.query(FIND_LIKES, (rs1, rowNum1) -> {
            long num = rs1.getLong("USER_ID");
            return num;
        }, film.getId());
        for (Long id : likeIds) {
            film.getLikes().add(id);
        }
        film.setDescription(rs.getString("DESCRIPTION"));
        film.setReleaseDate(rs.getDate("RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getLong("DURATION"));

        return film;
    }
}