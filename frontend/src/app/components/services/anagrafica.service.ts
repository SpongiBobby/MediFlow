import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Medico} from '../model/medico';
import {Paziente} from '../model/paziente';

@Injectable({
  providedIn: 'root',
})
export class AnagraficaService {
  private httpClient: HttpClient = inject(HttpClient);
  private api=environment.apiPrenotazione;

  public getMedici() {
    return this.httpClient.get<Medico[]>(this.api);
  }

  public saveMedico(medico: Medico) {
    return this.httpClient.post(this.api, medico);
  }

  public eliminaMedico(id: number) {
    return this.httpClient.delete(this.api + id);
  }

  public getPazienti() {
    return this.httpClient.get<Paziente[]>(this.api);
  }

  public savePaziente(paziente: Paziente) {
    return this.httpClient.post(this.api, paziente);
  }

  public eliminaPaziente(id: number) {
    return this.httpClient.delete(this.api + id);
  }
}
