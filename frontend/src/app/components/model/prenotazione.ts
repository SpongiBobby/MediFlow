import {Tipologia} from './tipologia';
import {Paziente} from './paziente';
import {Medico} from './medico';

export interface Prenotazione {
  id: number;
  paziente: Paziente;
  medico: Medico;
  dataPrenotazione: Date;
  tipologia: Tipologia;
  stato: StatoPrenotazione
}

export enum StatoPrenotazione {
  CONFIRMED,
  DELETED,
}
