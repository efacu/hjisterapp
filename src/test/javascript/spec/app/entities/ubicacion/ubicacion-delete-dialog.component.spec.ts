import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Hjisterapp1TestModule } from '../../../test.module';
import { UbicacionDeleteDialogComponent } from 'app/entities/ubicacion/ubicacion-delete-dialog.component';
import { UbicacionService } from 'app/entities/ubicacion/ubicacion.service';

describe('Component Tests', () => {
  describe('Ubicacion Management Delete Component', () => {
    let comp: UbicacionDeleteDialogComponent;
    let fixture: ComponentFixture<UbicacionDeleteDialogComponent>;
    let service: UbicacionService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Hjisterapp1TestModule],
        declarations: [UbicacionDeleteDialogComponent]
      })
        .overrideTemplate(UbicacionDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UbicacionDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UbicacionService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
