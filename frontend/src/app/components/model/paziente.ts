export interface Paziente {
  id?: number;
  nome: string;
  cognome: string;
  dataDiNascita: Date;
  luogoDiNascita: string;
  codiceFiscale: string;
  indirizzoDiResidenza: string;
  telefono?: string;
  cellulare: string;
  email: string;
  annotazioni?: string;
}
