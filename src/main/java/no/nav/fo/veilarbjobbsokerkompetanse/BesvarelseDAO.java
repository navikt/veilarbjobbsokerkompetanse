package no.nav.fo.veilarbjobbsokerkompetanse;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;

public class BesvarelseDAO {

    JdbcTemplate database;

    @Inject
    public BesvarelseDAO(JdbcTemplate database) {
        this.database = database;
    }

    private long getNextUniqueJobbsokerkompetanseId() {
        return nesteFraSekvens("JOBBSOKERKOMPETANSE_ID_SEQ");
    }

    private long nesteFraSekvens(String sekvensNavn) {
        return database.queryForObject("select " + sekvensNavn + ".nextval from dual", Long.class);
    }
}
