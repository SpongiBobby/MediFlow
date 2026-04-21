import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {formatDate} from "@angular/common";
import {Fieldset} from "primeng/fieldset";
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {InputText} from "primeng/inputtext";
import {ConfirmationService, MessageService, PrimeIcons, PrimeTemplate} from "primeng/api";
import {TableLazyLoadEvent, TableModule} from "primeng/table";
import {Tooltip} from "primeng/tooltip";
import {Router} from '@angular/router';
import {AnagraficaService} from '../../../services/anagrafica.service';
import {Medico, Specializzazione} from '../../../model/medico';
import { DatePicker } from "primeng/datepicker";
import {ConfirmPopup} from 'primeng/confirmpopup';
import {Toast} from 'primeng/toast';
import {AutoComplete, AutoCompleteCompleteEvent} from 'primeng/autocomplete';
import {Select} from 'primeng/select';


@Component({
  selector: 'app-anagrafica-medici',
  imports: [
    Fieldset,
    FormsModule,
    ReactiveFormsModule,
    InputText,
    PrimeTemplate,
    TableModule,
    Tooltip,
    DatePicker,
    ConfirmPopup,
    Toast,
    AutoComplete,
    Select,
  ],
  templateUrl: './anagrafica-medici.component.html',
  styleUrl: './anagrafica-medici.component.css',
})
export class AnagraficaMediciComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service: AnagraficaService = inject(AnagraficaService);
  private confimationService = inject(ConfirmationService);
  private messageService = inject(MessageService);
  private router: Router = inject(Router);

  protected searchForm!: FormGroup;

  protected readonly medici: WritableSignal<Medico[]> = signal([]);
  protected readonly isLoading: WritableSignal<boolean> = signal(false);
  protected readonly first: WritableSignal<number> = signal(0);
  protected readonly totalRecords: WritableSignal<number> = signal(0);
  protected readonly suggestions: WritableSignal<string[]> = signal([]);
  protected readonly specializzazioni: WritableSignal<Specializzazione[]> = signal([]);

  private readonly lastFilters: WritableSignal<any | null> = signal(null);

  ngOnInit(): void {
    this.searchForm = this.fb.group({
      nome: new FormControl({ value: '', disabled: false }, {validators: [] }),
      cognome: new FormControl({ value: '', disabled: false }, { validators: [] }),
      dataNascita: new FormControl({ value: null, disabled: false }, { validators: [] }),
      codiceFiscale: new FormControl({ value: '', disabled: false }, { validators: [] }),
      specializzazione: new FormControl({ value: null, disabled: false }, { validators: [] })
    });

    this.service.getSpecializzazioni().subscribe({
      next: (value) => {
        this.specializzazioni.set(value);
      },
      error: (error) => {
        console.error('Errore durante la ricerca delle specializzazioni:', error.error.message);
      }
    })
  }

  protected search(): void {
    const rawValue = this.searchForm?.value;
    const filters = {
      ...rawValue,
      dataNascita: rawValue?.dataNascita
        ? formatDate(rawValue.dataNascita, 'yyyy-MM-dd', 'en-US')
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
      this.service.codiceFiscaleMedici(query).subscribe({
        next: (value) => {
          this.suggestions.set(value);
        },
        error: (error) => {
          console.error('Errore durante la ricerca dei medici:', error.error.message);
        }
      })
    }
  }

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

            this.fetchData(0, 5, this.lastFilters());
          },
          error: (error) => {
            this.messageService.add({
              severity: 'error',
              summary: 'Errore',
              detail: error.error.message
            });
          }
        });
      }
    })
  }

  private fetchData(page: number, size: number, filters: any | null): void {
    this.isLoading.set(true);

    this.service.getMedici(page, size, filters)
      .subscribe({
        next: (result) => {
          this.medici.set(result.content);
          this.totalRecords.set(result.totalElements);

          this.isLoading.set(false);
        },
        error: () => this.isLoading.set(false)
      })
  }
}
