import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IUbicacion } from 'app/shared/model/ubicacion.model';
import { AccountService } from 'app/core/auth/account.service';
import { UbicacionService } from './ubicacion.service';

@Component({
  selector: 'jhi-ubicacion',
  templateUrl: './ubicacion.component.html'
})
export class UbicacionComponent implements OnInit, OnDestroy {
  ubicacions: IUbicacion[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected ubicacionService: UbicacionService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search'] ? this.activatedRoute.snapshot.params['search'] : '';
  }

  loadAll() {
    if (this.currentSearch) {
      this.ubicacionService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IUbicacion[]>) => res.ok),
          map((res: HttpResponse<IUbicacion[]>) => res.body)
        )
        .subscribe((res: IUbicacion[]) => (this.ubicacions = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.ubicacionService
      .query()
      .pipe(
        filter((res: HttpResponse<IUbicacion[]>) => res.ok),
        map((res: HttpResponse<IUbicacion[]>) => res.body)
      )
      .subscribe(
        (res: IUbicacion[]) => {
          this.ubicacions = res;
          this.currentSearch = '';
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  search(query) {
    if (!query) {
      return this.clear();
    }
    this.currentSearch = query;
    this.loadAll();
  }

  clear() {
    this.currentSearch = '';
    this.loadAll();
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInUbicacions();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUbicacion) {
    return item.id;
  }

  registerChangeInUbicacions() {
    this.eventSubscriber = this.eventManager.subscribe('ubicacionListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
