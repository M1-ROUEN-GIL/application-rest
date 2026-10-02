package fr.univrouen.sepa26.util;

import fr.univrouen.sepa26.services.XsltTransformationService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Composant de transformation XSLT Saxon.
 * Implémente XsltTransformationService.
 */
@Component
public class XsltTransformer implements XsltTransformationService {

    private static final String DEFAULT_XSLT_PATH = "xml/sepa26.xslt";
    private final TransformerFactory transformerFactory;

    public XsltTransformer() {
        this.transformerFactory = new net.sf.saxon.TransformerFactoryImpl();
    }

    @Override
    public String transform(String xml, String xsltPath) throws TransformerException, IOException {
        StreamSource xsltSource = new StreamSource(new ClassPathResource(xsltPath).getInputStream());
        Transformer transformer = transformerFactory.newTransformer(xsltSource);
        StringReader reader = new StringReader(xml);
        StringWriter writer = new StringWriter();
        transformer.transform(new StreamSource(reader), new StreamResult(writer));
        return writer.toString();
    }

    @Override
    public String transformSepa(String xml) throws TransformerException, IOException {
        return transform(xml, DEFAULT_XSLT_PATH);
    }
}
