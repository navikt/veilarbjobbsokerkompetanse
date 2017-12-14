package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
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
            jobbsokerKartlegging.getLagretTidspunkt(),
            jobbsokerKartlegging.getBesvarelse(),
            jobbsokerKartlegging.getRaad());

        return hentJobbsokerKartlegging(id);
    }

    private JobbsokerKartlegging hentJobbsokerKartlegging(long id) {
        return database.queryForObject(
            "SELECT * FROM " + JOBBSOKERKOMPETANSE + " WHERE ID = ?",
            (resultSet, i) -> JobbsokerKartleggingRowMapper.mapJobbsokerKartlegging(resultSet),
            id
        );
    }

    public JobbsokerKartlegging hentNyesteJobbsokerKartlegging(String aktorId) {
        List<JobbsokerKartlegging> jobbsokerKartlegginger = hentJobbsokerKartlegginger(aktorId);
        return Collections.max(jobbsokerKartlegginger, new Comparator<JobbsokerKartlegging>() {
            @Override
            public int compare(JobbsokerKartlegging o1, JobbsokerKartlegging o2) {
                return o1.getLagretTidspunkt().compareTo(o2.getLagretTidspunkt());
            }
        });
    }

    List<JobbsokerKartlegging> hentJobbsokerKartlegginger(String aktorId) {
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
