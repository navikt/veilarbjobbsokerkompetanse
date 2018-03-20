package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.SvarAlternativ;
import no.nav.sbl.jdbc.Database;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.List;

@Component
public class SvarAlternativDao {

    @Inject
    private Database db;

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

    public List<SvarAlternativ> fetchBySvarId(long svarId) {
        return db.query("SELECT * FROM SVARALTERNATIV WHERE svar_id = ?",
                this::map,
                svarId
        );
    }

    @SneakyThrows
    public SvarAlternativ map(ResultSet rs) {
        return SvarAlternativ.builder()
                .svarAlternativId(rs.getLong("svaralternativ_id"))
                .svarId(rs.getLong("svar_id"))
                .svarAlternativKey(rs.getString("svaralternativ_key"))
                .svarAlternativ(rs.getString("svaralternativ"))
                .build();
    }
}
