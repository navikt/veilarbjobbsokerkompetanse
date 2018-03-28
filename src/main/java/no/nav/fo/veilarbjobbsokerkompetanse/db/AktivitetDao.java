package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Aktivitet;
import no.nav.sbl.jdbc.Database;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.List;

public class AktivitetDao {

    @Inject
    private Database db;

    void create(Aktivitet aktivitet, long raadId) {
        long aktivitetId = db.nesteFraSekvens("AKTIVITET_SEQ");
        db.update("INSERT INTO AKTIVITET (" +
                "aktivitet_id, " +
                "raad_id, " +
                "tittel, " +
                "innhold) " +
                "VALUES (?, ?, ?, ?)",
            aktivitetId,
            raadId,
            aktivitet.getTittel(),
            aktivitet.getInnhold()
        );
    }

    public List<Aktivitet> fetchByRaadId(long raadId) {
        return db.query("SELECT * FROM AKTIVITET WHERE raad_id = ?",
            this::map,
            raadId
        );
    }

    @SneakyThrows
    private Aktivitet map(ResultSet rs) {
        return Aktivitet.builder()
                .aktivitetId(rs.getLong("aktivitet_id"))
                .raadId(rs.getLong("raad_id"))
                .tittel(rs.getString("tittel"))
                .innhold(rs.getString("innhold"))
                .build();
    }
}
