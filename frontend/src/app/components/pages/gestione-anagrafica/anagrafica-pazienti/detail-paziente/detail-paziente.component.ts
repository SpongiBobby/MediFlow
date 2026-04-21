import {Component, inject, OnInit} from '@angular/core';
import {InputText} from 'primeng/inputtext';
import {Textarea} from 'primeng/textarea';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AnagraficaService} from '../../../../services/anagrafica.service';
import {PHONE_PATTERNS} from '../../../../../constants/pattern';
import {ActivatedRoute} from '@angular/router';
import {MessageService} from 'primeng/api';
import {formatDate} from '@angular/common';
import {Toast} from 'primeng/toast';
import {DatePicker} from 'primeng/datepicker';

@Component({
  selector: 'app-detail-paziente',
  imports: [
    InputText,
    Textarea,
    Toast,
    ReactiveFormsModule,
    DatePicker
  ],
  templateUrl: './detail-paziente.component.html',
  styleUrl: './detail-paziente.component.css',
  standalone: true
})
export class DetailPazienteComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private messageService = inject(MessageService);
  private fb = inject(FormBuilder);
  private service: AnagraficaService = inject(AnagraficaService);
  private id: number | null = null;

  protected detailForm!: FormGroup;

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.id = idParam ? parseInt(idParam) : null;

    this.detailForm = this.fb.group({
      nome: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      cognome: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      dataDiNascita: new FormControl({ value: null, disabled: false }, { validators: [Validators.required] }),
      luogoDiNascita: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      codiceFiscale: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      indirizzoDiResidenza: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      annotazioni: new FormControl({ value: '', disabled: false }, { validators: [] }),
      email: new FormControl({ value: '', disabled: false }, { validators: [Validators.required, Validators.email] }),
      cellulare: new FormControl({ value: '', disabled: false }, { validators: [Validators.required, Validators.pattern(PHONE_PATTERNS.IT_MOBILE)] }),
    });

    if (this.id && !isNaN(this.id)) {
      this.fetchPaziente(this.id);
    }
  }

  protected hasError(controlName: string, errorName: string): boolean {
    const control = this.detailForm.get(controlName);
    return !!control && control.invalid && (control.touched || control.dirty) && control.hasError(errorName);
  }

  protected salva(): void {
    if (this.detailForm.invalid) {
      this.detailForm.markAllAsTouched();
      this.messageService.add({
        severity: 'warn',
        summary: 'Attenzione',
        detail: 'Compila correttamente tutti i campi obbligatori.'
      });
      return;
    }

    const paziente = this.detailForm.getRawValue();
    paziente.id = this.id;
    paziente.dataDiNascita = formatDate(paziente.dataDiNascita, 'yyyy-MM-dd', 'en-US');

    this.service.savePaziente(paziente).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'OK',
          detail: 'Paziente salvato con successo.'
        });
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Errore',
          detail: error?.error?.message ?? 'Errore durante il salvataggio del paziente'
        });
      }
    });
  }

  private fetchPaziente(id: number): void {
    this.service.getPaziente(id).subscribe({
      next: (paziente) => {
        this.detailForm.patchValue({
          nome: paziente.nome,
          cognome: paziente.cognome,
          dataDiNascita: new Date(paziente.dataDiNascita),
          luogoDiNascita: paziente.luogoDiNascita,
          codiceFiscale: paziente.codiceFiscale,
          indirizzoDiResidenza: paziente.indirizzoDiResidenza,
          telefono: paziente.telefono,
          cellulare: paziente.cellulare,
          email: paziente.email,
          annotazioni: paziente.annotazioni
        });
      },
      error: (error) => {
        this.messageService.add({
          severity: 'warning',
          summary: 'Errore',
          detail: error?.error?.message ?? 'Errore durante il recupero del paziente'
        });
      }
    });
  }
}
