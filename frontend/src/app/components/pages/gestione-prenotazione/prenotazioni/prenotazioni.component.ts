import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {Tooltip} from 'primeng/tooltip';
import {Prenotazione} from '../../../model/prenotazione';
import {DatePipe, formatDate} from '@angular/common';
import {PrenotazioniService} from '../../../services/prenotazioni.service';
import {Fieldset} from 'primeng/fieldset';
import {InputText} from 'primeng/inputtext';
import {Router} from '@angular/router';
import {Tag} from 'primeng/tag';
import {DatePicker} from 'primeng/datepicker';
import {ConfirmationService, MessageService, PrimeIcons} from 'primeng/api';
import {Toast} from 'primeng/toast';
import {ConfirmPopup} from 'primeng/confirmpopup';
import {AutoComplete, AutoCompleteCompleteEvent} from 'primeng/autocomplete';
import {AnagraficaService} from '../../../services/anagrafica.service';
import {SelectButton} from 'primeng/selectbutton';
import {Select} from 'primeng/select';


@Component({
  selector: 'app-prenotazioni',
  providers: [ConfirmationService, MessageService],
  imports: [
    TableModule,
    FormsModule,
    Tooltip,
    DatePipe,
    Fieldset,
    DatePipe,
    InputText,
    ReactiveFormsModule,
    Tag,
    DatePicker,
    Toast,
    ConfirmPopup,
    AutoComplete,
    Select
  ],
  templateUrl: './prenotazioni.component.html',
  styleUrl: './prenotazioni.component.css',
})
export class PrenotazioniComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service: PrenotazioniService = inject(PrenotazioniService);
  private anagraficaService: AnagraficaService = inject(AnagraficaService);
  private confimationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private router: Router = inject(Router);

  protected searchForm!: FormGroup;

  protected readonly prenotazioni: WritableSignal<Prenotazione[]> = signal([]);
  protected readonly isLoading: WritableSignal<boolean> = signal(false);
  protected readonly first: WritableSignal<number> = signal(0);
  protected readonly totalRecords: WritableSignal<number> = signal(0);
  protected readonly suggestions: WritableSignal<string[]> = signal([]);
  protected readonly stati: WritableSignal<string[]> = signal(['CONFERMATO', 'ANNULLATO']);

  private readonly lastFilters: WritableSignal<any | null> = signal(null);

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
    const filters = {
      ...rawValue,
      dataPrenotazione: rawValue?.dataPrenotazione
        ? formatDate(rawValue.dataPrenotazione, 'yyyy-MM-dd', 'en-US')
        : null
    };

    this.lastFilters.set(filters);
    this.first.set(0);

    this.fetchData(0, 5, filters);
  }

  protected reset(): void {
    this.searchForm?.reset();
    this.search();
  }

  protected onLazyLoadHandler(event: TableLazyLoadEvent) {
    const offset = event.first ?? 0;
    const rows = event.rows ?? 5;
    const page = offset / rows;

    this.first.set(offset);
    this.fetchData(page, rows, this.lastFilters());
  }

  protected onSearch(event: AutoCompleteCompleteEvent) {
    const query = event!.query;
    if(query.length > 0) {
      this.anagraficaService.searchCodiceFiscale(query).subscribe({
        next: (value) => {
          this.suggestions.set(value);
        },
        error: (error) => {
          console.error('Errore durante la ricerca dei pazienti:', error.error.message);
        }
      })
    }
  }

  protected async nuovaPrenotazione(): Promise<void> {
    await this.router.navigate(['gestione/prenota/new']);
  }

  protected async modificaPrenotazione(id: number): Promise<void> {
    await this.router.navigate(['gestione/prenota', id]);
  }

  protected eliminaPrenotazione(event: Event, id: number): void {
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

            this.fetchData(0, 5, this.lastFilters())
          },
          error: () => {
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

    this.service.searchPrenotazioni(page, size, filters)
      .subscribe({
        next: (result) => {
          this.prenotazioni.set(result.content);
          this.totalRecords.set(result.totalElements);

          this.isLoading.set(false);
        },
        error: () => this.isLoading.set(false)
      })
  }
}
