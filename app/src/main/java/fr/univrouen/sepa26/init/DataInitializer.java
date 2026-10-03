package fr.univrouen.sepa26.init;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import fr.univrouen.sepa26.model.Account;
import fr.univrouen.sepa26.model.AccountId;
import fr.univrouen.sepa26.model.AccountSchemeId;
import fr.univrouen.sepa26.model.Agent;
import fr.univrouen.sepa26.model.CstmrDrctDbtInitn;
import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.model.DrctDbtTx;
import fr.univrouen.sepa26.model.DrctDbtTxInf;
import fr.univrouen.sepa26.model.FinInstnId;
import fr.univrouen.sepa26.model.GrpHdr;
import fr.univrouen.sepa26.model.InstdAmt;
import fr.univrouen.sepa26.model.LocalInstrument;
import fr.univrouen.sepa26.model.MndtRltdInf;
import fr.univrouen.sepa26.model.OtherIdentification;
import fr.univrouen.sepa26.model.Party;
import fr.univrouen.sepa26.model.PaymentTypeInfo;
import fr.univrouen.sepa26.model.PmtInf;
import fr.univrouen.sepa26.model.PrivateId;
import fr.univrouen.sepa26.model.SchemeName;
import fr.univrouen.sepa26.model.ServiceLevel;
import fr.univrouen.sepa26.services.SepaService;

/**
 * Initialise la base de données avec 2 documents de test au démarrage de l'application.
 * Chaque document contient 2 transactions SEPA.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final SepaService sepaService;

    @Autowired
    public DataInitializer(SepaService sepaService) {
        this.sepaService = sepaService;
    }

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
     * Crée le premier document avec 2 transactions.
     */
    private void createDocument1() {
        Document doc = new Document();

        CstmrDrctDbtInitn initn = new CstmrDrctDbtInitn();
        doc.setCstmrDrctDbtInitn(initn);

        // Header
        GrpHdr grpHdr = new GrpHdr();
        grpHdr.setMsgId("MSG-INIT-001");
        grpHdr.setCreDtTm(LocalDateTime.now());
        grpHdr.setNbOfTxs(2);
        grpHdr.setCtrlSum(600.0);
        Party initgPty = new Party();
        initgPty.setNm("Societe A");
        grpHdr.setInitgPty(initgPty);
        initn.setGrpHdr(grpHdr);

        // Payment Info
        PmtInf pmtInf = createPaymentInfo("PMT-INIT-001");

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
     * Crée le deuxième document avec 2 transactions.
     */
    private void createDocument2() {
        Document doc = new Document();

        CstmrDrctDbtInitn initn = new CstmrDrctDbtInitn();
        doc.setCstmrDrctDbtInitn(initn);

        // Header
        GrpHdr grpHdr = new GrpHdr();
        grpHdr.setMsgId("MSG-INIT-002");
        grpHdr.setCreDtTm(LocalDateTime.now());
        grpHdr.setNbOfTxs(2);
        grpHdr.setCtrlSum(1000.0);
        Party initgPty = new Party();
        initgPty.setNm("Societe B");
        grpHdr.setInitgPty(initgPty);
        initn.setGrpHdr(grpHdr);

        // Payment Info
        PmtInf pmtInf = createPaymentInfo("PMT-INIT-002");

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

    private PmtInf createPaymentInfo(String pmtInfId) {
        PmtInf pmtInf = new PmtInf();
        pmtInf.setPmtInfId(pmtInfId);
        pmtInf.setReqdColltnDt(LocalDate.now().plusDays(7));

        PaymentTypeInfo pmtTpInf = new PaymentTypeInfo();
        ServiceLevel sl = new ServiceLevel();
        sl.setCd("SEPA");
        LocalInstrument li = new LocalInstrument();
        li.setCd("SEPA");
        pmtTpInf.setSvcLvl(sl);
        pmtTpInf.setLclInstrm(li);
        pmtTpInf.setSeqTp("RCUR");
        pmtInf.setPmtTpInf(pmtTpInf);

        Party cdtr = new Party();
        cdtr.setNm("Creancier INIT");
        pmtInf.setCdtr(cdtr);

        Account cdtrAcct = new Account();
        AccountId cdtrAcctId = new AccountId();
        cdtrAcctId.setIban("FR7612345678901234567890123");
        cdtrAcct.setId(cdtrAcctId);
        pmtInf.setCdtrAcct(cdtrAcct);

        Agent cdtrAgt = new Agent();
        FinInstnId finCdtr = new FinInstnId();
        finCdtr.setBic("BANKFRPPXXX");
        cdtrAgt.setFinInstnId(finCdtr);
        pmtInf.setCdtrAgt(cdtrAgt);

        AccountSchemeId cdtrSchmeId = new AccountSchemeId();
        AccountId prvtIdWrapper = new AccountId();
        PrivateId prvtId = new PrivateId();
        OtherIdentification othr = new OtherIdentification();
        othr.setId("FR00ZZZ123456");
        SchemeName schmeNm = new SchemeName();
        schmeNm.setPrtry("SEPA");
        othr.setSchemeName(schmeNm);
        prvtId.setOthr(othr);
        prvtIdWrapper.setPrvtId(prvtId);
        cdtrSchmeId.setId(prvtIdWrapper);
        pmtInf.setCdtrSchmeId(cdtrSchmeId);

        return pmtInf;
    }

    private DrctDbtTxInf createTransaction(
            String pmtId,
            String amount,
            String debtorName,
            String debtorIban,
            String mandateId) {

        DrctDbtTxInf txInf = new DrctDbtTxInf();
        txInf.setPmtId(pmtId);

        InstdAmt amt = new InstdAmt();
        amt.setValue(Double.parseDouble(amount));
        amt.setCcy("EUR");
        txInf.setInstdAmt(amt);

        DrctDbtTx tx = new DrctDbtTx();
        MndtRltdInf mndt = new MndtRltdInf();
        mndt.setMndtId(mandateId);
        mndt.setDtOfSgntr(LocalDate.now().minusMonths(1));
        tx.setMndtRltdInf(mndt);
        txInf.setDrctDbtTx(tx);

        Agent dbtrAgt = new Agent();
        FinInstnId finDbtr = new FinInstnId();
        finDbtr.setBic("BANKDEFFXXX");
        dbtrAgt.setFinInstnId(finDbtr);
        txInf.setDbtrAgt(dbtrAgt);

        Party dbtr = new Party();
        dbtr.setNm(debtorName);
        txInf.setDbtr(dbtr);

        Account acct = new Account();
        AccountId acctId = new AccountId();
        acctId.setIban(debtorIban);
        acct.setId(acctId);
        txInf.setDbtrAcct(acct);

        txInf.setRmtInf("Facture initiale");

        return txInf;
    }
}
