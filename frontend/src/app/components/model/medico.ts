export interface Medico {
  id?: number;
  nome: string;
  cognome: string;
  dataDiNascita: Date;
  luogoDiNascita: string;
  codiceFiscale: string;
  indirizzoDiResidenza: string;
  specializzazione: Specializzazione
  email: string;
  telefono?: number;
  cellulare: number;
  annotazioni?: string;
}

export interface Specializzazione {
  id: number;
  descrizione: string
}
