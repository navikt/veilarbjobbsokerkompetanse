FROM docker.adeo.no:5000/bekkci/maven-builder as builder
ADD / /source
RUN build

FROM docker.adeo.no:5000/bekkci/nais-java-app
COPY --from=builder /source/target/veilarbjobbsokerkompetanse /app