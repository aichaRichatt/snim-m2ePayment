import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'm2EPaymentApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'solde',
    data: { pageTitle: 'm2EPaymentApp.solde.home.title' },
    loadChildren: () => import('./solde/solde.routes'),
  },
  {
    path: 'payment',
    data: { pageTitle: 'm2EPaymentApp.payment.home.title' },
    loadChildren: () => import('./payment/payment.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
