package no.nav.fo.veilarbjobbsokerkompetanse;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class JobbsokerKartleggingDAO {

    private static final String JOBBSOKERKOMPETANSE = "JOBBSOKERKOMPETANSE";

    private JdbcTemplate database;

    @Inject
    public JobbsokerKartleggingDAO(JdbcTemplate database) {
        this.database = database;
    }

    public JobbsokerKartlegging opprettJobbsokerKartlegging(JobbsokerKartlegging jobbsokerKartlegging) {
        long id = getNextUniqueJobbsokerkompetanseId();
        database.update("INSERT INTO " + JOBBSOKERKOMPETANSE + "(ID, AKTOR_ID, LAGRET_TIDSPUNKT, BESVARELSE, RAAD) VALUES(?, ?, ?, ?, ?)",
            id,
            jobbsokerKartlegging.getAktorId(),
            Timestamp.valueOf(LocalDateTime.now()),
            jobbsokerKartlegging.getBesvarelse(),
            jobbsokerKartlegging.getRaad());

        return hentJobbsokerKartlegging(id);
    }

    public JobbsokerKartlegging hentJobbsokerKartlegging(long id) {
        return database.queryForObject(
            "SELECT * FROM " + JOBBSOKERKOMPETANSE + " WHERE ID = ?",
            (resultSet, i) -> JobbsokerKartleggingRowMapper.mapJobbsokerKartlegging(resultSet),
            id
        );
    }

    public List<JobbsokerKartlegging> hentJobbsokerKartleggingerForAktorId(String aktorId) {
        return database.query(
            "SELECT * FROM " + JOBBSOKERKOMPETANSE + " WHERE AKTOR_ID = ?",
            (resultSet, i) -> JobbsokerKartleggingRowMapper.mapJobbsokerKartlegging(resultSet),
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
