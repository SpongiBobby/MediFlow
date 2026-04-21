import {Component, computed, inject, signal, WritableSignal} from '@angular/core';
import {MessageService} from "primeng/api";
import { TableModule } from "primeng/table";
import { Toast } from "primeng/toast";
import { FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputText } from 'primeng/inputtext';
import { Textarea } from 'primeng/textarea';
import { DatePicker } from 'primeng/datepicker';
import { AutoComplete, AutoCompleteCompleteEvent } from 'primeng/autocomplete';
import {ActivatedRoute} from '@angular/router';
import {AnagraficaService} from '../../../services/anagrafica.service';
import {PHONE_PATTERNS} from '../../../../constants/pattern';
import {Medico} from '../../../model/medico';
import {Paziente} from '../../../model/paziente';
import {PrenotazioniService} from '../../../services/prenotazioni.service';
import {formatDate} from '@angular/common';

@Component({
  selector: 'app-prenota',
  imports: [
    TableModule,
    Toast,
    ReactiveFormsModule,
    InputText,
    Textarea,
    DatePicker,
    AutoComplete
  ],
  templateUrl: './prenota.component.html',
  styleUrl: './prenota.component.css',
})
export class PrenotaComponent {
  private route = inject(ActivatedRoute);
  private messageService = inject(MessageService);
  private fb = inject(FormBuilder);
  private prenotazioneService: PrenotazioniService = inject(PrenotazioniService);
  private anagraficaService: AnagraficaService = inject(AnagraficaService);
  private id: number | null = null;

  protected readonly pazientiSuggestions: WritableSignal<string[]> = signal([]);
  protected readonly mediciSuggestions: WritableSignal<Medico[]> = signal([]);
  protected readonly pazienteNonTrovato: WritableSignal<boolean> = signal(false);
  protected readonly pazienteSelezionato: WritableSignal<Paziente | null> = signal(null);

  protected readonly disabledForm: WritableSignal<boolean> = signal(false);

  protected prenotazioneForm!: FormGroup;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.id = idParam ? parseInt(idParam) : null;

    this.prenotazioneForm = this.fb.group({
      // Paziente
      codiceFiscale: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      nome: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      cognome: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      dataDiNascita: new FormControl({ value: null, disabled: false }, { validators: [Validators.required] }),
      luogoDiNascita: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      indirizzoDiResidenza: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      cellulare: new FormControl({ value: '', disabled: false }, { validators: [Validators.required, Validators.pattern(PHONE_PATTERNS.IT_MOBILE)] }),
      email: new FormControl({ value: '', disabled: false }, { validators: [Validators.required, Validators.email] }),

      // Prenotazione
      medico: new FormControl({ value: null, disabled: false }, { validators: [Validators.required] }),
      dataPrenotazione: new FormControl({ value: new Date(), disabled: true }, { validators: [] }),
      dataVisita: new FormControl({ value: null, disabled: false }, { validators: [Validators.required] }),
      annotazioni: new FormControl({ value: '', disabled: false }, { validators: [] }),
      stato: new FormControl({ value: null, disabled: true }, { validators: [] })
    });

    if (this.id && !isNaN(this.id)) {
      this.fetchPrenotazioni(this.id)
    }
  }

  protected hasError(controlName: string, errorName: string): boolean {
    const control = this.prenotazioneForm.get(controlName);
    return !!control && control.invalid && (control.touched || control.dirty) && control.hasError(errorName);
  }

  protected onSearchPaziente(event: AutoCompleteCompleteEvent): void {
    const query = (event.query ?? '').trim();

    if (!query) {
      this.pazientiSuggestions.set([]);
      this.pazienteNonTrovato.set(false);
      return;
    }

    this.anagraficaService.searchCodiceFiscale(query).subscribe({
      next: (value) => {
        this.pazientiSuggestions.set(value);
        this.pazienteNonTrovato.set(value.length === 0);
      },
      error: (error) => {
        console.error('Errore durante la ricerca dei pazienti:', error.error.message);
      }
    });
  }

  protected onSearchMedici(event: AutoCompleteCompleteEvent): void {
    const query = (event.query ?? '').trim();

    if (!query) {
      this.mediciSuggestions.set([]);
      return;
    }

    this.anagraficaService.detailMedicoByCf(query).subscribe({
      next: (value) => {
        this.mediciSuggestions.set(value);
      },
      error: (error) => {
        console.error('Errore durante la ricerca dei pazienti:', error.error.message);
      }
    });
  }

  protected onSelectPaziente(cf: string): void {
    this.anagraficaService.getPazienteByCf(cf).subscribe({
      next: (value) => {
        this.pazienteSelezionato.set(value);
        this.pazienteNonTrovato.set(false);

        this.prenotazioneForm.patchValue({
          codiceFiscale: value.codiceFiscale,
          nome: value.nome ?? '',
          cognome: value.cognome ?? '',
          dataDiNascita: value.dataDiNascita ? new Date(value.dataDiNascita) : null,
          luogoDiNascita: value.luogoDiNascita ?? '',
          indirizzoDiResidenza: value.indirizzoDiResidenza ?? '',
          cellulare: value.cellulare ?? '',
          email: value.email ?? '',
        });
      }
    })
  }

  protected onSelectMedico(medico: Medico): void {
    this.prenotazioneForm.patchValue({
      medico: medico
    });
  }

  getMedicoLabel(medico: Medico): string {
    return medico?.nome + ' ' + medico?.cognome;
  }

  protected salva(): void {
    if (this.prenotazioneForm.invalid) {
      this.prenotazioneForm.markAllAsTouched();
      return;
    }

    const raw = this.prenotazioneForm.getRawValue();
    const payload = {
      id: this.id,
      paziente: {
        id: this.pazienteNonTrovato() ?  null : this.pazienteSelezionato()?.id,
        codiceFiscale: raw.codiceFiscale,
        nome: raw.nome,
        cognome: raw.cognome,
        dataDiNascita: formatDate(raw.dataDiNascita, 'yyyy-MM-dd', 'en-US'),
        luogoDiNascita: raw.luogoDiNascita,
        indirizzoDiResidenza: raw.indirizzoDiResidenza ?? '',
        cellulare: raw.cellulare ?? '',
        email: raw.email ?? '',
      },
      medico: raw.medico as Medico,
      dataPrenotazione: formatDate(raw.dataPrenotazione, 'yyyy-MM-dd', 'en-US'),
      dataVisita: formatDate(raw.dataVisita, 'yyyy-MM-dd', 'en-US'),
      annotazioni: raw.annotazioni,
      stato: raw.stato
    };

    this.prenotazioneService.modificaPrenotazione(payload).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'OK',
          detail: 'Prenotazione salvata con successo.'
        });
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Errore',
          detail: error?.error?.message ?? 'Errore durante la salvataggio della prenotazione'
        })
      }
    })
  }

  protected reset(): void {
    this.prenotazioneForm.reset({
      dataPrenotazione: new Date(),
    });
    this.pazienteSelezionato.set(null);
    this.pazienteNonTrovato.set(false);
  }

  private fetchPrenotazioni(id: number): void {
    this.prenotazioneService.getPrenotazione(id).subscribe({
      next: (value) => {
        this.prenotazioneForm.patchValue({
          nome: value.paziente?.nome,
          cognome: value.paziente?.cognome,
          dataDiNascita: this.toDate(value.paziente?.dataDiNascita),
          luogoDiNascita: value.paziente?.luogoDiNascita,
          codiceFiscale: value.paziente?.codiceFiscale,
          indirizzoDiResidenza: value.paziente?.indirizzoDiResidenza,
          telefono: value.paziente?.telefono,
          cellulare: value.paziente?.cellulare,
          email: value.paziente?.email,
          annotazioni: value.paziente?.annotazioni,

          medico: value.medico,
          dataPrenotazione: this.toDate(value.dataPrenotazione),
          dataVisita: this.toDate(value.dataVisita),
          stato: value.stato,
        });

        this.pazienteSelezionato.set(value.paziente);

        const oggi = new Date();
        oggi.setHours(0, 0, 0, 0);

        const dataVisita = this.toDate(value.dataVisita);
        if (dataVisita) {
          dataVisita.setHours(0, 0, 0, 0);
        }
        if(value.stato === 'ANNULLATO' || dataVisita?.getTime() == oggi.getTime()) {
          this.prenotazioneForm.disable();
          this.disabledForm.set(true);
        }
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Errore',
          detail: error?.error?.message ?? 'Errore durante il recupero del paziente'
        });
      }
    });
  }

  private toDate(value: string | Date | null | undefined): Date | null {
    if (!value) return null;
    return value instanceof Date ? value : new Date(value);
  }
}
