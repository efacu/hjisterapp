import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IProvincia } from 'app/shared/model/provincia.model';

type EntityResponseType = HttpResponse<IProvincia>;
type EntityArrayResponseType = HttpResponse<IProvincia[]>;

@Injectable({ providedIn: 'root' })
export class ProvinciaService {
  public resourceUrl = SERVER_API_URL + 'api/provincias';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/provincias';

  constructor(protected http: HttpClient) {}

  create(provincia: IProvincia): Observable<EntityResponseType> {
    return this.http.post<IProvincia>(this.resourceUrl, provincia, { observe: 'response' });
  }

  update(provincia: IProvincia): Observable<EntityResponseType> {
    return this.http.put<IProvincia>(this.resourceUrl, provincia, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProvincia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvincia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProvincia[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
