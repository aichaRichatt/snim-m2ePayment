import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISolde, NewSolde } from '../solde.model';

export type PartialUpdateSolde = Partial<ISolde> & Pick<ISolde, 'id'>;

type RestOf<T extends ISolde | NewSolde> = Omit<T, 'updatingDate'> & {
  updatingDate?: string | null;
};

export type RestSolde = RestOf<ISolde>;

export type NewRestSolde = RestOf<NewSolde>;

export type PartialUpdateRestSolde = RestOf<PartialUpdateSolde>;

export type EntityResponseType = HttpResponse<ISolde>;
export type EntityArrayResponseType = HttpResponse<ISolde[]>;

@Injectable({ providedIn: 'root' })
export class SoldeService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/soldes');

  create(solde: NewSolde): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solde);
    return this.http.post<RestSolde>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(solde: ISolde): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solde);
    return this.http
      .put<RestSolde>(`${this.resourceUrl}/${this.getSoldeIdentifier(solde)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(solde: PartialUpdateSolde): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(solde);
    return this.http
      .patch<RestSolde>(`${this.resourceUrl}/${this.getSoldeIdentifier(solde)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSolde>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSolde[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSoldeIdentifier(solde: Pick<ISolde, 'id'>): number {
    return solde.id;
  }

  compareSolde(o1: Pick<ISolde, 'id'> | null, o2: Pick<ISolde, 'id'> | null): boolean {
    return o1 && o2 ? this.getSoldeIdentifier(o1) === this.getSoldeIdentifier(o2) : o1 === o2;
  }

  addSoldeToCollectionIfMissing<Type extends Pick<ISolde, 'id'>>(
    soldeCollection: Type[],
    ...soldesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const soldes: Type[] = soldesToCheck.filter(isPresent);
    if (soldes.length > 0) {
      const soldeCollectionIdentifiers = soldeCollection.map(soldeItem => this.getSoldeIdentifier(soldeItem));
      const soldesToAdd = soldes.filter(soldeItem => {
        const soldeIdentifier = this.getSoldeIdentifier(soldeItem);
        if (soldeCollectionIdentifiers.includes(soldeIdentifier)) {
          return false;
        }
        soldeCollectionIdentifiers.push(soldeIdentifier);
        return true;
      });
      return [...soldesToAdd, ...soldeCollection];
    }
    return soldeCollection;
  }

  protected convertDateFromClient<T extends ISolde | NewSolde | PartialUpdateSolde>(solde: T): RestOf<T> {
    return {
      ...solde,
      updatingDate: solde.updatingDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSolde: RestSolde): ISolde {
    return {
      ...restSolde,
      updatingDate: restSolde.updatingDate ? dayjs(restSolde.updatingDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSolde>): HttpResponse<ISolde> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSolde[]>): HttpResponse<ISolde[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
