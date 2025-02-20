import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ISolde } from '../solde.model';

@Component({
  standalone: true,
  selector: 'jhi-solde-detail',
  templateUrl: './solde-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SoldeDetailComponent {
  solde = input<ISolde | null>(null);

  previousState(): void {
    window.history.back();
  }
}
