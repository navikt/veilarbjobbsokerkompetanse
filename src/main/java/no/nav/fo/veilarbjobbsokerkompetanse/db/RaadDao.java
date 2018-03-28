package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Raad;
import no.nav.sbl.jdbc.Database;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
class RaadDao {

    @Inject
    private Database db;

    @Inject
    private AktivitetDao aktivitetDao;

    void create(Raad raad, long besvarelseId) {
        long raadId = db.nesteFraSekvens("RAAD_SEQ");
        db.update("INSERT INTO RAAD (" +
                        "raad_id, " +
                        "besvarelse_id, " +
                        "raad_key, " +
                        "raad_tittel, " +
                        "raad_ingress) " +
                        "VALUES (?, ?, ?, ?, ?)",
                raadId,
                besvarelseId,
                raad.getRaadKey(),
                raad.getRaadTittel(),
                raad.getRaadIngress()
        );
        raad.getRaadAktiviteter().forEach(ra -> aktivitetDao.create(ra, raadId));
    }

    List<Raad> fetchByBesvarelseId(long besvarelseId) {
        List<Raad> raad = db.query("SELECT * FROM RAAD WHERE besvarelse_id = ?",
                this::map,
                besvarelseId
        );
        return raad.stream()
                .map(s -> s.toBuilder()
                    .raadAktiviteter(aktivitetDao.fetchByRaadId(s.getRaadId()))
                    .build())
                .collect(toList());
    }

    @SneakyThrows
    private Raad map(ResultSet rs) {
        return Raad.builder()
                .raadId(rs.getLong("raad_id"))
                .besvarelseId(rs.getLong("besvarelse_id"))
                .raadKey(rs.getString("raad_key"))
                .raadTittel(rs.getString("raad_tittel"))
                .raadIngress(rs.getString("raad_ingress"))
                .build();
    }

}
