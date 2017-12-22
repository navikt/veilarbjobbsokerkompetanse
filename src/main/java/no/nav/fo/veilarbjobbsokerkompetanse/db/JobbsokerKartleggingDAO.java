package no.nav.fo.veilarbjobbsokerkompetanse.db;

import no.nav.fo.veilarbjobbsokerkompetanse.JobbsokerKartlegging;
import no.nav.sbl.sql.SqlUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class JobbsokerKartleggingDAO {

    public static final String JOBBSOKERKARTLEGGING = "JOBBSOKERKARTLEGGING";
    public static final String ID = "ID";
    public static final String AKTOR_ID = "AKTOR_ID";
    public static final String LAGRET_TIDSPUNKT = "LAGRET_TIDSPUNKT";
    public static final String BESVARELSE = "BESVARELSE";
    public static final String RAAD = "RAAD";

    private JdbcTemplate database;

    @Inject
    public JobbsokerKartleggingDAO(JdbcTemplate database) {
        this.database = database;
    }

    public JobbsokerKartlegging opprettJobbsokerKartlegging(JobbsokerKartlegging jobbsokerKartlegging) {
        long id = getNextUniqueJobbsokerkompetanseId();
        SqlUtils.insert(database, JOBBSOKERKARTLEGGING)
            .value(ID, id)
            .value(AKTOR_ID, jobbsokerKartlegging.getAktorId())
            .value(LAGRET_TIDSPUNKT, jobbsokerKartlegging.getLagretTidspunkt())
            .value(BESVARELSE, jobbsokerKartlegging.getBesvarelse())
            .value(RAAD, jobbsokerKartlegging.getRaad())
            .execute();

        return hentJobbsokerKartlegging(id);
    }

    private JobbsokerKartlegging hentJobbsokerKartlegging(long id) {
        return database.queryForObject(
            "SELECT * FROM " + JOBBSOKERKARTLEGGING + " WHERE " + ID + " = ?",
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
            "SELECT * FROM " + JOBBSOKERKARTLEGGING + " WHERE " + AKTOR_ID + " = ?",
            (resultSet, i) -> JobbsokerKartleggingRowMapper.mapJobbsokerKartlegging(resultSet),
            aktorId
        );
    }

    long getNextUniqueJobbsokerkompetanseId() {
        return nesteFraSekvens("JOBBSOKERKARTLEGGING_ID_SEQ");
    }

    private long nesteFraSekvens(String sekvensNavn) {
        return database.queryForObject("select " + sekvensNavn + ".nextval from dual", Long.class);
    }
}
