package fr.univrouen.sepa26.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.DocumentRepository;

/**
 * Tests unitaires pour le service SepaService.
 * Utilise Mockito pour isoler la logique métier du repository.
 */
@ExtendWith(MockitoExtension.class)
public class SepaServiceTest {

    @Mock
    private DocumentRepository repository;

    @InjectMocks
    private SepaService sepaService;

    private Document validDoc;

    @BeforeEach
    void setUp() {
        validDoc = new Document();
        // NOTE: creDtTm and ctrlSum are for our DB/Internal use but NOT allowed in the pure XSD Document element
        // The XSD expects a sequence of DrctDbtTxInf only.

        Document.DrctDbtTxInf inf = new Document.DrctDbtTxInf();
        inf.setPmtId("REF-UNIT-TEST");

        Document.InstdAmt amt = new Document.InstdAmt();
        amt.setValue(100.0);
        amt.setCcy("EUR");
        inf.setInstdAmt(amt);

        Document.DrctDbtTx tx = new Document.DrctDbtTx();
        Document.MndtRltdInf mndt = new Document.MndtRltdInf();
        mndt.setMndtId("MANDAT-UNIT");
        mndt.setDtOfSgntr("2026-03-01");
        tx.setMndtRltdInf(mndt);
        inf.setDrctDbtTx(tx);

        Document.DbtrAgt agt = new Document.DbtrAgt();
        Document.FinInstnId fin = new Document.FinInstnId();
        fin.setBic("ROUENSWNXXX");
        agt.setFinInstnId(fin);
        inf.setDbtrAgt(agt);

        Document.Dbtr dbtr = new Document.Dbtr();
        dbtr.setNm("Client Unitaire");
        inf.setDbtr(dbtr);

        Document.DbtrAcct acct = new Document.DbtrAcct();
        Document.DbtrAcctId acctId = new Document.DbtrAcctId();
        acctId.setIban("FR7612345678901234567890123");
        acct.setId(acctId);
        inf.setDbtrAcct(acct);

        validDoc.getDrctDbtTxInfs().add(inf);
    }

    @Test
    void testValidateXSD_Success() {
        assertTrue(sepaService.validateXSD(validDoc), "Le document devrait être valide selon le XSD");
    }

    @Test
    void testValidateXSD_Failure() {
        Document invalidDoc = new Document(); // Document vide, invalide selon XSD (minOccurs=1 attendu par défaut ou sequence non vide)
        assertFalse(sepaService.validateXSD(invalidDoc), "Un document vide devrait être invalide");
    }

    @Test
    void testSave_Success() {
        when(repository.findByPmtId("REF-UNIT-TEST")).thenReturn(Optional.empty());
        when(repository.save(any(Document.class))).thenReturn(validDoc);

        Document saved = sepaService.save(validDoc);

        assertNotNull(saved);
        verify(repository, times(1)).save(validDoc);
    }

    @Test
    void testSave_DuplicateError() {
        when(repository.findByPmtId("REF-UNIT-TEST")).thenReturn(Optional.of(new Document()));

        Document result = sepaService.save(validDoc);

        assertNull(result, "Le service devrait retourner null en cas de doublon de PmtId");
        verify(repository, never()).save(any());
    }

    @Test
    void testDelete_Success() {
        long id = 123L;
        when(repository.existsById(id)).thenReturn(true);

        boolean deleted = sepaService.delete(id);

        assertTrue(deleted);
        verify(repository, times(1)).deleteById(id);
    }

    @Test
    void testDelete_NotFound() {
        long id = 999L;
        when(repository.existsById(id)).thenReturn(false);

        boolean deleted = sepaService.delete(id);

        assertFalse(deleted);
        verify(repository, never()).deleteById(id);
    }
}
