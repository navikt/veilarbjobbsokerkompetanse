package no.nav.fo.veilarbjobbsokerkompetanse.mock.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static no.nav.fo.veilarbjobbsokerkompetanse.config.ApplicationConfig.isMocksEnabled;

public class Mock implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        return isMocksEnabled();
    }

}
