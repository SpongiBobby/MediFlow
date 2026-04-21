import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Medico, Specializzazione} from '../model/medico';
import {Paziente} from '../model/paziente';
import {Page} from '../model/page';

@Injectable({
  providedIn: 'root',
})
export class AnagraficaService {
  private httpClient: HttpClient = inject(HttpClient);
  private apiMedici= environment.apiMedici;
  private apiSpecializzazioni= environment.apiSpecializzazioni;
  private apiPazienti = environment.apiPazienti;

  public getMedici(page: number, size: number, filters: {} | null) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())

    return this.httpClient.post<Page<Medico>>(`${this.apiMedici}/search`, filters ?? {} , { params });
  }

  public getMedico(id: number) {
    return this.httpClient.get<Medico>(`${this.apiMedici}/${id}`);
  }

  public getSpecializzazioni() {
    return this.httpClient.get<Specializzazione[]>(`${this.apiSpecializzazioni}/`);
  }

  public saveMedico(medico: Medico) {
    return this.httpClient.post(this.apiMedici + '/', medico);
  }

  public eliminaMedico(id: number) {
    return this.httpClient.delete(`${this.apiMedici}/${id}`);
  }

  public codiceFiscaleMedici(cf: string) {
    return this.httpClient.get<string[]>(`${this.apiMedici}/search/cf/${cf}`);
  }

  public detailMedicoByCf(cf: string) {
    return this.httpClient.get<Medico[]>(`${this.apiMedici}/detail/cf/${cf}`);
  }

  public getPazienti(page: number, size: number, filters: {} | null) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())

    return this.httpClient.post<Page<Paziente>>(`${this.apiPazienti}/search`, filters ?? {} , { params });
  }

  public getPaziente(id: number) {
    return this.httpClient.get<Paziente>(`${this.apiPazienti}/${id}`);
  }

  public getPazienteByCf(cf: string) {
    return this.httpClient.get<Paziente>(`${this.apiPazienti}/detail/cf/${cf}`);
  }

  public savePaziente(paziente: Paziente) {
    return this.httpClient.post(this.apiPazienti + '/', paziente);
  }

  public eliminaPaziente(id: number) {
    return this.httpClient.delete(`${this.apiPazienti}/${id}`);
  }

  public searchCodiceFiscale(cf: string) {
    return this.httpClient.get<string[]>(`${this.apiPazienti}/search/cf/${cf}`);
  }
}
