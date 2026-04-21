package it.mediflow.sportello.repository.specification;

import it.mediflow.sportello.entity.Medici;
import it.mediflow.sportello.entity.Pazienti;
import it.mediflow.sportello.entity.Prenotazioni;
import it.mediflow.sportello.entity.Specializzazione;
import it.mediflow.sportello.web.dto.MediciFilterDto;
import it.mediflow.sportello.web.dto.PazientiFilterDto;
import it.mediflow.sportello.web.dto.PrenotazioniFilterDto;
import jakarta.persistence.criteria.Join;
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
            Join<Medici, Specializzazione> specializzazione = from.join("specializzazione");


            // Codice Fiscale
            if(StringUtils.hasText(filters.getCodiceFiscale())) {
                predicates.add(builder.like(builder.lower(from.get("codiceFiscale")), "%" +
                        filters.getCodiceFiscale().toLowerCase() + "%"));
            }

            // Nome
            if(StringUtils.hasText(filters.getNome())) {
                predicates.add(builder.like(builder.lower(from.get("nome")), "%" +
                        filters.getNome().toLowerCase() + "%"));
            }

            // Cognome
            if(StringUtils.hasText(filters.getCognome())) {
                predicates.add(builder.like(builder.lower(from.get("cognome")), "%" +
                        filters.getCognome().toLowerCase() + "%"));
            }

            // Data di nascita
            if(filters.getDataNascita() != null) {
                predicates.add(builder.equal(from.get("dataDiNascita"), filters.getDataNascita()));
            }

            // Specializzazione
            if(filters.getSpecializzazione() != null) {
                predicates.add(builder.equal(specializzazione.get("id"), filters.getSpecializzazione()));
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
                predicates.add(builder.like(builder.lower(from.get("codiceFiscale")), "%" +
                        filters.getCodiceFiscale().toLowerCase() + "%"));
            }

            // Nome
            if(StringUtils.hasText(filters.getNome())) {
                predicates.add(builder.like(builder.lower(from.get("nome")), "%" +
                        filters.getNome().toLowerCase() + "%"));
            }

            // Cognome
            if(StringUtils.hasText(filters.getCognome())) {
                predicates.add(builder.like(builder.lower(from.get("cognome")), "%" +
                        filters.getCognome().toLowerCase() + "%"));
            }

            // Data di nascita
            if(filters.getDataDiNascita() != null) {
                predicates.add(builder.equal(from.get("dataDiNascita"), filters.getDataDiNascita()));
            }

            // Specializzazione
            if(filters.getLuogoDiNascita() != null) {
                predicates.add(builder.like(builder.lower(from.get("luogoDiNascita")), "%" +
                        filters.getLuogoDiNascita().toLowerCase() + "%"));
            }

            // Evito i duplicati
            query.distinct(true);
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Prenotazioni> ricercaPrenotazioni(PrenotazioniFilterDto filters) {
        return (from, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Prenotazioni, Pazienti> paziente = from.join("paziente");

            // Codice Fiscale paziente
            if(StringUtils.hasText(filters.getCodiceFiscale())) {
                predicates.add(builder.like(builder.lower(paziente.get("codiceFiscale")), "%" + filters.getCodiceFiscale().toLowerCase() + "%"));
            }

            // Nome paziente
            if(StringUtils.hasText(filters.getNome())) {
                predicates.add(builder.like(builder.lower(paziente.get("nome")), "%" + filters.getNome().toLowerCase() + "%"));
            }

            // Cognome Paziente
            if(StringUtils.hasText(filters.getCognome())) {
                predicates.add(builder.like(builder.lower(paziente.get("cognome")), "%" + filters.getCognome().toLowerCase() + "%"));
            }

            // Data di prenotazione
            if(filters.getDataPrenotazione() != null) {
                predicates.add(builder.equal(from.get("dataPrenotazione"), filters.getDataPrenotazione()));
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
