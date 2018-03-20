package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.SvarAlternativ;
import no.nav.sbl.jdbc.Database;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.List;

@Component
class SvarAlternativDao {

    @Inject
    private Database db;

    void create(SvarAlternativ svarAlternativ, long svarId) {
        long svarAlternativId = db.nesteFraSekvens("SVARALTERNATIV_SEQ");
        db.update("INSERT INTO SVARALTERNATIV (" +
                        "svaralternativ_id, " +
                        "svar_id, " +
                        "svaralternativ_key, " +
                        "svaralternativ) " +
                        "VALUES (?, ?, ?, ?)",
                svarAlternativId,
                svarId,
                svarAlternativ.getSvarAlternativKey(),
                svarAlternativ.getSvarAlternativ()
        );
    }

    List<SvarAlternativ> fetchBySvarId(long svarId) {
        return db.query("SELECT * FROM SVARALTERNATIV WHERE svar_id = ?",
                this::map,
                svarId
        );
    }

    @SneakyThrows
    private SvarAlternativ map(ResultSet rs) {
        return SvarAlternativ.builder()
                .svarAlternativId(rs.getLong("svaralternativ_id"))
                .svarId(rs.getLong("svar_id"))
                .svarAlternativKey(rs.getString("svaralternativ_key"))
                .svarAlternativ(rs.getString("svaralternativ"))
                .build();
    }
}
