package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Raad;
import no.nav.sbl.jdbc.Database;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.List;

@Component
public class RaadDao {

    @Inject
    private Database db;

    public void create(Raad raad) {
        raad.toBuilder().raadId(db.nesteFraSekvens("RAAD_SEQ")).build();
        db.update("INSERT INTO RAAD (" +
                        "raad_id, " +
                        "besvarelse_id, " +
                        "raad) " +
                        "VALUES (?, ?, ?)",
                raad.getRaadId(),
                raad.getBesvarelseId(),
                raad.getRaad()
        );
    }

    public List<Raad> fetchByBesvarelseId(long besvarelseId) {
        return db.query("SELECT * FROM RAAD WHERE besvarelse_id = ?",
                this::map,
                besvarelseId
        );
    }

    @SneakyThrows
    public Raad map(ResultSet rs) {
        return Raad.builder()
                .raadId(rs.getLong("raad_id"))
                .besvarelseId(rs.getLong("besvarelse_id"))
                .raad(rs.getString("raad"))
                .build();
    }

}
