import {Component, inject, OnInit} from '@angular/core';
import {InputText} from 'primeng/inputtext';
import {Textarea} from 'primeng/textarea';
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {AnagraficaService} from '../../../../services/anagrafica.service';
import {PHONE_PATTERNS} from '../../../../../constants/pattern';

@Component({
  selector: 'app-detail-paziente',
  imports: [
    InputText,
    Textarea,
    ReactiveFormsModule
  ],
  templateUrl: './detail-paziente.component.html',
  styleUrl: './detail-paziente.component.css',
  standalone: true
})
export class DetailPazienteComponent implements OnInit {
  private fb = inject(FormBuilder);
  private service: AnagraficaService = inject(AnagraficaService);

  protected detailForm!: FormGroup;


  ngOnInit(): void {
    this.detailForm = this.fb.group({
      nome: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      cognome: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      dataDiNascita: new FormControl({ value: null, disabled: false }, { validators: [Validators.required] }),
      luogoDiNascita: new FormControl({ value: null, disabled: false }, { validators: [Validators.required] }),
      codiceFiscale: new FormControl({ value: '', disabled: true }, { validators: [Validators.required] }),
      indirizzoDiResidenza: new FormControl({ value: '', disabled: false }, { validators: [Validators.required] }),
      annotazioni: new FormControl({ value: '', disabled: false }, { validators: [] }),
      email: new FormControl({ value: '', disabled: false }, { validators: [Validators.required, Validators.email] }),
      telefono: new FormControl({ value: '', disabled: false }, { validators: [Validators.pattern(PHONE_PATTERNS.IT)] }),
      cellulare: new FormControl({ value: '', disabled: false }, { validators: [Validators.required, Validators.pattern(PHONE_PATTERNS.IT_MOBILE)] })    })
  }
}
