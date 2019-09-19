import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Ubicacion } from 'app/shared/model/ubicacion.model';
import { UbicacionService } from './ubicacion.service';
import { UbicacionComponent } from './ubicacion.component';
import { UbicacionDetailComponent } from './ubicacion-detail.component';
import { UbicacionUpdateComponent } from './ubicacion-update.component';
import { UbicacionDeletePopupComponent } from './ubicacion-delete-dialog.component';
import { IUbicacion } from 'app/shared/model/ubicacion.model';

@Injectable({ providedIn: 'root' })
export class UbicacionResolve implements Resolve<IUbicacion> {
  constructor(private service: UbicacionService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUbicacion> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Ubicacion>) => response.ok),
        map((ubicacion: HttpResponse<Ubicacion>) => ubicacion.body)
      );
    }
    return of(new Ubicacion());
  }
}

export const ubicacionRoute: Routes = [
  {
    path: '',
    component: UbicacionComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.ubicacion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UbicacionDetailComponent,
    resolve: {
      ubicacion: UbicacionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.ubicacion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UbicacionUpdateComponent,
    resolve: {
      ubicacion: UbicacionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.ubicacion.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UbicacionUpdateComponent,
    resolve: {
      ubicacion: UbicacionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.ubicacion.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const ubicacionPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UbicacionDeletePopupComponent,
    resolve: {
      ubicacion: UbicacionResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'hjisterapp1App.ubicacion.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
