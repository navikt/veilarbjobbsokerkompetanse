package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Raad;
import no.nav.sbl.jdbc.Database;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.List;

@Component
class RaadDao {

    @Inject
    private Database db;

    void create(Raad raad, long besvarelseId) {
        long raadId = db.nesteFraSekvens("RAAD_SEQ");
        db.update("INSERT INTO RAAD (" +
                        "raad_id, " +
                        "besvarelse_id, " +
                        "raad_key, " +
                        "raad) " +
                        "VALUES (?, ?, ?)",
                raadId,
                besvarelseId,
                raad.getRaadKey(),
                raad.getRaad()
        );
    }

    List<Raad> fetchByBesvarelseId(long besvarelseId) {
        return db.query("SELECT * FROM RAAD WHERE besvarelse_id = ?",
                this::map,
                besvarelseId
        );
    }

    @SneakyThrows
    private Raad map(ResultSet rs) {
        return Raad.builder()
                .raadId(rs.getLong("raad_id"))
                .besvarelseId(rs.getLong("besvarelse_id"))
                .raadKey(rs.getString("raad_key"))
                .raad(rs.getString("raad"))
                .build();
    }

}
