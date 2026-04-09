package fr.univrouen.sepa26.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.services.SepaService;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Initialise la base de données avec 2 documents de test au démarrage de l'application.
 * Chaque document contient 2 transactions SEPA.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private SepaService sepaService;

    @Override
    public void run(String... args) throws Exception {
        // Vérifier si les données existent déjà
        if (sepaService.getLast10().isEmpty()) {
            System.out.println("Initialisation de la base de données avec 2 documents de test...");

            createDocument1();
            createDocument2();

            System.out.println("✓ Données de test créées avec succès!");
        }
    }

    /**
     * Crée le premier document avec 2 transactions
     */
    private void createDocument1() {
        Document doc = new Document();

        Document.CstmrDrctDbtInitn initn = new Document.CstmrDrctDbtInitn();
        doc.setCstmrDrctDbtInitn(initn);

        // Header
        Document.GrpHdr grpHdr = new Document.GrpHdr();
        grpHdr.setMsgId("MSG-INIT-001");
        grpHdr.setCreDtTm(LocalDateTime.now());
        grpHdr.setNbOfTxs(2);
        grpHdr.setCtrlSum(600.0);
        Document.Party initgPty = new Document.Party();
        initgPty.setNm("Societe A");
        grpHdr.setInitgPty(initgPty);
        initn.setGrpHdr(grpHdr);

        // Payment Info
        Document.PmtInf pmtInf = createPaymentInfo("PMT-INIT-001");

        // TX 1
        pmtInf.getDrctDbtTxInfs().add(createTransaction(
            "REF-INIT-001-A",
            "300.00",
            "Client 1A",
            "FR7630001007941234567890185",
            "MANDAT-001-A"
        ));

        // TX 2
        pmtInf.getDrctDbtTxInfs().add(createTransaction(
            "REF-INIT-001-B",
            "300.00",
            "Client 1B",
            "FR7620041010050500013M02606",
            "MANDAT-001-B"
        ));

        pmtInf.setNbOfTxs(2);
        pmtInf.setCtrlSum(600.0);
        initn.getPmtInfs().add(pmtInf);

        sepaService.save(doc);
    }

    /**
     * Crée le deuxième document avec 2 transactions
     */
    private void createDocument2() {
        Document doc = new Document();

        Document.CstmrDrctDbtInitn initn = new Document.CstmrDrctDbtInitn();
        doc.setCstmrDrctDbtInitn(initn);

        // Header
        Document.GrpHdr grpHdr = new Document.GrpHdr();
        grpHdr.setMsgId("MSG-INIT-002");
        grpHdr.setCreDtTm(LocalDateTime.now());
        grpHdr.setNbOfTxs(2);
        grpHdr.setCtrlSum(1000.0);
        Document.Party initgPty = new Document.Party();
        initgPty.setNm("Societe B");
        grpHdr.setInitgPty(initgPty);
        initn.setGrpHdr(grpHdr);

        // Payment Info
        Document.PmtInf pmtInf = createPaymentInfo("PMT-INIT-002");

        // TX 1
        pmtInf.getDrctDbtTxInfs().add(createTransaction(
            "REF-INIT-002-A",
            "500.00",
            "Client 2A",
            "FR7612548017150001234567890",
            "MANDAT-002-A"
        ));

        // TX 2
        pmtInf.getDrctDbtTxInfs().add(createTransaction(
            "REF-INIT-002-B",
            "500.00",
            "Client 2B",
            "FR7614508000505917721779613",
            "MANDAT-002-B"
        ));

        pmtInf.setNbOfTxs(2);
        pmtInf.setCtrlSum(1000.0);
        initn.getPmtInfs().add(pmtInf);

        sepaService.save(doc);
    }

    private Document.PmtInf createPaymentInfo(String pmtInfId) {
        Document.PmtInf pmtInf = new Document.PmtInf();
        pmtInf.setPmtInfId(pmtInfId);
        pmtInf.setReqdColltnDt(LocalDate.now().plusDays(7));

        Document.PaymentTypeInfo pmtTpInf = new Document.PaymentTypeInfo();
        Document.ServiceLevel sl = new Document.ServiceLevel();
        sl.setCd("SEPA");
        Document.LocalInstrument li = new Document.LocalInstrument();
        li.setCd("SEPA");
        pmtTpInf.setSvcLvl(sl);
        pmtTpInf.setLclInstrm(li);
        pmtTpInf.setSeqTp("RCUR");
        pmtInf.setPmtTpInf(pmtTpInf);

        Document.Party cdtr = new Document.Party();
        cdtr.setNm("Creancier INIT");
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

        return pmtInf;
    }

    private Document.DrctDbtTxInf createTransaction(
            String pmtId,
            String amount,
            String debtorName,
            String debtorIban,
            String mandateId) {

        Document.DrctDbtTxInf txInf = new Document.DrctDbtTxInf();
        txInf.setPmtId(pmtId);

        Document.InstdAmt amt = new Document.InstdAmt();
        amt.setValue(Double.parseDouble(amount));
        amt.setCcy("EUR");
        txInf.setInstdAmt(amt);

        Document.DrctDbtTx tx = new Document.DrctDbtTx();
        Document.MndtRltdInf mndt = new Document.MndtRltdInf();
        mndt.setMndtId(mandateId);
        mndt.setDtOfSgntr(LocalDate.now().minusMonths(1));
        tx.setMndtRltdInf(mndt);
        txInf.setDrctDbtTx(tx);

        Document.Agent dbtrAgt = new Document.Agent();
        Document.FinInstnId finDbtr = new Document.FinInstnId();
        finDbtr.setBic("BANKDEFFXXX");
        dbtrAgt.setFinInstnId(finDbtr);
        txInf.setDbtrAgt(dbtrAgt);

        Document.Party dbtr = new Document.Party();
        dbtr.setNm(debtorName);
        txInf.setDbtr(dbtr);

        Document.Account acct = new Document.Account();
        Document.AccountId acctId = new Document.AccountId();
        acctId.setIban(debtorIban);
        acct.setId(acctId);
        txInf.setDbtrAcct(acct);

        txInf.setRmtInf("Facture initiale");

        return txInf;
    }
}
