package ru.yandex.practicum.filmorate.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;


@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM FILMS";

    private static final String FIND_ONE_QUERY = "SELECT * FROM FILMS " +
            "WHERE FILM_ID = ?";

    private static final String INSERT_QUERY = "INSERT INTO FILMS (NAME, " +
            "RELEASE_DATE, MPA_ID, DURATION, DESCRIPTION)" +
            "VALUES (?, ?, ?, ?, ?)";

    private static final String SELECT_FOR_ID_QUERY = "SELECT MAX(FILM_ID) " +
            "FROM FILMS ";
    private static final String UPDATE_QUERY = "UPDATE FILMS SET " +
            "NAME = ?," +
            "RELEASE_DATE = ?, " +
            "MPA_ID = ?, " +
            "DURATION = ?, " +
            "DESCRIPTION = ? " +
            "WHERE FILM_ID = ?";

    private static final String INSERT_LIKE_QUERY = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) " +
            "VALUES(?, ?)";

    private static final String DELETE_LIKE_QUERY = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? " +
            "AND USER_ID = ?";

    private static final String FIND_MOST_POPULAR = """
            WITH prep AS (
                         SELECT FILM_ID,
                                count(*) AS cnt
                           FROM FILM_LIKES
                          GROUP BY FILM_ID
                         )
            SELECT f.*\s
              FROM FILMS f
              left JOIN prep
                ON prep.FILM_ID = f.FILM_ID\s
             ORDER BY prep.cnt DESC\s
             LIMIT ?
            """;

    private static final String FIND_MPA = "SELECT * FROM MPA WHERE MPA_ID = ?";

    private static final String DELETE_FILM_GENRES = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";

    private static final String INSERT_FILM_GENRES = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) " +
            "VALUES (?, ?)";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }


    public Collection<Film> findAllFilms() {
        return super.findMany(FIND_ALL_QUERY);
    }


    public Film createFilm(Film film) {
        super.insert(
                INSERT_QUERY,
                film.getName(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getDuration(),
                film.getDescription()
        );
        long id = jdbc.queryForObject(SELECT_FOR_ID_QUERY, Long.class);
        film.setId(id);
        LinkedHashSet<Genre> genres = film.getGenres();
        super.delete(DELETE_FILM_GENRES, film.getId());
        for (Genre genre : genres) {
            super.insert(INSERT_FILM_GENRES, film.getId(), genre.getId());
        }
        return film;
    }


    public Film updateFilm(Film film) {
        Long mpa;
        if (film.getMpa() == null) {
            mpa = null;
        } else {
            mpa = film.getMpa().getId();
        }
        super.update(UPDATE_QUERY,
                film.getName(),
                film.getReleaseDate(),
                mpa,
                film.getDuration(),
                film.getDescription(),
                film.getId());
        LinkedHashSet<Genre> genres = film.getGenres();
        super.delete(DELETE_FILM_GENRES, film.getId());
        for (Genre genre : genres) {
            super.insert(INSERT_FILM_GENRES, film.getId(), genre.getId());
        }
        return film;
    }


    public Optional<Film> getFilm(Long filmId) {
        return super.findOne(FIND_ONE_QUERY, filmId);
    }


    public void addLike(Long filmId, Long userId) {
        super.insert(INSERT_LIKE_QUERY, filmId, userId);
    }


    public void deleteLike(Long filmId, Long userId) {
        super.delete(DELETE_LIKE_QUERY, filmId, userId);
    }


    public List<Film> topFilms(int count) {
        Comparator<Film> comparator = Comparator.comparing(film -> film.getLikes().size(), Comparator.reverseOrder());
        return super.findMany(FIND_MOST_POPULAR, count).stream().sorted(comparator).toList();
    }
}
