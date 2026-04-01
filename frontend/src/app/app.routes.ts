import { Routes } from '@angular/router';
import {HomeComponent} from './components/pages/home/home.component';
import {PrenotazioniComponent} from './components/pages/gestione-prenotazione/prenotazioni/prenotazioni.component';
import {
  AnagraficaMediciComponent
} from './components/pages/gestione-anagrafica/anagrafica-medici/anagrafica-medici.component';
import {
  AnagraficaPazientiComponent
} from './components/pages/gestione-anagrafica/anagrafica-pazienti/anagrafica-pazienti.component';
import {PrenotaComponent} from './components/pages/gestione-prenotazione/prenota/prenota.component';
import {
  DetailMedicoComponent
} from './components/pages/gestione-anagrafica/anagrafica-medici/detail-medico/detail-medico.component';
import {
  DetailPazienteComponent
} from './components/pages/gestione-anagrafica/anagrafica-pazienti/detail-paziente/detail-paziente.component';

export const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'gestione',
    children: [
      { path: 'prenota', children:
          [
            { path: '', redirectTo: 'new', pathMatch: 'full' },
            { path: 'new', component: PrenotaComponent },
            { path: ':id', component: PrenotaComponent }
          ]
      },
      { path: 'prenotazioni', component: PrenotazioniComponent },
    ]
  },

  {
    path: 'anagrafica',
    children: [
      { path: 'medici', component: AnagraficaMediciComponent },
      { path: 'medici/new', component: DetailMedicoComponent },
      { path: 'medici/:id', component: DetailMedicoComponent },
      { path: 'pazienti', component: AnagraficaPazientiComponent },
      { path: 'pazienti/new', component: DetailPazienteComponent },
      { path: 'pazienti/:id', component: DetailPazienteComponent }
    ]
  },
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: '**', redirectTo: 'home' }
];
