package fr.univrouen.sepa26.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.repository.DocumentRepository;

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
        
        Document.CstmrDrctDbtInitn initn = new Document.CstmrDrctDbtInitn();
        validDoc.setCstmrDrctDbtInitn(initn);

        Document.GrpHdr grpHdr = new Document.GrpHdr();
        grpHdr.setMsgId("MSG-UNIT-TEST");
        grpHdr.setCreDtTm(LocalDateTime.parse("2026-03-01T10:00:00"));
        grpHdr.setNbOfTxs(1);
        grpHdr.setCtrlSum(100.0);
        Document.Party initgPty = new Document.Party();
        initgPty.setNm("Test Company");
        grpHdr.setInitgPty(initgPty);
        initn.setGrpHdr(grpHdr);

        Document.PmtInf pmtInf = new Document.PmtInf();
        pmtInf.setPmtInfId("PMT-UNIT-1");
        pmtInf.setNbOfTxs(1);
        pmtInf.setCtrlSum(100.0);
        pmtInf.setReqdColltnDt(LocalDate.parse("2026-03-10"));
        
        Document.PaymentTypeInfo pmtTpInf = new Document.PaymentTypeInfo();
        Document.ServiceLevel sl = new Document.ServiceLevel();
        Document.LocalInstrument li = new Document.LocalInstrument();
        li.setCd("SEPA");
        sl.setCd("SEPA");
        pmtTpInf.setLclInstrm(li);
        pmtTpInf.setSvcLvl(sl);
        pmtTpInf.setSeqTp("RCUR");
        pmtInf.setPmtTpInf(pmtTpInf);
        

        Document.Party cdtr = new Document.Party();
        cdtr.setNm("Creditor Company");
        pmtInf.setCdtr(cdtr);

        Document.Account cdtrAcct = new Document.Account();
        Document.AccountId cdtrAcctId = new Document.AccountId();
        cdtrAcctId.setIban("FR7612345678901234567890123");
        cdtrAcct.setId(cdtrAcctId);
        pmtInf.setCdtrAcct(cdtrAcct);

        Document.Agent cdtrAgt = new Document.Agent();
        Document.FinInstnId finCdtr = new Document.FinInstnId();
        finCdtr.setBic("BANKFRPPXXX");
        cdtrAgt.setFinInstnId(finCdtr);
        pmtInf.setCdtrAgt(cdtrAgt);
        
        
        Document.AccountSchemeId cdtrSchmeId = new Document.AccountSchemeId();
        Document.AccountId prvtIdWrapper = new Document.AccountId();
        Document.PrivateId prvtId = new Document.PrivateId();
        Document.OtherIdentification othr = new Document.OtherIdentification();
        othr.setId("FR00ZZZ123456");
        Document.SchemeName schmeNm = new Document.SchemeName();
        schmeNm.setPrtry("SEPA");
        othr.setSchemeName(schmeNm);
        prvtId.setOthr(othr);
        prvtIdWrapper.setPrvtId(prvtId);
        cdtrSchmeId.setId(prvtIdWrapper);
        pmtInf.setCdtrSchmeId(cdtrSchmeId);
        

        Document.DrctDbtTxInf txInf = new Document.DrctDbtTxInf();
        txInf.setPmtId("REF-UNIT-TEST");

        Document.InstdAmt amt = new Document.InstdAmt();
        amt.setValue(100.0);
        amt.setCcy("EUR");
        txInf.setInstdAmt(amt);

        Document.DrctDbtTx tx = new Document.DrctDbtTx();
        Document.MndtRltdInf mndt = new Document.MndtRltdInf();
        mndt.setMndtId("MANDAT-UNIT");
        mndt.setDtOfSgntr(LocalDate.parse("2026-03-01"));
        tx.setMndtRltdInf(mndt);
        txInf.setDrctDbtTx(tx);

        Document.Agent dbtrAgt = new Document.Agent();
        Document.FinInstnId finDbtr = new Document.FinInstnId();
        finDbtr.setBic("BANKDEFFXXX");
        dbtrAgt.setFinInstnId(finDbtr);
        txInf.setDbtrAgt(dbtrAgt);

        Document.Party dbtr = new Document.Party();
        dbtr.setNm("Client Unitaire");
        txInf.setDbtr(dbtr);

        Document.Account acct = new Document.Account();
        Document.AccountId acctId = new Document.AccountId();
        acctId.setIban("FR7612345678901234567890123");
        acct.setId(acctId);
        txInf.setDbtrAcct(acct);
        
        txInf.setRmtInf("Facture Unitaire");
        
        pmtInf.getDrctDbtTxInfs().add(txInf);

        initn.getPmtInfs().add(pmtInf);
    }
    
    @Test
    void testValidateXSDRaw_Success() {
        String xml = sepaService.convertToXml(validDoc);
        assertTrue(sepaService.validateXSDRaw(xml));
    }
    
    @Test
    void testValidateXSDRaw_Failure() {
        String invalidXml = "<Document xmlns=\"http://univ.fr/sepa26\"></Document>";
        assertFalse(sepaService.validateXSDRaw(invalidXml));
    }
    
    @Test
    void testSave_Success() {
        when(repository.findByPmtId("REF-UNIT-TEST")).thenReturn(Optional.empty());
        when(repository.save(any(Document.class))).thenReturn(validDoc);

        Document saved = sepaService.save(validDoc);

        assertNotNull(saved, "Le document sauvegardé ne devrait pas être null");
        verify(repository, times(1)).save(validDoc);
    }
    
    @Test
    void testSave_DuplicateError() {
        when(repository.findByPmtId("REF-UNIT-TEST"))
            .thenReturn(Optional.of(new Document()));

        Document result = sepaService.save(validDoc);

        assertNull(result, "Le service devrait retourner null en cas de doublon");
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
        verify(repository, never()).deleteById(any());
    }
    /*
    @Test
    void testValidateXSD_Success() {
        assertTrue(sepaService.validateXSD(validDoc), "Le document devrait être valide selon le XSD");
    }
    @Test
    void testValidateXSD_Failure() {
        Document invalidDoc = new Document();
        
        //System.out.println(sepaService.convertToXml(invalidDoc));
        
        boolean valid = sepaService.validateXSD(invalidDoc);
        assertFalse(valid, "Un document vide devrait être invalide");
    }
    @Test
    void testPrintXml() {
        String xml = sepaService.convertToXml(validDoc);
        System.out.println("=== XML généré ===");
        System.out.println(xml);
        System.out.println("=================");
    }
   */
}
