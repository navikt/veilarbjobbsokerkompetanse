package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.apiapp.feil.Feil;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Besvarelse;
import no.nav.sbl.jdbc.Database;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.function.Supplier;

import static java.sql.Timestamp.from;
import static java.util.Comparator.comparing;

@Component
@Import({
        RaadDao.class,
        AktivitetDao.class,
        SvarDao.class,
        SvarAlternativDao.class
})
public class BesvarelseDao {

    private static final Supplier<Feil> INGEN_BESVARELSE =
            () -> new Feil(Feil.Type.FINNES_IKKE, "Ingen lagrede besvarelser for aktør");

    @Inject
    private Database db;

    @Inject
    private SvarDao svarDao;

    @Inject
    private RaadDao raadDao;

    @Transactional
    public void create(String aktorId, boolean underOppfolging, Besvarelse besvarelse) {
        long besvarelseId = db.nesteFraSekvens("BESVARELSE_SEQ");
        db.update("INSERT INTO BESVARELSE (" +
                        "besvarelse_id, " +
                        "aktor_id, " +
                        "under_oppfolging, " +
                        "besvarelse_dato) " +
                        "VALUES (?, ?, ?, ?)",
                besvarelseId,
                aktorId,
                underOppfolging,
                from(besvarelse.getBesvarelseDato())
        );

        besvarelse.getSvar().forEach(s -> svarDao.create(s, besvarelseId));
        besvarelse.getRaad().forEach(r -> raadDao.create(r, besvarelseId));
    }

    public Besvarelse fetchMostRecentByAktorId(String aktorId) {

        Besvarelse besvarelse = db.query("SELECT * FROM BESVARELSE WHERE aktor_id = ?",
                this::map,
                aktorId
        ).stream()
                .sorted(comparing(Besvarelse::getBesvarelseDato).reversed())
                .findFirst()
                .orElseThrow(INGEN_BESVARELSE);

        return besvarelse.toBuilder()
                .svar(svarDao.fetchByBesvarelseId(besvarelse.getBesvarelseId()))
                .raad(raadDao.fetchByBesvarelseId(besvarelse.getBesvarelseId()))
                .build();
    }

    @SneakyThrows
    private Besvarelse map(ResultSet rs) {
        return Besvarelse.builder()
                .besvarelseId(rs.getLong("besvarelse_id"))
                .aktorId(rs.getString("aktor_id"))
                .underOppfolging(rs.getBoolean("under_oppfolging"))
                .besvarelseDato(rs.getTimestamp("besvarelse_dato").toInstant())
                .build();
    }

}
