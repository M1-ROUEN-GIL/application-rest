package fr.univrouen.sepa26;

import java.time.LocalDate;
import java.time.LocalDateTime;

import fr.univrouen.sepa26.model.Document;

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
        Document.CstmrDrctDbtInitn initn = new Document.CstmrDrctDbtInitn();
        doc.setCstmrDrctDbtInitn(initn);

        // Header du groupe
        Document.GrpHdr grpHdr = new Document.GrpHdr();
        grpHdr.setMsgId("MSG-MOCK-001");
        grpHdr.setCreDtTm(LocalDateTime.parse("2026-04-09T14:00:00"));
        grpHdr.setNbOfTxs(2);
        grpHdr.setCtrlSum(500.0); // 250 + 250
        Document.Party initgPty = new Document.Party();
        initgPty.setNm("Entreprise Mockee");
        grpHdr.setInitgPty(initgPty);
        initn.setGrpHdr(grpHdr);

        // Informations de paiement
        Document.PmtInf pmtInf = createPaymentInfo();

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
    private static Document.PmtInf createPaymentInfo() {
        Document.PmtInf pmtInf = new Document.PmtInf();
        pmtInf.setPmtInfId("PMT-MOCK-001");
        pmtInf.setReqdColltnDt(LocalDate.parse("2026-04-15"));

        Document.PaymentTypeInfo pmtTpInf = new Document.PaymentTypeInfo();
        Document.ServiceLevel sl = new Document.ServiceLevel();
        sl.setCd("SEPA");
        Document.LocalInstrument li = new Document.LocalInstrument();
        li.setCd("SEPA");
        pmtTpInf.setSvcLvl(sl);
        pmtTpInf.setLclInstrm(li);
        pmtTpInf.setSeqTp("RCUR");
        pmtInf.setPmtTpInf(pmtTpInf);

        // Creancier
        Document.Party cdtr = new Document.Party();
        cdtr.setNm("Creancier Mocke SARL");
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

    /**
     * Crée une transaction individuellement avec les paramètres fournis.
     */
    private static Document.DrctDbtTxInf createTransaction(
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
        mndt.setDtOfSgntr(LocalDate.parse("2025-01-01"));
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

        txInf.setRmtInf("Facture automatique");

        return txInf;
    }
}
