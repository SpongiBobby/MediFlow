import {Paziente} from './paziente';
import {Medico} from './medico';

export interface Prenotazione {
  id: number,
  paziente: Paziente,
  medico: Medico,
  dataPrenotazione: Date,
  dataVisita: Date,
  annotazioni?: string,
  stato?: string,
}
