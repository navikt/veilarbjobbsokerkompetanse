package no.nav.fo.veilarbjobbsokerkompetanse.db;

import java.sql.ResultSet;

public interface Dao<T> {

    void create(T t);

    T fetch(long id);

    T map(ResultSet rs);

}
