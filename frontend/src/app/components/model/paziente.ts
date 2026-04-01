export interface Paziente {
  id: number;
  codiceFiscale: string;
  nome: string;
  cognome: string;
  dataDiNascita?: Date;
  luogoDiNascita?: string;
  indirizzoDiResidenza?: string;
  cap?: number;
  telefono?: number;
  email?: string;
}
