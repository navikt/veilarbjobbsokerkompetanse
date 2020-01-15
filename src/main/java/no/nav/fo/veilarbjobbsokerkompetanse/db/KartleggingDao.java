package no.nav.fo.veilarbjobbsokerkompetanse.db;

import lombok.SneakyThrows;
import no.nav.fo.veilarbjobbsokerkompetanse.domain.Kartlegging;
import no.nav.sbl.jdbc.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.Optional.of;

@Component
public class KartleggingDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(KartleggingDao.class);

    private final Database db;

    private final BesvarelseDao besvarelseDao;

    private final RaadDao raadDao;

    private final KulepunktDao kulepunktDao;

    @Inject
    public KartleggingDao(Database db) {
        this.db = db;
        this.besvarelseDao = new BesvarelseDao(db);
        this.raadDao = new RaadDao(db);
        this.kulepunktDao = new KulepunktDao(db);
    }

    @Transactional
    public long create(String aktorId, Kartlegging kartlegging) {
        long kartleggingId = db.nesteFraSekvens("KARTLEGGING_SEQ");
        db.update("INSERT INTO KARTLEGGING (" +
                "kartlegging_id, " +
                "aktor_id, " +
                "under_oppfolging, " +
                "oppsummering, " +
                "oppsummering_key, " +
                "kartlegging_dato) " +
                "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)",
            kartleggingId,
            aktorId,
            true,
            kartlegging.getOppsummering(),
            kartlegging.getOppsummeringKey()
        );
        kartlegging.getBesvarelse().forEach(s -> besvarelseDao.create(s, kartleggingId));
        kartlegging.getRaad().forEach(r -> raadDao.create(r, kartleggingId));
        kartlegging.getKulepunkter().forEach(k -> kulepunktDao.create(k, kartleggingId));

        LOGGER.info("lagret kartlegging med id={}", kartleggingId);
        return kartleggingId;
    }

    public Optional<Kartlegging> fetchMostRecentByAktorId(String aktorId) {
        return db.query("SELECT * FROM KARTLEGGING WHERE aktor_id = ?", this::map, aktorId)
            .stream()
            .max(comparing(Kartlegging::getKartleggingTidspunkt))
            .map(this::populate);
    }

    private Kartlegging populate(Kartlegging kartlegging) {
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
            .kartleggingTidspunkt(rs.getTimestamp("kartlegging_dato"))
            .build();
    }

    public Kartlegging fetchById(long id) {
        return of(db.queryForObject("SELECT * FROM KARTLEGGING WHERE kartlegging_id = ?",
            this::map,
            id
        ))
            .map(this::populate)
            .get();
    }

}
