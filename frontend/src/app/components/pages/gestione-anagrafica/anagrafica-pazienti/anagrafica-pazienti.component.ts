import {Component, inject} from '@angular/core';
import {DatePipe} from "@angular/common";
import {Fieldset} from "primeng/fieldset";
import {FormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {ConfirmationService, MessageService, PrimeIcons, PrimeTemplate} from "primeng/api";
import {Tooltip} from "primeng/tooltip";
import {TableModule} from 'primeng/table';
import {Router} from '@angular/router';
import {AnagraficaService} from '../../../services/anagrafica.service';
import {Toast} from 'primeng/toast';
import {ConfirmPopup} from 'primeng/confirmpopup';

@Component({
  selector: 'app-anagrafica-pazienti',
  imports: [
    DatePipe,
    Fieldset,
    FormsModule,
    InputText,
    PrimeTemplate,
    Tooltip,
    TableModule,

  ],
  templateUrl: './anagrafica-pazienti.component.html',
  styleUrl: './anagrafica-pazienti.component.css',
})
export class AnagraficaPazientiComponent {
  private service: AnagraficaService = inject(AnagraficaService);
  private confimationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private router: Router = inject(Router);

  protected async nuovoPaziente(): Promise<void> {
    await this.router.navigate(['anagrafica/pazienti/new']);
  }

  protected async modificaPaziente(id: number): Promise<void> {
    await this.router.navigate(['anagrafica/pazienti/', id]);
  }

  protected eliminaPaziente(event: Event, id: number): void {
    this.confimationService.confirm({
      target: event.target as EventTarget,
      message: 'Sei sicuro di voler eliminare il seguente paziente?',
      header: 'Attenzione',
      icon: PrimeIcons.INFO_CIRCLE,
      rejectLabel: 'Annulla',
      acceptButtonProps: {
        label: 'Elimina',
        severity: 'danger'
      },
      accept: () => {
        this.service.eliminaPaziente(id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'info',
              summary: 'Conferma',
              detail: 'Paziente eliminato con successo!'
            });
          },
          error: (err) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Errore',
              detail: 'Non è stato possibilie eliminare il paziente!'
            });
          }
        });
      }
    })
  }
}
