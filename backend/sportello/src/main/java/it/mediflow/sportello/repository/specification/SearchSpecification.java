package it.mediflow.sportello.repository.specification;

import it.mediflow.sportello.entity.Medici;
import it.mediflow.sportello.entity.Pazienti;
import it.mediflow.sportello.entity.Prenotazioni;
import it.mediflow.sportello.web.dto.MediciFilterDto;
import it.mediflow.sportello.web.dto.PazientiFilterDto;
import it.mediflow.sportello.web.dto.PrenotazioniFilterDto;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SearchSpecification {

    public static Specification<Medici> ricercaMedici(MediciFilterDto filters) {
        return (from, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Codice Fiscale
            if(StringUtils.hasText(filters.getCodiceFiscale())) {
                predicates.add(builder.like(builder.lower(from.get("codiceFiscale")), "%" + filters.getCodiceFiscale() + "%"));
            }

            // Nome
            if(StringUtils.hasText(filters.getNome())) {
                predicates.add(builder.like(builder.lower(from.get("nome")), "%" + filters.getNome() + "%"));
            }

            // Cognome
            if(StringUtils.hasText(filters.getCognome())) {
                predicates.add(builder.like(builder.lower(from.get("cognome")), "%" + filters.getCognome() + "%"));
            }

            // Data di nascita
            if(filters.getDataDiNascita() != null) {
                predicates.add(builder.equal(from.get("dataDiNascita"), filters.getDataDiNascita()));
            }

            // Specializzazione
            if(filters.getIdSpecializzazione() != null) {
                predicates.add(builder.equal(from.get("specializzazione").get("id"), filters.getIdSpecializzazione()));
            }

            // Evito i duplicati
            query.distinct(true);
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Pazienti> ricercaPazienti(PazientiFilterDto filters) {
        return (from, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Codice Fiscale
            if(StringUtils.hasText(filters.getCodiceFiscale())) {
                predicates.add(builder.like(builder.lower(from.get("codiceFiscale")), "%" + filters.getCodiceFiscale() + "%"));
            }

            // Nome
            if(StringUtils.hasText(filters.getNome())) {
                predicates.add(builder.like(builder.lower(from.get("nome")), "%" + filters.getNome() + "%"));
            }

            // Cognome
            if(StringUtils.hasText(filters.getCognome())) {
                predicates.add(builder.like(builder.lower(from.get("cognome")), "%" + filters.getCognome() + "%"));
            }

            // Data di nascita
            if(filters.getDataDiNascita() != null) {
                predicates.add(builder.equal(from.get("dataDiNascita"), filters.getDataDiNascita()));
            }

            // Specializzazione
            if(filters.getLuogoDiNascita() != null) {
                predicates.add(builder.equal(from.get("luogoDiNascita"), filters.getLuogoDiNascita()));
            }

            // Evito i duplicati
            query.distinct(true);
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Prenotazioni> ricercaPrenotazioni(PrenotazioniFilterDto filters) {
        return (from, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Codice Fiscale paziente
            if(StringUtils.hasText(filters.getCodiceFiscale())) {
                predicates.add(builder.like(builder.lower(from.get("paziente").get("codiceFiscale")), "%" + filters.getCodiceFiscale() + "%"));
            }

            // Nome paziente
            if(StringUtils.hasText(filters.getNome())) {
                predicates.add(builder.like(builder.lower(from.get("paziente").get("nome")), "%" + filters.getNome() + "%"));
            }

            // Cognome Paziente
            if(StringUtils.hasText(filters.getCognome())) {
                predicates.add(builder.like(builder.lower(from.get("paziente").get("cognome")), "%" + filters.getCognome() + "%"));
            }

            // Data di prenotazione
            if(filters.getDataDiPrenotazione() != null) {
                predicates.add(builder.equal(from.get("dataPrenotazione"), filters.getDataDiPrenotazione()));
            }

            // Stato
            if(filters.getStato() != null) {
                predicates.add(builder.equal(from.get("stato"), filters.getStato()));
            }

            // Evito i duplicati
            query.distinct(true);
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
