import {Component, inject, OnInit, output, signal, WritableSignal} from '@angular/core';
import {DatePipe, formatDate} from "@angular/common";
import {Fieldset} from "primeng/fieldset";
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {InputText} from "primeng/inputtext";
import {ConfirmationService, MessageService, PrimeIcons, PrimeTemplate} from "primeng/api";
import {Tooltip} from "primeng/tooltip";
import {TableLazyLoadEvent, TableModule} from 'primeng/table';
import {Router} from '@angular/router';
import {AnagraficaService} from '../../../services/anagrafica.service';
import {Paziente} from '../../../model/paziente';
import {AutoComplete, AutoCompleteCompleteEvent} from 'primeng/autocomplete';
import {DatePicker} from 'primeng/datepicker';
import {ConfirmPopup} from 'primeng/confirmpopup';
import {Toast} from 'primeng/toast';

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
    AutoComplete,
    ReactiveFormsModule,
    DatePicker,
    ConfirmPopup,
    Toast
  ],
  templateUrl: './anagrafica-pazienti.component.html',
  styleUrl: './anagrafica-pazienti.component.css',
})
export class AnagraficaPazientiComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service: AnagraficaService = inject(AnagraficaService);
  private confimationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private router: Router = inject(Router);

  protected searchForm!: FormGroup;

  protected readonly pazienti: WritableSignal<Paziente[]> = signal([]);
  protected readonly isLoading: WritableSignal<boolean> = signal(false);
  protected readonly first: WritableSignal<number> = signal(0);
  protected readonly totalRecords: WritableSignal<number> = signal(0);
  protected readonly suggestions: WritableSignal<string[]> = signal([]);

  private readonly lastFilters: WritableSignal<any | null> = signal(null);

  protected items = signal<string[]>([]);
  ngOnInit(): void {
    this.searchForm = this.fb.group({
      codiceFiscale: new FormControl({ value: '', disabled: false }, {validators: [] }),
      nome: new FormControl({ value: '', disabled: false }, { validators: [] }),
      cognome: new FormControl({ value: '', disabled: false }, { validators: [] }),
      dataDiNascita: new FormControl({ value: null, disabled: false }, { validators: [] }),
      luogoDiNascita: new FormControl({ value: '', disabled: false }, { validators: [] }),
    })
  }

  protected search(): void {
    const rawValue = this.searchForm?.value;
    const filters = {
      ...rawValue,
      dataDiNascita: rawValue?.dataDiNascita
        ? formatDate(rawValue.dataDiNascita, 'yyyy-MM-dd', 'en-US')
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

            this.fetchData(0, 5, this.lastFilters());
          },
          error: (err) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Errore',
              detail: err.error.message
            });
          }
        });
      }
    })
  }

  private fetchData(page: number, size: number, filters: any | null): void {
    this.isLoading.set(true);

    this.service.getPazienti(page, size, filters)
      .subscribe({
        next: (result) => {
          this.pazienti.set(result.content);
          this.totalRecords.set(result.totalElements);

          this.isLoading.set(false);
        },
        error: () => this.isLoading.set(false)
      })
  }

  protected onSearch(event: AutoCompleteCompleteEvent) {
    const query = event!.query;
    if(query.length > 0) {
      this.service.searchCodiceFiscale(query).subscribe({
        next: (value) => {
          this.suggestions.set(value);
        },
        error: (error) => {
          console.error('Errore durante la ricerca dei pazienti:', error.error.message);
        }
      })
    }
  }
}
