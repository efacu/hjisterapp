import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Provincia } from 'app/shared/model/provincia.model';
import { ProvinciaService } from './provincia.service';
import { ProvinciaComponent } from './provincia.component';
import { ProvinciaDetailComponent } from './provincia-detail.component';
import { ProvinciaUpdateComponent } from './provincia-update.component';
import { ProvinciaDeletePopupComponent } from './provincia-delete-dialog.component';
import { IProvincia } from 'app/shared/model/provincia.model';

@Injectable({ providedIn: 'root' })
export class ProvinciaResolve implements Resolve<IProvincia> {
  constructor(private service: ProvinciaService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProvincia> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Provincia>) => response.ok),
        map((provincia: HttpResponse<Provincia>) => provincia.body)
      );
    }
    return of(new Provincia());
  }
}

export const provinciaRoute: Routes = [
  {
    path: '',
    component: ProvinciaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.provincia.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProvinciaDetailComponent,
    resolve: {
      provincia: ProvinciaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.provincia.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProvinciaUpdateComponent,
    resolve: {
      provincia: ProvinciaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.provincia.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProvinciaUpdateComponent,
    resolve: {
      provincia: ProvinciaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.provincia.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const provinciaPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ProvinciaDeletePopupComponent,
    resolve: {
      provincia: ProvinciaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.provincia.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
