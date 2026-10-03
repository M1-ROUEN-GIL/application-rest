package fr.univrouen.sepa26.services;

import fr.univrouen.sepa26.dto.ValidationResult;
import fr.univrouen.sepa26.services.impl.XmlValidationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XmlValidationServiceTest {

    private XmlValidationService xmlValidationService;

    @BeforeEach
    void setUp() {
        xmlValidationService = new XmlValidationServiceImpl();
    }

    @Test
    void testValidate_EmptyOrNullXml() {
        ValidationResult nullRes = xmlValidationService.validate(null);
        assertFalse(nullRes.valid());
        assertNotNull(nullRes.errorMessage());

        ValidationResult emptyRes = xmlValidationService.validate("   ");
        assertFalse(emptyRes.valid());
        assertNotNull(emptyRes.errorMessage());
    }

    @Test
    void testValidate_MalformedXml() {
        ValidationResult result = xmlValidationService.validate("<Document><Unclosed>");
        assertFalse(result.valid());
        assertNotNull(result.errorMessage());
    }

    @Test
    void testValidate_InvalidAgainstSchema() {
        String invalidXml = "<Document xmlns=\"http://univ.fr/sepa26\"><InvalidTag/></Document>";
        ValidationResult result = xmlValidationService.validate(invalidXml);
        assertFalse(result.valid());
        assertTrue(result.errorMessage().contains("Erreur de validation XSD"));
    }
}
