package fr.univrouen.sepa26;

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

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Builder pour créer des documents SEPA de test avec des données réalistes.
 * Facilite la création de documents avec une ou plusieurs transactions.
 */
public class TestDocumentBuilder {

    /**
     * Crée un document avec 2 transactions par défaut.
     * @return Un document complètement peuplé avec 2 transactions de test.
     */
    public static Document buildDocumentWithTwoTransactions() {
        Document doc = new Document();

        // Initialisation
        CstmrDrctDbtInitn initn = new CstmrDrctDbtInitn();
        doc.setCstmrDrctDbtInitn(initn);

        // Header du groupe
        GrpHdr grpHdr = new GrpHdr();
        grpHdr.setMsgId("MSG-MOCK-001");
        grpHdr.setCreDtTm(LocalDateTime.parse("2026-04-09T14:00:00"));
        grpHdr.setNbOfTxs(2);
        grpHdr.setCtrlSum(500.0); // 250 + 250
        Party initgPty = new Party();
        initgPty.setNm("Entreprise Mockee");
        grpHdr.setInitgPty(initgPty);
        initn.setGrpHdr(grpHdr);

        // Informations de paiement
        PmtInf pmtInf = createPaymentInfo();

        // Transaction 1
        pmtInf.getDrctDbtTxInfs().add(createTransaction(
            "REF-MOCK-TX-001",
            "250.00",
            "Client A",
            "FR7612345678901234567890123",
            "MANDAT-MOCK-001"
        ));

        // Transaction 2
        pmtInf.getDrctDbtTxInfs().add(createTransaction(
            "REF-MOCK-TX-002",
            "250.00",
            "Client B",
            "FR7687654321098765432109876",
            "MANDAT-MOCK-002"
        ));

        pmtInf.setNbOfTxs(2);
        pmtInf.setCtrlSum(500.0);
        initn.getPmtInfs().add(pmtInf);

        return doc;
    }

    /**
     * Crée une information de paiement standard.
     */
    private static PmtInf createPaymentInfo() {
        PmtInf pmtInf = new PmtInf();
        pmtInf.setPmtInfId("PMT-MOCK-001");
        pmtInf.setReqdColltnDt(LocalDate.parse("2026-04-15"));

        PaymentTypeInfo pmtTpInf = new PaymentTypeInfo();
        ServiceLevel sl = new ServiceLevel();
        sl.setCd("SEPA");
        LocalInstrument li = new LocalInstrument();
        li.setCd("SEPA");
        pmtTpInf.setSvcLvl(sl);
        pmtTpInf.setLclInstrm(li);
        pmtTpInf.setSeqTp("RCUR");
        pmtInf.setPmtTpInf(pmtTpInf);

        // Creancier
        Party cdtr = new Party();
        cdtr.setNm("Creancier Mocke SARL");
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

    /**
     * Crée une transaction individuellement avec les paramètres fournis.
     */
    private static DrctDbtTxInf createTransaction(
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
        mndt.setDtOfSgntr(LocalDate.parse("2025-01-01"));
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

        txInf.setRmtInf("Facture automatique");

        return txInf;
    }
}
