import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUbicacion } from 'app/shared/model/ubicacion.model';
import { UbicacionService } from './ubicacion.service';

@Component({
  selector: 'jhi-ubicacion-delete-dialog',
  templateUrl: './ubicacion-delete-dialog.component.html'
})
export class UbicacionDeleteDialogComponent {
  ubicacion: IUbicacion;

  constructor(protected ubicacionService: UbicacionService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.ubicacionService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'ubicacionListModification',
        content: 'Deleted an ubicacion'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-ubicacion-delete-popup',
  template: ''
})
export class UbicacionDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ubicacion }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UbicacionDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.ubicacion = ubicacion;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/ubicacion', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/ubicacion', { outlets: { popup: null } }]);
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
