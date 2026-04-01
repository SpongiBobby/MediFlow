import {Component, inject} from '@angular/core';
import {DatePipe} from "@angular/common";
import {Fieldset} from "primeng/fieldset";
import {FormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {ConfirmationService, MessageService, PrimeIcons, PrimeTemplate} from "primeng/api";
import {TableModule} from "primeng/table";
import {Tooltip} from "primeng/tooltip";
import {Route, Router, RouterOutlet} from '@angular/router';
import {AnagraficaService} from '../../../services/anagrafica.service';

@Component({
  selector: 'app-anagrafica-medici',
  imports: [
    Fieldset,
    FormsModule,
    InputText,
    PrimeTemplate,
    TableModule,
    Tooltip,
    RouterOutlet
  ],
  templateUrl: './anagrafica-medici.component.html',
  styleUrl: './anagrafica-medici.component.css',
})
export class AnagraficaMediciComponent {
  private service: AnagraficaService = inject(AnagraficaService);
  private confimationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private router: Router = inject(Router);

  protected async nuovoMedico(): Promise<void> {
    await this.router.navigate(['anagrafica/medici/new']);
  }

  protected async modificaMedico(id: number): Promise<void> {
    await this.router.navigate(['anagrafica/medici/', id]);
  }

  protected eliminaMedico(event: Event, id: number): void {
    this.confimationService.confirm({
      target: event.target as EventTarget,
      message: 'Sei sicuro di voler eliminare il seguente medico?',
      header: 'Attenzione',
      icon: PrimeIcons.INFO_CIRCLE,
      rejectLabel: 'Annulla',
      acceptButtonProps: {
        label: 'Elimina',
        severity: 'danger'
      },
      accept: () => {
        // Effettuo la chiamata al BE
        this.service.eliminaMedico(id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'info',
              summary: 'Conferma',
              detail: 'Medico eliminato con successo!'
            });
          },
          error: (err) => {
            this.messageService.add({
              severity: 'warning',
              summary: 'Errore',
              detail: 'Non è stato possibilie eliminare il medico!'
            });
          }
        });
      }
    })
  }
}
