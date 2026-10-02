package fr.univrouen.sepa26.services;

import fr.univrouen.sepa26.TestDocumentBuilder;
import fr.univrouen.sepa26.dto.ParseResult;
import fr.univrouen.sepa26.model.Document;
import fr.univrouen.sepa26.services.impl.XmlSerializationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XmlSerializationServiceTest {

    private XmlSerializationService xmlSerializationService;

    @BeforeEach
    void setUp() {
        xmlSerializationService = new XmlSerializationServiceImpl();
    }

    @Test
    void testMarshalAndUnmarshal_Success() {
        Document originalDoc = TestDocumentBuilder.buildDocumentWithTwoTransactions();
        String xml = xmlSerializationService.marshalToXml(originalDoc);

        assertNotNull(xml);
        assertTrue(xml.contains("REF-MOCK-TX-001"));
        assertTrue(xml.contains("REF-MOCK-TX-002"));

        ParseResult<Document> parseResult = xmlSerializationService.unmarshalFromXml(xml);
        assertTrue(parseResult.isSuccess());
        assertNotNull(parseResult.data());
        assertEquals("MSG-MOCK-001", parseResult.data().getCstmrDrctDbtInitn().getGrpHdr().getMsgId());
    }

    @Test
    void testUnmarshal_InvalidXml() {
        ParseResult<Document> result = xmlSerializationService.unmarshalFromXml("<InvalidXml>>>");
        assertFalse(result.isSuccess());
        assertNull(result.data());
        assertNotNull(result.errorMessage());
    }

    @Test
    void testUnmarshal_EmptyXml() {
        ParseResult<Document> result = xmlSerializationService.unmarshalFromXml("");
        assertFalse(result.isSuccess());
        assertNull(result.data());
    }
}
