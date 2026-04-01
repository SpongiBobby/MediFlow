import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Tooltip} from 'primeng/tooltip';
import {Prenotazione, StatoPrenotazione} from '../../../model/prenotazione';
import {DatePipe} from '@angular/common';
import {PrenotazioniService} from '../../../services/prenotazioni.service';
import {Fieldset} from 'primeng/fieldset';
import {InputText} from 'primeng/inputtext';
import {Router} from '@angular/router';
import {Page} from '../../../model/page';
import {delay, of} from 'rxjs';
import {Tag} from 'primeng/tag';
import {DatePicker} from 'primeng/datepicker';
import {ConfirmationService, MessageService, PrimeIcons} from 'primeng/api';
import {Toast} from 'primeng/toast';
import {ConfirmPopup} from 'primeng/confirmpopup';


@Component({
  selector: 'app-prenotazioni',
  providers: [ConfirmationService, MessageService],
  imports: [
    TableModule,
    FormsModule,
    Tooltip,
    DatePipe,
    Fieldset,
    InputText,
    ReactiveFormsModule,
    Tag,
    DatePicker,
    Toast,
    ConfirmPopup
  ],
  templateUrl: './prenotazioni.component.html',
  styleUrl: './prenotazioni.component.css',
})
export class PrenotazioniComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service: PrenotazioniService = inject(PrenotazioniService);
  private confimationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private router: Router = inject(Router);

  protected searchForm!: FormGroup;

  protected readonly prenotazioni: WritableSignal<Prenotazione[]> = signal([]);
  protected readonly isLoading: WritableSignal<boolean> = signal(false);
  protected readonly first: WritableSignal<number> = signal(0);
  protected readonly totalRecords: WritableSignal<number> = signal(0);

  private readonly lastFilters: WritableSignal<any | null> = signal(null);

  MOCK_PRENOTAZIONI: Prenotazione[] = [
    {
      id: 1,
      paziente: { id: 1, codiceFiscale: 'RSSMRA80A01H501Z', nome: 'Mario', cognome: 'Rossi', dataDiNascita: new Date('1980-01-01') },
      medico: { id: 1, codiceFiscale: 'VRDLGI75B02F205X', nome: 'Luigi', cognome: 'Verdi', specializzazione: { id: 1, descrizione: 'Cardiologia' } },
      dataPrenotazione: new Date('2025-04-10T09:00:00'),
      tipologia: { id: 1, descrizione: 'Visita generica' },
      stato: StatoPrenotazione.CONFIRMED
    },
    {
      id: 2,
      paziente: { id: 2, codiceFiscale: 'BNCANNA90C41F839P', nome: 'Anna', cognome: 'Bianchi', dataDiNascita: new Date('1990-03-41') },
      medico: { id: 2, codiceFiscale: 'NRNGPP68D03G224Y', nome: 'Giuseppe', cognome: 'Neri', specializzazione: { id: 2, descrizione: 'Neurologia' } },
      dataPrenotazione: new Date('2025-04-11T10:30:00'),
      tipologia: { id: 2, descrizione: 'Ecografia' },
      stato: StatoPrenotazione.DELETED
    },
    {
      id: 3,
      paziente: { id: 3, codiceFiscale: 'FRRSFN85E14L219K', nome: 'Stefano', cognome: 'Ferrari', dataDiNascita: new Date('1985-05-14') },
      medico: { id: 1, codiceFiscale: 'VRDLGI75B02F205X', nome: 'Luigi', cognome: 'Verdi', specializzazione: { id: 1, descrizione: 'Cardiologia' } },
      dataPrenotazione: new Date('2025-04-12T14:00:00'),
      tipologia: { id: 3, descrizione: 'Elettrocardiogramma' },
      stato: StatoPrenotazione.DELETED
    },
    {
      id: 4,
      paziente: { id: 4, codiceFiscale: 'CNTLRA92H55A662B', nome: 'Laura', cognome: 'Conti', dataDiNascita: new Date('1992-06-15') },
      medico: { id: 3, codiceFiscale: 'MRNGNN70F06H223W', nome: 'Giovanni', cognome: 'Marini', specializzazione: { id: 3, descrizione: 'Ortopedia' } },
      dataPrenotazione: new Date('2025-04-13T11:00:00'),
      tipologia: { id: 4, descrizione: 'Visita ortopedica' },
      stato: StatoPrenotazione.CONFIRMED
    },
    {
      id: 5,
      paziente: { id: 5, codiceFiscale: 'GLLMRC88P20C351T', nome: 'Marco', cognome: 'Galli', dataDiNascita: new Date('1988-09-20') },
      medico: { id: 2, codiceFiscale: 'NRNGPP68D03G224Y', nome: 'Giuseppe', cognome: 'Neri', specializzazione: { id: 2, descrizione: 'Neurologia' } },
      dataPrenotazione: new Date('2025-04-14T16:00:00'),
      tipologia: { id: 5, descrizione: 'Risonanza magnetica' },
      stato: StatoPrenotazione.CONFIRMED
    },
  ];

  ngOnInit(): void {
    this.searchForm = this.fb.group({
      codiceFiscale: new FormControl({ value: '', disabled: false }, { validators: [] }),
      nome: new FormControl({ value: '', disabled: false }, { validators: [] }),
      cognome: new FormControl({ value: '', disabled: false }, { validators: [] }),
      dataPrenotazione: new FormControl({ value: null, disabled: false }, { validators: [] }),
      stato: new FormControl({ value: null, disabled: false }, { validators: [] })
    });
  }

  protected search(): void {
    const rawValue = this.searchForm?.value;
    const filters = {...rawValue};

    console.log(filters);
  }

  protected reset(): void {
    this.searchForm?.reset();
  }

  protected onLazyLoadHandler(event: TableLazyLoadEvent) {
    const offset = event.first ?? 0;
    const rows = event.rows ?? 10;
    const page = offset / rows;

    this.first.set(offset);
    this.fetchData(page, rows, this.lastFilters());
  }

  protected async nuovaPrenotazione(): Promise<void> {
    await this.router.navigate(['gestione/prenota/new']);
  }

  protected async modificaPrenotazione(id: number): Promise<void> {
    await this.router.navigate(['gestione/prenota', id]);
  }

  protected eliminaPrenotazione(event: Event, id: number): void {
    debugger;
    this.confimationService.confirm({
      target: event.target as EventTarget,
      message: 'Sei sicuro di voler eliminare la seguente prenotazione?',
      header: 'Attenzione',
      icon: PrimeIcons.INFO_CIRCLE,
      rejectLabel: 'Annulla',
      acceptButtonProps: {
        label: 'Elimina',
        severity: 'danger'
      },
      accept: () => {
        this.service.eliminaPrenotazione(id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'info',
              summary: 'Conferma',
              detail: 'Prenotazione eliminata con successo!'
            });
          },
          error: (err) => {
            this.messageService.add({
              severity: 'warning',
              summary: 'Errore',
              detail: 'Non è stato possibilie eliminare la prenotazione!'
            });
          }
        });
      }
    })
  }

  private fetchData(page: number, size: number, filters: any | null): void {
    this.isLoading.set(true);

    const start = page * size;
    const end = start + size;
    const content = this.MOCK_PRENOTAZIONI.slice(start, end);

    const result: Page<Prenotazione> = {
      content,
      page: {
        number: page,
        size,
        totalElements: this.MOCK_PRENOTAZIONI.length,
        totalPages: Math.ceil(this.MOCK_PRENOTAZIONI.length / size)
      }
    };

    of(result).pipe(delay(300)).subscribe({
      next: (result) => {
        this.prenotazioni.set(result.content);
        this.totalRecords.set(result.page!.totalElements);

        this.isLoading.set(false);
      }
    });
    /*this.isLoading.set(true);

    this.service.searchPrenotazioni(page, size, filters)
      .subscribe({
        next: (result) => {
          this.prenotazioni.set(result.content);
          this.totalRecords.set(result.page!.totalElements);

          this.isLoading.set(false);
        },
        error: () => this.isLoading.set(false)
      })*/
  }
}
