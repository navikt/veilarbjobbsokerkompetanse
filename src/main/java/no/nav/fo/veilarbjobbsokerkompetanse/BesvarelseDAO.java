package no.nav.fo.veilarbjobbsokerkompetanse;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class BesvarelseDAO {

    private static final String JOBBSOKERKOMPETANSE = "JOBBSOKERKOMPETANSE";

    private JdbcTemplate database;

    @Inject
    public BesvarelseDAO(JdbcTemplate database) {
        this.database = database;
    }

    public BesvarelseData opprettBesvarelse(BesvarelseData besvarelseData) {
        long id = getNextUniqueJobbsokerkompetanseId();
        database.update("INSERT INTO " + JOBBSOKERKOMPETANSE + "(ID, AKTOR_ID, LAGRET_TIDSPUNKT, BESVARELSE, RAAD) VALUES(?, ?, ?, ?, ?)",
            id,
            besvarelseData.getAktorId(),
            Timestamp.valueOf(LocalDateTime.now()),
            besvarelseData.getBesvarelse(),
            besvarelseData.getRaad());

        return hentBesvarelse(id);
    }

    public BesvarelseData hentBesvarelse(long id) {
        return database.queryForObject(
            "SELECT * FROM " + JOBBSOKERKOMPETANSE + " WHERE ID = ?",
            (resultSet, i) -> BesvarelseDataRowMapper.mapBesvarelse(resultSet),
            id
        );
    }

    public List<BesvarelseData> hentBesvarelserForAktorId(String aktorId) {
        return database.query(
            "SELECT * FROM " + JOBBSOKERKOMPETANSE + " WHERE AKTOR_ID = ?",
            (resultSet, i) -> BesvarelseDataRowMapper.mapBesvarelse(resultSet),
            aktorId
        );
    }

    private long getNextUniqueJobbsokerkompetanseId() {
        return nesteFraSekvens("JOBBSOKERKOMPETANSE_ID_SEQ");
    }

    private long nesteFraSekvens(String sekvensNavn) {
        return database.queryForObject("select " + sekvensNavn + ".nextval from dual", Long.class);
    }
}
