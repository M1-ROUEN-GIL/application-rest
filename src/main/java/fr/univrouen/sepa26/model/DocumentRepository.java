package fr.univrouen.sepa26.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("SELECT d FROM Document d JOIN d.drctDbtTxInfs t WHERE t.pmtId = :pmtId")
    Optional<Document> findByPmtId(String pmtId);

    @Query(value = "SELECT * FROM documents ORDER BY id DESC LIMIT 10", nativeQuery = true)
    List<Document> findLast10();
}
