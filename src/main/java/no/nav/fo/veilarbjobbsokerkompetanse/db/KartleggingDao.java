package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import no.nav.sbl.jdbc.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.function.Supplier;

import static java.util.Comparator.comparing;

@Component
@Import({
    RaadDao.class,
    AktivitetDao.class,
    BesvarelseDao.class,
    SvarAlternativDao.class,
    KulepunktDao.class,
})
public class KartleggingDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(KartleggingDao.class);

    private static final Supplier<WebApplicationException> INGEN_BESVARELSE =
        () -> new WebApplicationException("Ingen lagrede besvarelser for aktør", Response.Status.NO_CONTENT);

    @Inject
    private Database db;

    @Inject
    private BesvarelseDao besvarelseDao;

    @Inject
    private RaadDao raadDao;

    @Inject
    private KulepunktDao kulepunktDao;

    @Transactional
    public void create(String aktorId, Kartlegging kartlegging) {
        long kartleggingId = db.nesteFraSekvens("KARTLEGGING_SEQ");
        db.update("INSERT INTO KARTLEGGING (" +
                "kartlegging_id, " +
                "aktor_id, " +
                "under_oppfolging, " +
                "oppsummering, " +
                "oppsummering_key, " +
                "kartlegging_dato) " +
                "VALUES (?, ?, ?, ?, ?, ?)",
            kartleggingId,
            aktorId,
            true,
            kartlegging.getOppsummering(),
            kartlegging.getOppsummeringKey(),
            Timestamp.from(kartlegging.getKartleggingDato())
        );
        kartlegging.getBesvarelse().forEach(s -> besvarelseDao.create(s, kartleggingId));
        kartlegging.getRaad().forEach(r -> raadDao.create(r, kartleggingId));
        kartlegging.getKulepunkter().forEach(k -> kulepunktDao.create(k, kartleggingId));

        LOGGER.info("lagret kartlegging med id={}", kartleggingId);
    }

    public Kartlegging fetchMostRecentByAktorId(String aktorId) {

        Kartlegging kartlegging = db.query("SELECT * FROM KARTLEGGING WHERE aktor_id = ?",
            this::map,
            aktorId
        ).stream()
            .sorted(comparing(Kartlegging::getKartleggingDato).reversed())
            .findFirst()
            .orElseThrow(INGEN_BESVARELSE);

        return kartlegging.toBuilder()
            .besvarelse(besvarelseDao.fetchByKartleggingId(kartlegging.getKartleggingId()))
            .raad(raadDao.fetchByKartleggingId(kartlegging.getKartleggingId()))
            .kulepunkter(kulepunktDao.fetchByKartleggingId(kartlegging.getKartleggingId()))
            .build();
    }

    public int anonymiserByAktorId(String aktorId, Date sluttDato) {
        return db.update("UPDATE KARTLEGGING " +
                "SET aktor_id = 'anonym' " +
                "WHERE aktor_id = ? AND KARTLEGGING_DATO <= ?",
            aktorId, sluttDato);
    }

    @SneakyThrows
    Kartlegging map(ResultSet rs) {
        return Kartlegging.builder()
            .kartleggingId(rs.getLong("kartlegging_id"))
            .aktorId(rs.getString("aktor_id"))
            .underOppfolging(rs.getBoolean("under_oppfolging"))
            .oppsummering(rs.getString("oppsummering"))
            .oppsummeringKey(rs.getString("oppsummering_key"))
            .kartleggingDato(rs.getTimestamp("kartlegging_dato").toInstant())
            .build();
    }
}
