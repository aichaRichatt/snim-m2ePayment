import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISolde } from '../solde.model';
import { SoldeService } from '../service/solde.service';

const soldeResolve = (route: ActivatedRouteSnapshot): Observable<null | ISolde> => {
  const id = route.params['id'];
  if (id) {
    return inject(SoldeService)
      .find(id)
      .pipe(
        mergeMap((solde: HttpResponse<ISolde>) => {
          if (solde.body) {
            return of(solde.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default soldeResolve;
