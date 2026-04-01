import { Injectable , inject} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Prenotazione} from '../model/prenotazione';
import {environment} from '../../../environments/environment';
import {Page} from '../model/page';

@Injectable({
  providedIn: 'root',
})
export class PrenotazioniService {
  private httpClient: HttpClient = inject(HttpClient);
  private api=environment.apiPrenotazione;

  searchPrenotazioni(page: number, size: number, filters: {} | null) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())


    return this.httpClient.post<Page<Prenotazione>>(this.api, filters ?? {}, { params });
  }

  nuovaPrenotazione(prenotazione: Prenotazione) {
    return this.httpClient.post(this.api, prenotazione);

  }

  eliminaPrenotazione(id: number) {
    return this.httpClient.delete(this.api + id);

  }

  modificaPrenotazione(prenotazione: Prenotazione) {
    return this.httpClient.put(this.api +prenotazione.id, prenotazione);
  }
}

