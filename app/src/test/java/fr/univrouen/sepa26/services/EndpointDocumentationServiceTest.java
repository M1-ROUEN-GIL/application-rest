package fr.univrouen.sepa26.services;

import fr.univrouen.sepa26.services.impl.EndpointDocumentationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EndpointDocumentationServiceTest {

    private EndpointDocumentationService documentationService;

    @BeforeEach
    void setUp() {
        documentationService = new EndpointDocumentationServiceImpl();
    }

    @Test
    void testGetEndpointsDocumentation_ContainsRequiredEndpoints() {
        List<Map<String, String>> docs = documentationService.getEndpointsDocumentation();
        assertNotNull(docs);
        assertEquals(10, docs.size(), "Il doit y avoir 10 endpoints documentés");

        boolean hasResumeXml = docs.stream().anyMatch(e -> "/sepa26/resume/xml".equals(e.get("url")));
        boolean hasResumeHtml = docs.stream().anyMatch(e -> "/sepa26/resume/html".equals(e.get("url")));
        boolean hasInsert = docs.stream().anyMatch(e -> "/sepa26/insert".equals(e.get("url")));
        boolean hasDelete = docs.stream().anyMatch(e -> "/sepa26/delete/{id}".equals(e.get("url")));

        assertTrue(hasResumeXml, "L'endpoint /sepa26/resume/xml doit être présent");
        assertTrue(hasResumeHtml, "L'endpoint /sepa26/resume/html doit être présent");
        assertTrue(hasInsert, "L'endpoint /sepa26/insert doit être présent");
        assertTrue(hasDelete, "L'endpoint /sepa26/delete/{id} doit être présent");
    }
}
