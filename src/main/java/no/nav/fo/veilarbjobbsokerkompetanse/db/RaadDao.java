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

    void create(Raad raad, long kartleggingId) {
        long raadId = db.nesteFraSekvens("RAAD_SEQ");
        db.update("INSERT INTO RAAD (" +
                        "raad_id, " +
                        "kartlegging_id, " +
                        "raad_key, " +
                        "raad_tittel, " +
                        "raad_ingress) " +
                        "VALUES (?, ?, ?, ?, ?)",
                raadId,
                kartleggingId,
                raad.getRaadKey(),
                raad.getRaadTittel(),
                raad.getRaadIngress()
        );
        raad.getRaadAktiviteter().forEach(ra -> aktivitetDao.create(ra, raadId));
    }

    List<Raad> fetchByKartleggingId(long kartleggingId) {
        List<Raad> raad = db.query("SELECT * FROM RAAD WHERE kartlegging_id = ?",
                this::map,
                kartleggingId
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
                .kartleggingId(rs.getLong("kartlegging_id"))
                .raadKey(rs.getString("raad_key"))
                .raadTittel(rs.getString("raad_tittel"))
                .raadIngress(rs.getString("raad_ingress"))
                .build();
    }

}
