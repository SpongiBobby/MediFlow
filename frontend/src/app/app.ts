import {Component, OnInit } from '@angular/core';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MenuItem, PrimeIcons} from 'primeng/api';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterLinkActive, RouterLink ],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {

    items: MenuItem[] | undefined;
    ngOnInit(): void {
      this.items = [
        {
          label: 'Home',
          icon: PrimeIcons.HOME,
          routerLink: "/home",
        },
        {
          label: 'Gestione',
          items: [
            {
              label: 'Prenotazioni',
              icon: PrimeIcons.BOOK,
              routerLink: "/gestione/prenotazioni",
            },
          ]
        },
        {
          label: 'Gestione anagrafica',
          items: [
            {
              label: 'Medici',
              icon: PrimeIcons.USER,
              routerLink: "/anagrafica/medici",
            },
            {
              label: 'Pazienti',
              icon: PrimeIcons.USER,
              routerLink: "/anagrafica/pazienti",
            },
          ]
        },

      ];
    }

  }
