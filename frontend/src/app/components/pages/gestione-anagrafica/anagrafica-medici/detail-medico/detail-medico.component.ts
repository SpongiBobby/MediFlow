import {Component, inject, OnInit, signal, WritableSignal} from '@angular/core';
import {InputText} from "primeng/inputtext";
import {Textarea} from "primeng/textarea";
import {AnagraficaService} from '../../../../services/anagrafica.service';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {PHONE_PATTERNS} from '../../../../../constants/pattern';
import { ActivatedRoute } from "@angular/router";
import { MessageService } from "primeng/api";
import { formatDate } from "@angular/common";
import { Toast } from "primeng/toast";
import {Select} from 'primeng/select';
import {Medico, Specializzazione} from '../../../../model/medico';
import {forkJoin} from 'rxjs';
import {DatePicker} from 'primeng/datepicker';

@Component({
  selector: 'app-detail-medico',
  imports: [
    InputText,
    Textarea,
    Toast,
    ReactiveFormsModule,
    Select,
    DatePicker
  ],
  templateUrl: './detail-medico.component.html',
  styleUrl: './detail-medico.component.css',
  standalone: true
})
export class DetailMedicoComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private messageService = inject(MessageService);
  private fb = inject(FormBuilder);
  private service: AnagraficaService = inject(AnagraficaService);
  private id: number | null = null;

  protected detailForm!: FormGroup;

  protected readonly specializzazioni: WritableSignal<Specializzazione[]> = signal([]);

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.id = idParam ? parseInt(idParam) : null;

    this.detailForm = this.fb.group({
      nome: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      cognome: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      dataDiNascita: new FormControl({ value: null, disabled: false }, { validators: [Validators.required] }),
      luogoDiNascita: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      codiceFiscale: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      specializzazione: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      annotazioni: new FormControl({ value: '', disabled: false }, { validators: [] }),
      email: new FormControl({ value: '', disabled: false }, { validators: [Validators.required, Validators.email] }),
      indirizzoDiResidenza: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      cellulare: new FormControl({ value: '', disabled: false }, { validators: [Validators.required, Validators.pattern(PHONE_PATTERNS.IT_MOBILE)] })
    });

    this.loadData();
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

    const medico = this.detailForm.getRawValue();
    medico.id = this.id;
    medico.dataDiNascita = formatDate(medico.dataDiNascita, 'yyyy-MM-dd', 'en-US');
    medico.specializzazione = { id: medico.specializzazione };

    this.service.saveMedico(medico).subscribe({
      next: () => {
        this.messageService.add({
          severity: 'success',
          summary: 'OK',
          detail: 'Medico salvato con successo.'
        });
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Errore',
          detail: error?.error?.message ?? 'Errore durante il salvataggio del medico'
        });
      }
    });
  }

  protected hasError(controlName: string, errorName: string): boolean {
    const control = this.detailForm.get(controlName);
    return !!control && control.invalid && (control.touched || control.dirty) && control.hasError(errorName);
  }

  private loadData(): void {
    const requests = []
    requests.push(this.service.getSpecializzazioni());

    if (this.id && !isNaN(this.id)) {
      requests.push(this.service.getMedico(this.id));
    }

    forkJoin(requests).subscribe({
      next: (values) => {
        const specializzazioni = values[0] as Specializzazione[];
        const medico = values[1] as Medico;

        this.specializzazioni.set(specializzazioni);
        if(medico) {
          this.detailForm.patchValue({
            nome: medico.nome,
            cognome: medico.cognome,
            dataDiNascita: new Date(medico.dataDiNascita),
            luogoDiNascita: medico.luogoDiNascita,
            codiceFiscale: medico.codiceFiscale,
            specializzazione: medico.specializzazione.id,
            annotazioni: medico.annotazioni,
            email: medico.email,
            indirizzoDiResidenza: medico.indirizzoDiResidenza,
            cellulare: medico.cellulare,
          });
        }
      }
    })
  }
}
