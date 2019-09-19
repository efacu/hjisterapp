import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { Hjisterapp1TestModule } from '../../../test.module';
import { UbicacionUpdateComponent } from 'app/entities/ubicacion/ubicacion-update.component';
import { UbicacionService } from 'app/entities/ubicacion/ubicacion.service';
import { Ubicacion } from 'app/shared/model/ubicacion.model';

describe('Component Tests', () => {
  describe('Ubicacion Management Update Component', () => {
    let comp: UbicacionUpdateComponent;
    let fixture: ComponentFixture<UbicacionUpdateComponent>;
    let service: UbicacionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [Hjisterapp1TestModule],
        declarations: [UbicacionUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UbicacionUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UbicacionUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UbicacionService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ubicacion(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ubicacion();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
