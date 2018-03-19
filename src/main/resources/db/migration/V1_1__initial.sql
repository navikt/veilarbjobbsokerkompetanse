CREATE SEQUENCE BESVARELSE_SEQ START WITH 1;
CREATE SEQUENCE SVAR_SEQ START WITH 1;
CREATE SEQUENCE SVARALTERNATIV_SEQ START WITH 1;
CREATE SEQUENCE RAAD_SEQ START WITH 1;

CREATE TABLE BESVARELSE (
  besvarelse_id    NUMBER(19)     NOT NULL,
  aktor_id         NVARCHAR2(255) NOT NULL,
  under_oppfolging NUMBER(1, 0)   NOT NULL,
  besvarelse_dato  TIMESTAMP(6)   NOT NULL,
  CONSTRAINT BESVARELSE_PK PRIMARY KEY (besvarelse_id)
);

CREATE TABLE SVAR (
  svar_id       NUMBER(19) NOT NULL,
  besvarelse_id NUMBER(19) NOT NULL,
  sporsmal_key  NVARCHAR2(255),
  sporsmal      NVARCHAR2(255),
  tips_key      NVARCHAR2(255),
  tips          NVARCHAR2(255),
  CONSTRAINT SVAR_PK PRIMARY KEY (svar_id),
  CONSTRAINT SVAR_BESVARELSE_FK FOREIGN KEY (besvarelse_id) REFERENCES BESVARELSE (besvarelse_id)
);

CREATE TABLE SVARALTERNATIV (
  svaralternativ_id  NUMBER(19) NOT NULL,
  svar_id            NUMBER(19) NOT NULL,
  svaralternativ_key NVARCHAR2(255),
  svaralternativ     NVARCHAR2(255),
  CONSTRAINT SVARALTERNATIV_PK PRIMARY KEY (svaralternativ_id),
  CONSTRAINT SVARALTERNATIV_SVAR_FK FOREIGN KEY (svar_id) REFERENCES SVAR (svar_id)
);

CREATE TABLE RAAD (
  raad_id       NUMBER(19) NOT NULL,
  besvarelse_id NUMBER(19) NOT NULL,
  raad          CLOB,
  CONSTRAINT RAAD_PK PRIMARY KEY (raad_id),
  CONSTRAINT RAAD_BESVARELSE_FK FOREIGN KEY (besvarelse_id) REFERENCES BESVARELSE (besvarelse_id)
);

CREATE INDEX BESVARELSE_AKTOR_ID_IDX
  ON BESVARELSE (aktor_id);
CREATE INDEX SVAR_BESVARELSE_IDX
  ON SVAR (besvarelse_id);
CREATE INDEX SVARALTERNATIV_SVAR_IDX
  ON SVARALTERNATIV (svar_id);
CREATE INDEX RAAD_BESVARELSE_IDX
  ON RAAD (besvarelse_id);

-- ALTER TABLE RAAD DROP CONSTRAINT RAAD_BESVARELSE_FK;
-- ALTER TABLE SVARALTERNATIV DROP CONSTRAINT SVARALTERNATIV_SVAR_FK;
-- ALTER TABLE SVAR DROP CONSTRAINT SVAR_BESVARELSE_FK;
-- DROP TABLE RAAD;
-- DROP TABLE SVARALTERNATIV;
-- DROP TABLE SVAR;
-- DROP TABLE BESVARELSE;
-- DROP SEQUENCE RAAD_SEQ;
-- DROP SEQUENCE SVARALTERNATIV_SEQ;
-- DROP SEQUENCE SVAR_SEQ;
-- DROP SEQUENCE BESVARELSE_SEQ;