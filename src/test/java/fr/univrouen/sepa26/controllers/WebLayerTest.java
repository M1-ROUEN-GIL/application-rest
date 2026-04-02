package fr.univrouen.sepa26.controllers;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class WebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnIndex() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Projet REST Sepa26")));
    }

    @Test
    public void shouldReturnResume() throws Exception {
        this.mockMvc.perform(get("/resume"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Envoi de la liste des flux SEPA enregistrés")));
    }

    @Test
    public void shouldReturnGuid() throws Exception {
        this.mockMvc.perform(get("/guid").param("guid", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Détail de la transaction SEPA 12345")));
    }

    @Test
    public void shouldReturnXml() throws Exception {
        this.mockMvc.perform(get("/xml").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_XML))
                .andExpect(xpath("/Document/DrctDbtTxInf/PmtId").string("REF-123"))
                .andExpect(xpath("/Document/DrctDbtTxInf/Dbtr/Nm").string("Test Debiteur"));
    }

    @Test
    public void shouldHandlePostTest() throws Exception {
        String xmlContent = "<Document><DrctDbtTxInf><PmtId>POST-REF</PmtId><Dbtr><Nm>Post Debiteur</Nm></Dbtr></DrctDbtTxInf></Document>";
        this.mockMvc.perform(post("/testpost")
                .contentType(MediaType.APPLICATION_XML)
                .content(xmlContent)
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(xpath("/Document/DrctDbtTxInf/PmtId").string("POST-REF"));
    }

    @Test
    public void shouldReturnFileContent() throws Exception {
        this.mockMvc.perform(post("/testload").accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(":Document")));
    }
}
