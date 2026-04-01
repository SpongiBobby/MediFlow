export interface Medico {
  id: number;
  codiceFiscale: string;
  nome: string;
  cognome: string;
  dataDiNascita?: Date;
  luogoDiNascita?: string;
  specializzazione?: Specializzazione
}

export interface Specializzazione {
  id: number;
  descrizione: string
}
