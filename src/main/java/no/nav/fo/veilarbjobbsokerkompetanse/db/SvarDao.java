package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Svar;
import no.nav.sbl.jdbc.Database;

import javax.inject.Inject;
import java.sql.ResultSet;

public class SvarDao implements Dao<Svar> {

    @Inject
    private Database db;

    @Override
    public void create(Svar svar) {
        svar.toBuilder().svarId(db.nesteFraSekvens("SVAR_SEQ"));
        db.update("INSERT INTO SVAR (" +
                        "svar_id, " +
                        "besvarelse_id, " +
                        "sporsmal_key, " +
                        "sporsmal, " +
                        "tips_key, " +
                        "tips) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                svar.getSvarId(),
                svar.getBesvarelseId(),
                svar.getSporsmalKey(),
                svar.getSporsmal(),
                svar.getTipsKey(),
                svar.getTips()
        );
    }

    @Override
    public Svar fetch(long id) {
        return db.queryForObject("SELECT * FROM SVAR WHERE svar_id = ?",
                this::map,
                id
        );
    }

    @SneakyThrows
    @Override
    public Svar map(ResultSet rs) {
        return Svar.builder()
                .svarId(rs.getLong("svar_id"))
                .besvarelseId(rs.getLong("besvarelse_id"))
                .sporsmalKey(rs.getString("sporsmal_key"))
                .sporsmal(rs.getString("sporsmal"))
                .tipsKey(rs.getString("tips_key"))
                .tips(rs.getString("tips"))
                .build();
    }

}
