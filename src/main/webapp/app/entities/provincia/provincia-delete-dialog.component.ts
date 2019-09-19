import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProvincia } from 'app/shared/model/provincia.model';
import { ProvinciaService } from './provincia.service';

@Component({
  selector: 'jhi-provincia-delete-dialog',
  templateUrl: './provincia-delete-dialog.component.html'
})
export class ProvinciaDeleteDialogComponent {
  provincia: IProvincia;

  constructor(protected provinciaService: ProvinciaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.provinciaService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'provinciaListModification',
        content: 'Deleted an provincia'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-provincia-delete-popup',
  template: ''
})
export class ProvinciaDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ provincia }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ProvinciaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.provincia = provincia;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/provincia', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/provincia', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
