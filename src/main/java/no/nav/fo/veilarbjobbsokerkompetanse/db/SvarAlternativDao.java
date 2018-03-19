package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.SvarAlternativ;
import no.nav.sbl.jdbc.Database;

import javax.inject.Inject;
import java.sql.ResultSet;

public class SvarAlternativDao implements Dao<SvarAlternativ> {

    @Inject
    private Database db;

    @Override
    public void create(SvarAlternativ svarAlternativ) {
        svarAlternativ.toBuilder().svarAlternativId(db.nesteFraSekvens("SVARALTERNATIV_SEQ")).build();
        db.update("INSERT INTO SVARALTERNATIV (" +
                        "svaralternativ_id, " +
                        "svar_id, " +
                        "svaralternativ_key, " +
                        "svaralternativ) " +
                        "VALUES (?, ?, ?, ?)",
                svarAlternativ.getSvarAlternativId(),
                svarAlternativ.getSvarId(),
                svarAlternativ.getSvarAlternativKey(),
                svarAlternativ.getSvarAlternativ()
        );
    }

    @Override
    public SvarAlternativ fetch(long id) {
        return db.queryForObject("SELECT * FROM SVARALTERNATIV WHERE svaralternativ_id = ?",
                this::map,
                id
        );
    }

    @SneakyThrows
    @Override
    public SvarAlternativ map(ResultSet rs) {
        return SvarAlternativ.builder()
                .svarAlternativId(rs.getLong("svaralternativ_id"))
                .svarId(rs.getLong("svar_id"))
                .svarAlternativKey(rs.getString("svaralternativ_key"))
                .svarAlternativ(rs.getString("svaralternativ"))
                .build();
    }
}
