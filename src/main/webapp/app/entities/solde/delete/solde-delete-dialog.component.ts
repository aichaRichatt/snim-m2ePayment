import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISolde } from '../solde.model';
import { SoldeService } from '../service/solde.service';

@Component({
  standalone: true,
  templateUrl: './solde-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SoldeDeleteDialogComponent {
  solde?: ISolde;

  protected soldeService = inject(SoldeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.soldeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
