import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SoldeComponent } from './list/solde.component';
import { SoldeDetailComponent } from './detail/solde-detail.component';
import { SoldeUpdateComponent } from './update/solde-update.component';
import SoldeResolve from './route/solde-routing-resolve.service';

const soldeRoute: Routes = [
  {
    path: '',
    component: SoldeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoldeDetailComponent,
    resolve: {
      solde: SoldeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoldeUpdateComponent,
    resolve: {
      solde: SoldeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoldeUpdateComponent,
    resolve: {
      solde: SoldeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default soldeRoute;
