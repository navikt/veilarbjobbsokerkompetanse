package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.SvarAlternativ;
import no.nav.sbl.jdbc.Database;

import java.sql.ResultSet;
import java.util.List;

class SvarAlternativDao {

    private final Database db;

    SvarAlternativDao(Database db) {
        this.db = db;
    }

    void create(SvarAlternativ svarAlternativ, long besvarelseId) {
        long svarAlternativId = db.nesteFraSekvens("SVARALTERNATIV_SEQ");
        db.update("INSERT INTO SVARALTERNATIV (" +
                        "svaralternativ_id, " +
                        "besvarelse_id, " +
                        "svaralternativ_key, " +
                        "svaralternativ) " +
                        "VALUES (?, ?, ?, ?)",
                svarAlternativId,
                besvarelseId,
                svarAlternativ.getSvarAlternativKey(),
                svarAlternativ.getSvarAlternativ()
        );
    }

    List<SvarAlternativ> fetchByBesvarelseId(long besvarelseId) {
        return db.query("SELECT * FROM SVARALTERNATIV WHERE besvarelse_id = ?",
                this::map,
                besvarelseId
        );
    }

    @SneakyThrows
    private SvarAlternativ map(ResultSet rs) {
        return SvarAlternativ.builder()
                .svarAlternativId(rs.getLong("svaralternativ_id"))
                .besvarelseId(rs.getLong("besvarelse_id"))
                .svarAlternativKey(rs.getString("svaralternativ_key"))
                .svarAlternativ(rs.getString("svaralternativ"))
                .build();
    }
}
