import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProvincia } from 'app/shared/model/provincia.model';
import { AccountService } from 'app/core/auth/account.service';
import { ProvinciaService } from './provincia.service';

@Component({
  selector: 'jhi-provincia',
  templateUrl: './provincia.component.html'
})
export class ProvinciaComponent implements OnInit, OnDestroy {
  provincias: IProvincia[];
  currentAccount: any;
  eventSubscriber: Subscription;
  currentSearch: string;

  constructor(
    protected provinciaService: ProvinciaService,
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
      this.provinciaService
        .search({
          query: this.currentSearch
        })
        .pipe(
          filter((res: HttpResponse<IProvincia[]>) => res.ok),
          map((res: HttpResponse<IProvincia[]>) => res.body)
        )
        .subscribe((res: IProvincia[]) => (this.provincias = res), (res: HttpErrorResponse) => this.onError(res.message));
      return;
    }
    this.provinciaService
      .query()
      .pipe(
        filter((res: HttpResponse<IProvincia[]>) => res.ok),
        map((res: HttpResponse<IProvincia[]>) => res.body)
      )
      .subscribe(
        (res: IProvincia[]) => {
          this.provincias = res;
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
    this.registerChangeInProvincias();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProvincia) {
    return item.id;
  }

  registerChangeInProvincias() {
    this.eventSubscriber = this.eventManager.subscribe('provinciaListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
