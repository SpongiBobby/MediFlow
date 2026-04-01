import { Component } from '@angular/core';
import {DatePipe} from "@angular/common";
import {PrimeTemplate} from "primeng/api";
import {TableModule} from "primeng/table";
import {Tooltip} from "primeng/tooltip";
import {Step, StepList, StepPanel, StepPanels, Stepper} from 'primeng/stepper';
import {Button} from 'primeng/button';

@Component({
  selector: 'app-prenota',
  imports: [
    DatePipe,
    PrimeTemplate,
    TableModule,
    Tooltip,
    Stepper,
    StepList,
    Step,
    StepPanels,
    StepPanel,
    Button
  ],
  templateUrl: './prenota.component.html',
  styleUrl: './prenota.component.css',
})
export class PrenotaComponent {

}
